import {UserProfileResponse} from './UserProfileResponse';
import {UserSecurityResponse} from './UserSecurityResponse';
import {UserEducationResponse} from './UserEducationResponse';

export interface UserResponse {
    email?: string;
    roles?: string[];
    profile?: UserProfileResponse;
    security?: UserSecurityResponse;
    education?: UserEducationResponse;
}
