import {Component, OnInit} from '@angular/core';
import {InstitutionUserResponse} from '../../../shared/models/institution/InstitutionUserResponse';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {debounceTime} from 'rxjs/operators';
import {InstitutionResponse} from '../../../shared/models/institution/InstitutionResponse';
import {UserResponse} from '../../../shared/models/user/UserResponse';
import {InstitutionService} from '../../../shared/services/institution/institution.service';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';
import {ToastrService} from 'ngx-toastr';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {
  get currentPageUsers(): number {
    return this._currentPageUsers;
  }
  set currentPageUsers(value: number) {
    this._currentPageUsers = value;
    if (this.searchControlUsers.value == null) {
      this.getInstitutionUsers(this._currentPageUsers, this.itemsPerPageUsers, null, null);
    } else {
      this.getInstitutionUsers(this._currentPageUsers, this.itemsPerPageUsers, this.searchControlUsers.value, null);
    }
  }
  constructor(
      private institutionService: InstitutionService,
      private handleResponse: ResponseHandlerService,
      private formBuilder: FormBuilder,
      private toastr: ToastrService,
      private route: ActivatedRoute,
      private modalService: NgbModal
  ) { }
  currentUser: InstitutionUserResponse;
    institutionID;
    currentInstitution: InstitutionResponse;
  searchControlUsers: FormControl = new FormControl();
  users: InstitutionUserResponse[] = [];
  _currentPageUsers = 1;
  totalPagesUsers = 0;
  totalItemsUsers = 0;
  itemsPerPageUsers = 10;
  loadingUsers = false;
  roles = ['Admin', 'Teacher', 'Student'];
  addUserForm = this.formBuilder.group({
        email: ['', [Validators.required, Validators.email]],
        role: ['', [Validators.required]],
      }
  );
  selectedRole = '';
  availableRoles: string[] = ['ADMIN', 'STUDENT', 'TEACHER'];
  ngOnInit() {
    this.institutionID = this.route.snapshot.paramMap.get('institutionID');
    this.institutionService.getInstitutionByID(this.institutionID).subscribe(
        response => {
          this.currentInstitution = response;
        }
    );
    this.getInstitutionUsers(this.currentPageUsers, this.itemsPerPageUsers, null, null);
    this.searchControlUsers.valueChanges
        .pipe(debounceTime(200))
        .subscribe(value => {
          this.getInstitutionUsers(1, this.itemsPerPageUsers, value, null);
        });
  }
  shouldShowError(controlName: string, errorName: string): boolean {
    const control = this.addUserForm.get(controlName);
    return control && control.errors && control.errors[errorName] && (control.dirty || control.touched);
  }
  inviteUser() {
    this.loadingUsers = true;
    if (this.addUserForm.valid) {
      this.institutionService.inviteUser(this.institutionID,
          this.addUserForm.controls.email.value,
          this.addUserForm.controls.role.value.toUpperCase()).subscribe(
          response => {
            this.toastr.success('User invited successfully');
            this.addUserForm.reset();
            this.getInstitutionUsers(this.currentPageUsers, this.itemsPerPageUsers, null, null);
            this.loadingUsers = false;
          }, error => {
            this.handleResponse.handleError(error);
            this.loadingUsers = false;
          }
      );
    } else {
      this.toastr.error('Please fill all fields correctly');
      this.loadingUsers = false;
    }
  }
  addUserModel(content) {
    this.modalService.open(content, { ariaLabelledBy: 'Invite user' })
        .result.then((result) => {
      console.log(result);
    }, (reason) => {
      console.log('Err!', reason);
    });
  }
  getInstitutionUsers(page: number, size: number, keyword: string, role: string) {
    this.loadingUsers = true;
    this.institutionService.getInstitutionUsers(this.institutionID, keyword, role, page - 1, size).subscribe(
        response => {
          console.log(response);
          this.users = response.users;
          this._currentPageUsers = response.currentPage + 1;
          this.totalPagesUsers = response.totalPages;
          this.totalItemsUsers = response.totalItems;
          this.itemsPerPageUsers = response.itemsPerPage;
          this.loadingUsers = false;
        }, error => {
          this.handleResponse.handleError(error);
          this.loadingUsers = false;
        }
    );
  }
  removeInstitutionUser(user: InstitutionUserResponse) {
    this.loadingUsers = true;
    this.institutionService.removeInstitutionUser(this.institutionID, user.email).subscribe(
        response => {
          this.toastr.success('User removed successfully');
          this.getInstitutionUsers(this.currentPageUsers, this.itemsPerPageUsers, null, null);
          this.loadingUsers = false;
        }, error => {
          this.handleResponse.handleError(error);
          this.loadingUsers = false;
        }
    );
  }
  modalConfirmUserFunction(content: any, user: InstitutionUserResponse) {
    this.currentUser = user;
    this.modalService.open(content, { ariaLabelledBy: 'confirm User' })
        .result.then((result) => {
      if (result === 'Ok') {
        this.removeInstitutionUser(user);
      }
    }, (reason) => {
      console.log('Err!', reason);
    });
  }
  changeUserRole(user: UserResponse) {
    this.loadingUsers = true;
    if (this.selectedRole && !user.roles.includes(this.selectedRole)) {
      this.institutionService.addInstitutionUserRole(this.institutionID, user.email, this.selectedRole).subscribe(res => {
        this.toastr.success('User role updated successfully');
        user.roles.push(this.selectedRole);
        this.loadingUsers = false;
      }, error => {
        this.handleResponse.handleError(error);
        this.loadingUsers = false;
      });
    } else if (this.selectedRole && user.roles.includes(this.selectedRole)) {
      this.institutionService.removeInstitutionUserRole(this.institutionID, user.email, this.selectedRole).subscribe(res => {
        this.toastr.success('User role updated successfully');
        user.roles = user.roles.filter(role => role !== this.selectedRole);
        this.loadingUsers = false;
      }, error => {
        this.handleResponse.handleError(error);
        this.loadingUsers = false;
      });
    }
  }
}
