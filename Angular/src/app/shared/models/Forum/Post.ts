import { UserResponse } from "../user/UserResponse";
import { SubForum } from "./SubForum";

export interface Post {
    id: string;                    // ID of the post
    postName: string;                  // Name of the post
    content: string;                   // Content of the post
    description: string;               // Description of the post
    user: UserResponse;                        // User who created the post
    createdDate: Date;               // Created date in ISO 8601 string format (adjust as needed)
    subforum: SubForum;                // SubForum associated with the post
    upvotedBy: string[];               // List of user IDs who upvoted
    downvotedBy: string[];             // List of user IDs who downvoted
  }