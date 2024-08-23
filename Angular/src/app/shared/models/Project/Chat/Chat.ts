import { User } from "src/app/views/chat/chat.service";
import { Project } from "../Project";
import { UserResponse } from "../../user/UserResponse";

export interface Chat {
  text: string;
  time: Date | string;
  contactId: UserResponse['id'];
  project: Project;
  }