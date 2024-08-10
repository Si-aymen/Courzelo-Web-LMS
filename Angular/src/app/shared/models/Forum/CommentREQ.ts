export interface CommentREQ {
    id: string;                    // ID of the post
    text: string;                  // Name of the post             // Description of the post
    post: string;    // User who created the post
    createdDate: Date;               // Created date in ISO 8601 string format (adjust as needed)
    user: string;                 // SubForum associated with the post             // List of user IDs who downvoted
  }