import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ChatGroup } from '../../models/ChatGroups/chatgroup';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private apiUrl = `/tk/api`;

  constructor(private http: HttpClient) { }

  getGroups(): Observable<any> {
    return this.http.get<ChatGroup[]>(`${this.apiUrl}/groups/all`);
  }
  getMembers(id:any): Observable<any> {
    return this.http.get<String[]>(`${this.apiUrl}/groups/get/${id}`);
  }

  getGroupById(id:any): Observable<any> {
    return this.http.get<ChatGroup>(`${this.apiUrl}/groups/get/${id}`);
  }
  getMessages(groupId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/messages/group/${groupId}`);
  }

  sendMessage(groupId: string, message: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/messages`, { ...message, groupId });
  }

  sendMessage1(message: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/messages/send`,message);
  }
  addgroup(mail: any): Observable<ChatGroup> {
    return this.http.post<ChatGroup>(`${this.apiUrl}/groups`, mail);
  }
  addmember(mail: any): Observable<any> {
    return this.http.put<ChatGroup>(`${this.apiUrl}/groups/addmember`, mail);
  }
  addmember1(mail: any): Observable<any> {
    return this.http.put<ChatGroup>(`${this.apiUrl}/groups/addmember1`, mail);
  }
}