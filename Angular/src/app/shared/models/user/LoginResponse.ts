import {StatusMessageResponse} from './StatusMessageResponse';
import {UserResponse} from './UserResponse';

export interface LoginResponse extends StatusMessageResponse {
    twoFactorAuth: boolean;
    user?: UserResponse;
}
