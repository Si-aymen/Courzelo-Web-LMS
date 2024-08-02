import { ChatCollection } from "src/app/views/chat/chat.service";
import { UserResponse } from "../../user/UserResponse";
import { UserProfileResponse } from "../../user/UserProfileResponse";
import { Project } from "../Project";


export interface UserChatInfo {
    chatId: ChatCollection['id'];
    contactId: UserResponse['id'];
    contactName: UserProfileResponse['name'];
    unread: number;
    lastChatTime: Date | string;
   
  }