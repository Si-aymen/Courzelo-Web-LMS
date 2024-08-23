import { UserChatInfo } from '../Project/Chat/UserChatInfo';
import {UserProfileResponse} from './UserProfileResponse';
import {UserSecurityResponse} from './UserSecurityResponse';
import {UserEducationResponse} from './UserEducationResponse';

export interface UserResponse {
    id?: string;
    email?: string;
    roles?: string[];
    profile?: UserProfileResponse;
    security?: UserSecurityResponse;
    education?: UserEducationResponse;
    chatInfo?: UserChatInfo[];
   
}
