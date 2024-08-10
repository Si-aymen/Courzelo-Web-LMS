import { UserResponse } from "../user/UserResponse";

export interface MessageGroup {
    id: string;   // The username or identifier of the message sender
    groupId: string;  // The content of the message
    senderId: UserResponse;  // When the message was sent
    text:string;
    createdDate:Date;
}
  