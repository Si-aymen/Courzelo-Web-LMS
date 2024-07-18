import {UserResponse} from './UserResponse';

export interface PaginatedUsersResponse {
    users: UserResponse[];
    currentPage: number;
    totalPages: number;
    totalItems: number;
    itemsPerPage: number;
}
