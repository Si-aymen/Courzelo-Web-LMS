import {GroupResponse} from './GroupResponse';

export interface PaginatedGroupsResponse {
    groups: GroupResponse[];
    currentPage: number;
    totalPages: number;
    totalItems: number;
    itemsPerPage: number;
}
