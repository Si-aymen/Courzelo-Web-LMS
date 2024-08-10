import { UserResponse } from "../user/UserResponse";

export interface SubForum {
    id : string,	
    name:string,	
    description:string,	
    createdDate:Date,
    user:UserResponse
  }