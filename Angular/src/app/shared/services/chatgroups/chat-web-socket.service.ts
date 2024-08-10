import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatWebSocketService {
  private apiUrl = `/tk/api`;

  private socket: WebSocket;
  private messagesSubject: Subject<string> = new Subject<string>();

  constructor(private http: HttpClient) {
    this.socket = new WebSocket('ws://localhost:8080/ws/chat');

    this.socket.onmessage = (event) => {
      this.messagesSubject.next(event.data);
    };

    this.socket.onopen = () => {
      console.log('WebSocket connection opened');
    };

    this.socket.onclose = () => {
      console.log('WebSocket connection closed');
    };
  }

  sendMessage(message: any): void {
    if (this.socket.readyState === WebSocket.OPEN) {
      console.log('Sending message to WebSocket:', message); // Log message being sent
      this.socket.send(JSON.stringify(message));
    } else {
      console.error('WebSocket is not open. Unable to send message.');
    }
  }

  getMessages(): Observable<string> {
    return this.messagesSubject.asObservable();
  }

  getChatHistory(groupId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/messages/group/${groupId}`);
  }
}
