import { UserResponse } from "../user/UserResponse";

export interface ChatGroup {
    id: string;   // The username or identifier of the message sender
    name: string;
    creator:UserResponse;  // The content of the message
    members: string[];  // When the message was sent
}
  