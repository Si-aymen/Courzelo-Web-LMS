import {InstitutionResponse} from './InstitutionResponse';

export interface PaginatedInstitutionsResponse {
    institutions: InstitutionResponse[];
    currentPage: number;
    totalPages: number;
    totalItems: number;
    itemsPerPage: number;
}
