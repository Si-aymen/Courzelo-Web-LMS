export interface MessageGroupREQ {
    id: string;   // The username or identifier of the message sender
    groupId: string;  // The content of the message
    senderId: string;  // When the message was sent
    text:string;
    createdDate:Date;
}
  