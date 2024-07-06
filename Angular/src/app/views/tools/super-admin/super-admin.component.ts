import {Component, OnInit} from '@angular/core';
import {ProductService} from '../../../shared/services/product.service';
import {SuperAdminService} from '../../../shared/services/user/super-admin.service';
import {PaginatedUsersResponse} from '../../../shared/models/user/PaginatedUsersResponse';
import {UserResponse} from '../../../shared/models/user/UserResponse';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';

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
    this.superAdminService.getUsers(page - 1, size).subscribe((response: PaginatedUsersResponse) => {
      console.log(response);
      this.users = response.users;
      this._currentPage = response.currentPage + 1;
      this.totalPages = response.totalPages;
      this.totalItems = response.totalItems;
      this.itemsPerPage = response.itemsPerPage;
    }, error => this.handleResponse.handleError(error)
    );
  }
}
