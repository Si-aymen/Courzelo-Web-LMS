

export interface PostREQ {
    id: string;                    // ID of the post
    postName: string;                  // Name of the post
    content: string;                   // Content of the post
    description: string;               // Description of the post
    user: string;                        // User who created the post
    createdDate: Date;               // Created date in ISO 8601 string format (adjust as needed)
    subforum: string;                // SubForum associated with the post             // List of user IDs who downvoted
  }