import {InstitutionUserResponse} from './InstitutionUserResponse';

export interface PaginatedInstitutionUsersResponse {
    users: InstitutionUserResponse[];
    currentPage: number;
    totalPages: number;
    totalItems: number;
    itemsPerPage: number;
}
