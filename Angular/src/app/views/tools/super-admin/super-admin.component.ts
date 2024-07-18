import {Component, OnInit} from '@angular/core';
import {ProductService} from '../../../shared/services/product.service';
import {SuperAdminService} from '../../../shared/services/user/super-admin.service';
import {PaginatedUsersResponse} from '../../../shared/models/user/PaginatedUsersResponse';
import {UserResponse} from '../../../shared/models/user/UserResponse';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';
import {FormControl} from '@angular/forms';
import {debounceTime} from 'rxjs/operators';

@Component({
  selector: 'app-super-admin',
  templateUrl: './super-admin.component.html',
  styleUrls: ['./super-admin.component.scss']
})
export class SuperAdminComponent implements OnInit {
  users: UserResponse[] = [];
  _currentPage = 1;
  totalPages = 0;
  totalItems = 0;
  itemsPerPage = 5;
  loading = false;
  selectedRole = '';
  availableRoles: string[] = ['SUPERADMIN', 'ADMIN', 'STUDENT', 'TEACHER'];
  searchControl: FormControl = new FormControl();

  get currentPage(): number {
    return this._currentPage;
  }

  set currentPage(value: number) {
    this._currentPage = value;
    if (this.searchControl.value == null) {
      this.loadUsers(this._currentPage, this.itemsPerPage, '');
    } else {
        this.loadUsers(this._currentPage, this.itemsPerPage, this.searchControl.value);
    }
  }
  constructor(
      private superAdminService: SuperAdminService,
      private handleResponse: ResponseHandlerService
  ) { }

  ngOnInit() {
    this.loadUsers(this.currentPage, this.itemsPerPage, '');
      this.searchControl.valueChanges
          .pipe(debounceTime(200))
          .subscribe(value => {
            this.loadUsers(1, this.itemsPerPage, value);
          });

  }
  loadUsers(page: number, size: number, keyword: string) {
    this.loading = true;
    this.superAdminService.getUsers(page - 1, size, keyword).subscribe((response: PaginatedUsersResponse) => {
      console.log(response);
      this.users = response.users;
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
  toggleBan(user: UserResponse) {
    this.superAdminService.toggleBan(user.email).subscribe(res => {
      user.security.ban = !user.security.ban;
      this.handleResponse.handleSuccess(res.message);
    }, error => {
      this.handleResponse.handleError(error);
    }
    );
  }
  toggleEnabled(user: UserResponse) {
    this.superAdminService.toggleEnable(user.email).subscribe(res => {
      user.security.enabled = !user.security.enabled;
      this.handleResponse.handleSuccess(res.message);
    }, error => {
      this.handleResponse.handleError(error);
    }
    );
  }

  changeUserRole(user: UserResponse) {
    if (this.selectedRole && !user.roles.includes(this.selectedRole)) {
      this.superAdminService.addRole(user.email, this.selectedRole).subscribe(res => {
        this.handleResponse.handleSuccess(res.message);
        user.roles.push(this.selectedRole);
      }, error => {
        this.handleResponse.handleError(error);
      });
    } else if (this.selectedRole && user.roles.includes(this.selectedRole)) {
      this.superAdminService.removeRole(user.email, this.selectedRole).subscribe(res => {
        this.handleResponse.handleSuccess(res.message);
        user.roles = user.roles.filter(role => role !== this.selectedRole);
      }, error => {
        this.handleResponse.handleError(error);
      });
    }
  }
}
