import { UserResponse } from "../user/UserResponse";
import { Post } from "./Post";


export interface Comment {
    id: string;                    // ID of the post
    text: string;                  // Name of the post             // Description of the post
    post: Post;    // User who created the post
    createdDate: Date;               // Created date in ISO 8601 string format (adjust as needed)
    user: UserResponse;                 // SubForum associated with the post
    upvotedBy: string[];               // List of user IDs who upvoted
    downvotedBy: string[];             // List of user IDs who downvoted
  }