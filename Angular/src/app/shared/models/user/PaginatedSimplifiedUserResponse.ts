import {SimplifiedUserResponse} from './SimplifiedUserResponse';

export interface PaginatedSimplifiedUserResponse {
    users: SimplifiedUserResponse[];
    currentPage: number;
    totalPages: number;
    totalItems: number;
    itemsPerPage: number;
}
