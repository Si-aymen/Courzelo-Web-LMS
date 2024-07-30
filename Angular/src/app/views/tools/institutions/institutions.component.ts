import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';
import {debounceTime} from 'rxjs/operators';
import {InstitutionResponse} from '../../../shared/models/institution/InstitutionResponse';
import {InstitutionService} from '../../../shared/services/institution/institution.service';
import {PaginatedInstitutionsResponse} from '../../../shared/models/institution/PaginatedInstitutionsResponse';
import {ToastrService} from 'ngx-toastr';
import {InstitutionRequest} from '../../../shared/models/institution/InstitutionRequest';
import {UserService} from '../../../shared/services/user/user.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {InstitutionUserResponse} from '../../../shared/models/institution/InstitutionUserResponse';
import {UserResponse} from '../../../shared/models/user/UserResponse';

@Component({
  selector: 'app-institutions',
  templateUrl: './institutions.component.html',
  styleUrls: ['./institutions.component.scss']
})
export class InstitutionsComponent implements OnInit {
    get currentPageUsers(): number {
        return this._currentPageUsers;
    }
  get currentPage(): number {
    return this._currentPage;
  }
  set currentPageUsers(value: number) {
    this._currentPageUsers = value;
    if (this.searchControlUsers.value == null) {
        this.getInstitutionUsers(this._currentPageUsers, this.itemsPerPageUsers, null, null);
    } else {
        this.getInstitutionUsers(this._currentPageUsers, this.itemsPerPageUsers, this.searchControlUsers.value, null);
    }
  }
  set currentPage(value: number) {
    this._currentPage = value;
    if (this.searchControl.value == null) {
      this.loadInstitutions(this._currentPage, this.itemsPerPage, '');
    } else {
      this.loadInstitutions(this._currentPage, this.itemsPerPage, this.searchControl.value);
    }
  }
  constructor(
      private institutionService: InstitutionService,
      private handleResponse: ResponseHandlerService,
      private formBuilder: FormBuilder,
      private toastr: ToastrService,
      private userService: UserService,
      private modalService: NgbModal
  ) { }
    showInstitutionsTable = true;
  institutions: InstitutionResponse[] = [];
  currentInstitution: InstitutionResponse;
  currentUser: InstitutionUserResponse;
  _currentPage = 1;
  totalPages = 0;
  totalItems = 0;
  itemsPerPage = 10;
  showInstitutionUsersTable = false;
  users: InstitutionUserResponse[] = [];
    _currentPageUsers = 1;
    totalPagesUsers = 0;
    totalItemsUsers = 0;
    itemsPerPageUsers = 10;
  loading = false;
  loadingUsers = false;
    roles = ['Admin', 'Teacher', 'Student'];
  searchControl: FormControl = new FormControl();
    searchControlUsers: FormControl = new FormControl();
    addInstitutionForm = this.formBuilder.group({
            name: ['', [Validators.required, Validators.maxLength(40), Validators.minLength(3)]],
            slogan: ['', [Validators.required, Validators.maxLength(50), Validators.minLength(3)]],
            country: ['', [Validators.required]],
            address: ['', [Validators.required, Validators.maxLength(100), Validators.minLength(10)]],
            description: ['', [Validators.required, Validators.maxLength(500), Validators.minLength(10)]],
            website: ['', [Validators.required, Validators.maxLength(100), Validators.minLength(5)]],
        }
    );
    addUserForm = this.formBuilder.group({
            email: ['', [Validators.required, Validators.email]],
            role: ['', [Validators.required]],
        }
    );
    institutionRequest: InstitutionRequest = {};
    countries = [];
    selectedRole = '';
    availableRoles: string[] = ['ADMIN', 'STUDENT', 'TEACHER'];
  ngOnInit() {
    this.loadInstitutions(this.currentPage, this.itemsPerPage, '');
    this.searchControl.valueChanges
        .pipe(debounceTime(200))
        .subscribe(value => {
          this.loadInstitutions(1, this.itemsPerPage, value);
        });
      this.searchControlUsers.valueChanges
          .pipe(debounceTime(200))
          .subscribe(value => {
              this.getInstitutionUsers(1, this.itemsPerPageUsers, value, null);
          });
      this.userService.getCountries().subscribe(
          countries => {
              this.countries = countries;
              console.log(this.countries);
          }
      );
  }
    shouldShowError(controlName: string, errorName: string): boolean {
        const control = this.addInstitutionForm.get(controlName);
        return control && control.errors && control.errors[errorName] && (control.dirty || control.touched);
    }
    addInstitution() {
        if (this.addInstitutionForm.valid) {
            this.institutionRequest = this.addInstitutionForm.value;
        this.institutionService.addInstitution(this.institutionRequest).subscribe(
            response => {
                this.toastr.success('Institution added successfully');
                this.addInstitutionForm.reset();
                this.loadInstitutions(this.currentPage, this.itemsPerPage, '');
            }, error => {
                this.handleResponse.handleError(error);
            }
        );
    } else {
            this.toastr.error('Please fill all fields');

        }
    }
    inviteUser() {
        this.loadingUsers = true;
        if (this.addUserForm.valid) {
            this.institutionService.inviteUser(this.currentInstitution.id,
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
  loadInstitutions(page: number, size: number, keyword: string) {
    this.loading = true;
    this.institutionService.getInstitutions(page - 1, size, keyword).subscribe((response: PaginatedInstitutionsResponse) => {
          console.log(response);
          this.institutions = response.institutions;
          this._currentPage = response.currentPage + 1;
          this.totalPages = response.totalPages;
          this.totalItems = response.totalItems;
          this.itemsPerPage = response.itemsPerPage;
          this.loading = false;
        }, error => {
          this.handleResponse.handleError(error);
          this.loading = false;
        }
    );
  }
    addInstitutionModel(content) {
        this.modalService.open(content, { ariaLabelledBy: 'add Institution' })
            .result.then((result) => {
            console.log(result);
        }, (reason) => {
            console.log('Err!', reason);
        });
    }
    deleteInstitution(institutionID: string) {
        this.institutionService.deleteInstitution(institutionID).subscribe(
            response => {
                this.toastr.success('Institution deleted successfully');
                this.loadInstitutions(this.currentPage, this.itemsPerPage, '');
            }, error => {
                this.handleResponse.handleError(error);
            }
        );
    }
    updateInstitution(id: string) {
      if (this.addInstitutionForm.valid) {
          const institution: InstitutionRequest = this.addInstitutionForm.value;
          this.institutionService.updateInstitution(id, institution).subscribe(
              response => {
                  this.toastr.success('Institution updated successfully');
                  this.addInstitutionForm.reset();
                  this.loadInstitutions(this.currentPage, this.itemsPerPage, '');
              }, error => {
                  this.handleResponse.handleError(error);
              }
          );
      } else {
          this.toastr.error('Please fill all fields');
      }
    }
    deleteInstitutionModal(content, institution: InstitutionResponse) {
        this.currentInstitution = institution;
        const modalRef = this.modalService.open(content, { ariaLabelledBy: 'delete Institution'});
        modalRef.result.then((result) => {
            if (result === 'Ok') {
                this.deleteInstitution(this.currentInstitution.id);
            }
        }, (reason) => {
            console.log('Dismissed', reason);
        });
    }
    addUserModel(content, institution: InstitutionResponse) {
        this.currentInstitution = institution;
        this.modalService.open(content, { ariaLabelledBy: 'invite User' })
            .result.then((result) => {
            console.log(result);
        }, (reason) => {
            console.log('Err!', reason);
        });
    }
    editInstitutionModel(content, institution: InstitutionResponse) {
        this.currentInstitution = institution;
        this.addInstitutionForm.controls.name.setValue(institution.name);
        this.addInstitutionForm.controls.slogan.setValue(institution.slogan);
        this.addInstitutionForm.controls.country.setValue(institution.country);
        this.addInstitutionForm.controls.address.setValue(institution.address);
        this.addInstitutionForm.controls.description.setValue(institution.description);
        this.addInstitutionForm.controls.website.setValue(institution.website);
        this.modalService.open(content, { ariaLabelledBy: 'edit Institution' })
            .result.then((result) => {
            console.log(result);
        }, (reason) => {
            console.log('Err!', reason);
        });
    }
    getInstitutionUsers(page: number, size: number, keyword: string, role: string) {
        this.loadingUsers = true;
        this.institutionService.getInstitutionUsers(this.currentInstitution.id, keyword, role, page - 1, size).subscribe(
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
    toggleInstitutionUsersTable(institution: InstitutionResponse) {
      if (this.showInstitutionsTable) {
          this.showInstitutionsTable = false;
          this.showInstitutionUsersTable = true;
          this.loadingUsers = true;
          this.currentInstitution = institution;
          this.getInstitutionUsers(1, this.itemsPerPageUsers, null, null);
      } else {
            this.showInstitutionsTable = true;
            this.showInstitutionUsersTable = false;
      }
    }
    removeInstitutionUser(user: InstitutionUserResponse) {
        this.loadingUsers = true;
        this.institutionService.removeInstitutionUser(this.currentInstitution.id, user.email).subscribe(
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
            this.institutionService.addInstitutionUserRole(this.currentInstitution.id, user.email, this.selectedRole).subscribe(res => {
                this.toastr.success('User role updated successfully');
                user.roles.push(this.selectedRole);
                this.loadingUsers = false;
            }, error => {
                this.handleResponse.handleError(error);
                this.loadingUsers = false;
            });
        } else if (this.selectedRole && user.roles.includes(this.selectedRole)) {
            this.institutionService.removeInstitutionUserRole(this.currentInstitution.id, user.email, this.selectedRole).subscribe(res => {
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
