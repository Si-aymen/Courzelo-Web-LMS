import { Injectable } from '@angular/core';
import { ChatCollection } from '../../models/Project/Chat/ChatCollection';
import { Chat } from '../../models/Project/Chat/Chat';
import { BehaviorSubject, combineLatest, Observable, of, Subject } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { UserResponse } from '../../models/user/UserResponse';
import { Project } from '../../models/Project/Project';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  public contacts: UserResponse[];
  public chats: ChatCollection[];
  public user: UserResponse;

  onContactSelected = new BehaviorSubject<any>(null);
  onUserUpdated = new Subject<UserResponse>();
  project = new Subject<Project>();
  onChatSelected = new BehaviorSubject<any>(null);
  onChatsUpdated = new Subject<any>();
  loadingCollection: boolean;

  constructor(private http: HttpClient) {

  }

  loadChatData(): Observable<any> {
    return combineLatest(
      this.getAllContacts(),
      this.getAllChats(),
      this.getCurrentUser(),
      (contacts, chats, user) => {
        this.contacts = contacts;
        this.chats = chats;
        this.user = user;
        // console.log('next.willCall')
        this.onUserUpdated.next(user);
        // console.log('next.called')
        // console.log(
        //   "contacts:",
        //   contacts,
        //   "\n chats:",
        //   chats,
        //   "\n currUser:",
        //   user
        // );
      }
    );
  }
  public getChatByContact(contactId): Observable<ChatCollection> {
    this.loadingCollection = true;
    const chatInfo = this.user.chatInfo.find(chat => chat.contactId === contactId);

    if (!chatInfo) {
      return this.createChatCollection(contactId)
        .pipe(switchMap(chatColl => {
          return this.getChatByContact(contactId);
        }));
    }

    return this.getAllChats()
      .pipe(switchMap(chats => {
        const chatCollection = chats.find(chat => chat.id === chatInfo.chatId);
        const contact = this.contacts.find(
          contact => contact.id === contactId
        );

        this.onChatSelected.next({
          chatCollection: chatCollection,
          contact: contact
        });
        this.loadingCollection = false;
        return of(chatCollection);
      }));
  }

  createChatCollection(contactId) {

    const contact = this.contacts.find(contact => contact.id === contactId);
    const chatId = (Math.random() * 1000000000).toString();

    const chatCollection: ChatCollection = {
      id: chatId,
      chats: []
    };

    const chatInfo = {
      chatId: chatId,
      lastChatTime: new Date(),
      contactId: contact.id,
      contactName: contact.profile.name,
      unread: null 
    
    };

    return this.http
      .post('api/chat-collections', {...chatCollection})
      .pipe(switchMap(updatedChatCollection => {

        this.user.chatInfo.push(chatInfo);
        return this.updateUser(this.user)
          .pipe(switchMap((res) => {

            return this.getCurrentUser()
              .pipe(map(user => {
                this.user = user;
                // console.log(user)
                this.onUserUpdated.next(user);
              }));

            // return updatedChatCollection
          }));

      }));
  }

  getAllContacts(): Observable<UserResponse[]> {
    return this.http.get<UserResponse[]>('api/contacts');
  }
  getAllChats(): Observable<ChatCollection[]> {
    return this.http.get<ChatCollection[]>('api/chat-collections');
  }
  getCurrentUser(): Observable<UserResponse> {
    return this.http.get<UserResponse>('api/chat-user')
      .pipe(map(res => res[0]));
  }
  updateUser(user: UserResponse): Observable<UserResponse> {
    return this.http.put<UserResponse>(`api/chat-user/${user.id}`, {...user});
  }
  updateChats(chatId: string, chats: Chat[]): Observable<ChatCollection> {
    const chatCollection: ChatCollection = {
      id: chatId,
      chats: chats
    };
    return this.http.put<ChatCollection>('api/chat-collections', chatCollection);
  }

  autoReply(chat) {
    setTimeout(() => {
      this.onChatsUpdated.next(chat);
    }, 1500);
  }

}

