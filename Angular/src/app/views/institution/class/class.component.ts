import {Component, OnInit, ViewChild} from '@angular/core';
import {InstitutionService} from '../../../shared/services/institution/institution.service';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';
import {FormBuilder, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {ActivatedRoute} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {GroupResponse} from '../../../shared/models/institution/GroupResponse';
import {GroupRequest} from '../../../shared/models/institution/GroupRequest';
import {GroupService} from '../../../shared/services/institution/group.service';
import {InstitutionResponse} from '../../../shared/models/institution/InstitutionResponse';

@Component({
  selector: 'app-class',
  templateUrl: './class.component.html',
  styleUrls: ['./class.component.scss']
})
export class ClassComponent implements OnInit {
  set currentPageClasses(value: number) {
    this._currentPageClasses = value;
    this.loadGroups(this._currentPageClasses, this.itemsPerPageClasses);
  }
  get currentPageClasses(): number {
    return this._currentPageClasses;
  }
  currentClass: GroupResponse;
  classes: GroupResponse[] = [];
  _currentPageClasses = 1;
  totalPagesClasses = 0;
  totalItemsClasses = 0;
  itemsPerPageClasses = 10;
  createClassForm = this.formBuilder.group({
    name: ['', Validators.required],
    students: [[], Validators.required]
  });
    addUserForm = this.formBuilder.group({
        userEmail: ['', [Validators.required, Validators.email]]
    });
    removeUserForm = this.formBuilder.group({
        userEmail: ['', [Validators.required, Validators.email]]
    });
  groupRequest: GroupRequest;
  constructor(
      private institutionService: InstitutionService,
      private handleResponse: ResponseHandlerService,
      private formBuilder: FormBuilder,
      private toastr: ToastrService,
      private route: ActivatedRoute,
      private modalService: NgbModal,
      private groupService: GroupService
  ) { }
  institutionID;
    currentInstitution: InstitutionResponse;
    currentGroup: GroupResponse;
    loadingClasses = false;
    students;
    loading = false;
    @ViewChild('listModal') listModal;
    modalTitle: string;
    modalList: string[];
  ngOnInit(): void {
      this.institutionID = this.route.snapshot.paramMap.get('institutionID');
        this.institutionService.getInstitutionByID(this.institutionID).subscribe(
            response => {
                this.currentInstitution = response;
            },
            error => {
                this.handleResponse.handleError(error);
            }
        );
        this.loadGroups(this.currentPageClasses, this.itemsPerPageClasses);
    this.institutionService.getInstitutionStudents(this.institutionID).subscribe(
        response => {
          console.log(response);
          this.students = response;
        }, error => {
          this.handleResponse.handleError(error);
        }
    );
    }
  addClass() {
    if (this.createClassForm.valid) {
      this.loadingClasses = true;
      this.groupRequest = this.createClassForm.getRawValue();
      this.groupRequest.institutionID = this.institutionID;
      this.groupService.createGroup(this.groupRequest).subscribe(
          response => {
            this.toastr.success('Class created successfully');
            this.createClassForm.reset();
            this.loadGroups(this.currentPageClasses, this.itemsPerPageClasses);
            this.loadingClasses = false;
          }, error => {
            this.handleResponse.handleError(error);
            this.loadingClasses = false;
          }
      );
    } else {
      this.toastr.error('Please fill all fields');
    }
  }
    openUserModal(content, group: GroupResponse): void {
      this.currentGroup = group;
        this.modalService.open(content, { size: 'lg' });
    }
    addUser() {
        if (this.addUserForm.valid) {
            this.loading = true;
            this.groupService.addStudentToGroup(this.currentGroup.id, this.addUserForm.controls.userEmail.value).subscribe(
                response => {
                    this.toastr.success('User added successfully');
                    this.addUserForm.reset();
                    this.loading = false;
                    this.loadGroups(this.currentPageClasses, this.itemsPerPageClasses);
                }, error => {
                    this.handleResponse.handleError(error);
                    this.loading = false;
                }
            );
        } else {
            this.toastr.error('Please fill all fields');
        }
    }
    removeUser() {
        if (this.removeUserForm.valid) {
            this.loading = true;
            this.groupService.removeStudentFromGroup(this.currentGroup.id, this.removeUserForm.controls.userEmail.value).subscribe(
                response => {
                    this.toastr.success('User removed successfully');
                    this.removeUserForm.reset();
                    this.loading = false;
                    this.loadGroups(this.currentPageClasses, this.itemsPerPageClasses);
                }, error => {
                    this.handleResponse.handleError(error);
                    this.loading = false;
                }
            );
        } else {
            this.toastr.error('Please fill all fields');
        }
    }
  loadGroups(page: number, size: number) {
    this.loadingClasses = true;
    this.groupService.getGroupsByInstitution(this.institutionID, page - 1, size).subscribe(
        response => {
          console.log(response);
          this.classes = response.groups;
          this._currentPageClasses = response.currentPage + 1;
          this.totalPagesClasses = response.totalPages;
          this.totalItemsClasses = response.totalItems;
          this.itemsPerPageClasses = response.itemsPerPage;
          this.loadingClasses = false;
        }, error => {
          this.handleResponse.handleError(error);
          this.loadingClasses = false;
        }
    );
  }
    openModal(type: string, list: string[]): void {
        this.modalTitle = type === 'students' ? 'Students' : 'Courses';
        this.modalList = list;
        this.modalService.open(this.listModal, { size: 'lg' });
    }
  createClassModel(content) {
    this.modalService.open(content, { ariaLabelledBy: 'create Class' })
        .result.then((result) => {
      console.log(result);
    }, (reason) => {
      console.log('Err!', reason);
    });
  }
  modalConfirmClassFunction(content: any, group: GroupResponse) {
    this.currentClass = group;
    this.modalService.open(content, { ariaLabelledBy: 'confirm class' })
        .result.then((result) => {
      if (result === 'Ok') {
        this.removeGroup(group);
      }
    }, (reason) => {
      console.log('Err!', reason);
    });
  }
  removeGroup(group: GroupResponse) {
    this.loadingClasses = true;
    this.groupService.deleteGroup(group.id).subscribe(
        response => {
          this.toastr.success('Class removed successfully');
          this.loadGroups(this.currentPageClasses, this.itemsPerPageClasses);
          this.loadingClasses = false;
        }, error => {
          this.handleResponse.handleError(error);
          this.loadingClasses = false;
        }
    );
  }
  shouldShowError(controlName: string, errorName: string): boolean {
    const control = this.createClassForm.get(controlName);
    return control && control.errors && control.errors[errorName] && (control.dirty || control.touched);
  }
}
