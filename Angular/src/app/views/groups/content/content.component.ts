import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { el } from 'date-fns/locale';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { SharedChatserviceService } from 'src/app/shared/services/chatgroups/Sharedchatservice.service';
import { ChatWebSocketService } from 'src/app/shared/services/chatgroups/chat-web-socket.service';
import { ChatService } from 'src/app/shared/services/chatgroups/chat.service';
import { SessionStorageService } from 'src/app/shared/services/user/session-storage.service';
import { UserService } from 'src/app/shared/services/user/user.service';

@Component({
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.scss']
})
export class ContentComponent implements OnInit{

  groups: any;
  message: string = '';
  selectedGroupId: string | null = null; // To store the selected group's ID
  messages: any[] = [];  // Initialize as an empty array
  messageForm: FormGroup = new FormGroup({});
  activeGroup: any;
  connectedUser: UserResponse;
  imageSrc: any;
  imageCache: { [email: string]: SafeUrl } = {};

  constructor(
    private chatservice:ChatService,
    private sharedService:SharedChatserviceService,
    private sessionStorageService: SessionStorageService,
    private formBuilder:FormBuilder,
    private userService: UserService,
    private sanitizer: DomSanitizer,
    private cdr: ChangeDetectorRef, // Add this line
    private websocketchat:ChatWebSocketService
  ){}

  ngOnInit(): void {
    this.connectedUser = this.sessionStorageService.getUserFromSession();
    this.websocketchat.getMessages().subscribe(message => {
      console.log("Received message:", message);
      console.log(typeof(message)) // Log the received message
      this.messages.push(message);
      this.LoadGroup();
    });
    console.log(this.connectedUser)
    this.createForm();
    this.LoadGroup();
    /*
    this.websocketchat.getMessages().subscribe(message => {
      console.log("eeeeeeeeeeeeeeeeee",message)
      this.messages.push(message);
      console.log("Messages++++",this.messages)
    });*/
console.log("message list",this.messages)
  }

  loadProfileImage(email: string): Observable<SafeUrl> {
    if (this.imageCache[email]) {
      return of(this.imageCache[email]); // Return cached image if it exists
    }

    return this.userService.getProfileImageBlobUrl(email).pipe(
      map(blob => {
        const objectURL = URL.createObjectURL(blob);
        const safeURL = this.sanitizer.bypassSecurityTrustUrl(objectURL);
        this.imageCache[email] = safeURL;
        return safeURL;
      }),
      catchError(() => {
        const defaultImageURL = 'assets/default-profile-image.png'; // Default image path
        const safeDefaultURL = this.sanitizer.bypassSecurityTrustUrl(defaultImageURL);
        this.imageCache[email] = safeDefaultURL;
        return of(safeDefaultURL);
      })
    );
  }
  createForm() {
    this.messageForm = this.formBuilder.group({
      message: ['', Validators.required],
    });
  }
  LoadGroup(): void {
    this.sharedService.currentGroups.subscribe(groups => {
      console.log('Type of groups:', typeof(groups));
      this.groups = groups;
      console.log('Updated groups:', this.groups);
      if (this.groups.length > 0) {
        this.activeGroup=this.groups[0];
        console.log('Active Group',this.activeGroup)
        this.selectedGroupId = this.groups[0].id; // Adjust based on your use case
        console.log('Selected Group ID:', this.selectedGroupId);

        this.LoadMessage(this.selectedGroupId);
      }
    });
  }

 LoadMessage (groupId: string): void {
    console.log('Loading messages for group ID:', groupId); // Add log to verify method call
    this.chatservice.getMessages(groupId).subscribe(messages => {
      console.log('Loaded messages:', messages); // Log the fetched messages
      this.messages = messages;
   /*   messages.forEach(element => {console.log("email",element.sender.email)
        this.loadProfileImage(element.sender.email).subscribe(image=>{
          element.sender['profileImageUrl'] = image;
          console.log("image url",image)
          this.cdr.markForCheck(); // Ensure change detection runs
        })
        console.log(element.id)
      });
    });}
*/
  });}
      /*this.message.forEach(element => {
        
      });*/
/*, error => {
      console.error('Error loading messages:', error); // Log any errors
    });
  }/*
  LoadMessageSocket (groupId: string): void {
    console.log('Loading messages for group ID:', groupId); // Add log to verify method call
    this.websocketchat.getChatHistory(groupId).subscribe(history => {
      console.log('Loaded messages:', history); // Log the fetched messages
      this.messages = history.map(msg=>msg.text);
    }, error => {
      console.error('Error loading messages:', error); // Log any errors
    });
  }*/
  test(comment:any){
    this.loadProfileImage(comment.user.email).subscribe(image => {
      comment.user['profileImageUrl'] = image; // Dynamically add the property
      // Fetch downvote count
     // this.determineVoteCount(comment);
      this.cdr.markForCheck(); // Ensure change detection runs
    });
  }
  onSubmit(): void {
    const messageText = this.messageForm.get('message')?.value;
    if (messageText && this.activeGroup) {
      // Create message object
      const message = {
        text: messageText,
        groupId: this.activeGroup.id,
        senderId: this.connectedUser.email,
      };
      console.log("le message finale",message)
      this.chatservice.sendMessage1(message).subscribe((res)=>{
        console.log(res);
      })
      // Call service to send message
   /*  this.sharedService.sendMessage(message).subscribe(response => {
        console.log('Message sent:', response);
        this.messageForm.reset();  // Reset the form after submission
        this.loadMessages();  // Refresh messages
      });*/
    }
  }
  sendMessage(): void {
    const messageText = this.messageForm.get('message')?.value;
    if (messageText && this.activeGroup) {
      const messageObj = {
        text: messageText,
        groupId: this.activeGroup.id,
        senderId: this.connectedUser.email,
      };
      this.websocketchat.sendMessage(messageObj);
      this.message = '';
      this.messageForm.reset();
    }
  }
}