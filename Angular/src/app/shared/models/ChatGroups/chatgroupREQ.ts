export interface ChatGroupREQ {
    id: string;   // The username or identifier of the message sender
    name: string;
    creator:string;  // The content of the message
    members: string;  // When the message was sent
}