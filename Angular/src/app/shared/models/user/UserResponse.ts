import { UserChatInfo } from '../Project/Chat/UserChatInfo';
import {UserProfileResponse} from './UserProfileResponse';
import {UserSecurityResponse} from './UserSecurityResponse';

export interface UserResponse {
    id?: string;
    email?: string;
    roles?: string[];
    profile?: UserProfileResponse;
    security?: UserSecurityResponse;
    chatInfo?: UserChatInfo[];
   
}
