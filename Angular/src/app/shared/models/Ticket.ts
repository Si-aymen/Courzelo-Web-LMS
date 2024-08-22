import { TicketType } from "./TicketType";
import { UserResponse } from "./user/UserResponse";

export interface Ticket {
  id : string,	
  sujet:string,	
  details:string,	
  dateCreation:Date,
  type:TicketType,	
  status:string,
  user:UserResponse
}