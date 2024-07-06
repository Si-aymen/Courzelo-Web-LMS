import {Component, OnInit} from '@angular/core';
import {ProductService} from '../../../shared/services/product.service';
import {SuperAdminService} from '../../../shared/services/user/super-admin.service';
import {PaginatedUsersResponse} from '../../../shared/models/user/PaginatedUsersResponse';
import {UserResponse} from '../../../shared/models/user/UserResponse';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';
import {FormControl} from '@angular/forms';

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
  autocompletes$: string[] = ['STUDENT', 'TEACHER', 'ADMIN', 'SUPERADMIN'];

  get currentPage(): number {
    return this._currentPage;
  }

  set currentPage(value: number) {
    this._currentPage = value;
    this.loadUsers(this._currentPage, this.itemsPerPage); // Automatically reload users when currentPage changes
  }
  constructor(
      private superAdminService: SuperAdminService,
      private handleResponse: ResponseHandlerService
  ) { }

  ngOnInit() {
    this.loadUsers(this.currentPage, this.itemsPerPage);
  }
  loadUsers(page: number, size: number) {
    this.loading = true;
    this.superAdminService.getUsers(page - 1, size).subscribe((response: PaginatedUsersResponse) => {
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
    this.loading = true;
    this.superAdminService.toggleBan(user.email).subscribe(res => {
      user.security.ban = !user.security.ban;
      this.handleResponse.handleSuccess(res.message);
        this.loading = false;
    }, error => {
      this.handleResponse.handleError(error);
        this.loading = false;
    }
    );
  }
  toggleEnabled(user: UserResponse) {
    this.loading = true;
    this.superAdminService.toggleEnable(user.email).subscribe(res => {
      user.security.enabled = !user.security.enabled;
      this.handleResponse.handleSuccess(res.message);
    }, error => {
      this.handleResponse.handleError(error);
      this.loading = false;
    }
    );
  }

  removeRole(row: any, role: any) {

  }

  openRoleList(row: any) {

  }
}
