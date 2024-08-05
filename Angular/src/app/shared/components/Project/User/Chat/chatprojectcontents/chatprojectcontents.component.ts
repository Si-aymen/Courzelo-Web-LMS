import { Component, Input, OnDestroy, OnInit, ViewChild, ViewChildren } from '@angular/core';
import { NgForm } from '@angular/forms';
import { PerfectScrollbarDirective } from 'ngx-perfect-scrollbar';
import { Subscription } from 'rxjs';
import { Chat } from 'src/app/shared/models/Project/Chat/Chat';
import { ChatCollection } from 'src/app/shared/models/Project/Chat/ChatCollection';
import { Project } from 'src/app/shared/models/Project/Project';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { ChatService } from 'src/app/shared/services/Project/chat-project.service';

@Component({
  selector: 'app-chatprojectcontents',
  templateUrl: './chatprojectcontents.component.html',
  styleUrls: ['./chatprojectcontents.component.scss']
})
export class ChatprojectcontentsComponent implements OnInit, OnDestroy
 {
  user: UserResponse ;
  activeContact: UserResponse;
  public chatCollection: ChatCollection;
  currentProject:Project;
  userUpdateSub: Subscription;
  chatUpdateSub: Subscription;
  chatSelectSub: Subscription;

  @Input('matSidenav') matSidenav;
  @ViewChild(PerfectScrollbarDirective) psContainer: PerfectScrollbarDirective;

  @ViewChildren('msgInput') msgInput;
  @ViewChild('msgForm') msgForm: NgForm;

  constructor(public chatService: ChatService) {}

  ngOnInit() {
    // Listen for user update
    this.userUpdateSub = this.chatService.onUserUpdated.subscribe(user => {
      console.log(user);
      this.user = user;
    });

    // Listen for contact change
    this.chatSelectSub = this.chatService.onChatSelected.subscribe(res => {
      if (res) {
        this.chatCollection = res.chatCollection;
        this.activeContact = res.contact;
        this.initMsgForm();
      }
    });

    // Listen for chat update
    this.chatUpdateSub = this.chatService.onChatsUpdated.subscribe(chat => {
      this.chatCollection.chats.push(chat);
      this.scrollToBottom();
    });
  }

  ngOnDestroy() {
    if ( this.userUpdateSub ) { this.userUpdateSub.unsubscribe(); }
    if ( this.chatSelectSub ) { this.chatSelectSub.unsubscribe(); }
    if ( this.chatUpdateSub ) { this.chatUpdateSub.unsubscribe(); }
  }

  sendMessage(e) {
    const chat: Chat = {
      contactId: this.chatService.user.id,
      text: this.msgForm.form.value.message,
      time: new Date().toISOString(),
      project: this.currentProject ,
    };

    this.chatCollection.chats.push(chat);
    this.chatService
      .updateChats(this.chatCollection.id, [...this.chatCollection.chats])
      .subscribe(res => {
        this.initMsgForm();
      });

    // Only for demo purpose
    this.chatService.autoReply({
      contactId: this.activeContact.id,
      text: `Hi, I\'m ${this.activeContact.profile.name}. Your imaginary friend.`,
      time: new Date().toISOString()
    });

  }

  initMsgForm() {
    setTimeout(() => {
      this.msgForm.reset();
      this.msgInput.first.nativeElement.focus();
      this.scrollToBottom();
    });
  }

  scrollToBottom() {
    setTimeout(() => {
      this.psContainer.update();
      this.psContainer.scrollToBottom(0, 400);
    });
  }
}
