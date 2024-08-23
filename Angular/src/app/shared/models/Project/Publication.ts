
import { UserResponse } from "../user/UserResponse";
import { Commment } from "./Commment";
import { Reaction } from "./Reaction";




export interface Publication {
    id: string;
    author: UserResponse;
    userReactions: { [key: string]: string };
    dateTime: Date;
    content: string;
    likes: number;
    dislikes: number;
    commentsCount: number;
    comments: Commment[];
    projectId: string; 
}