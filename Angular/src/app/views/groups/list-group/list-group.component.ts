import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { SharedAnimations } from 'src/app/shared/animations/shared-animations';
import { ChatService } from 'src/app/shared/services/chatgroups/chat.service';
import { SessionStorageService } from 'src/app/shared/services/user/session-storage.service';
import { UserService } from 'src/app/shared/services/user/user.service';
import { AddMemberComponent } from '../add-member/add-member.component';
import { ChatGroup } from 'src/app/shared/models/ChatGroups/chatgroup';
import { SharedChatserviceService } from 'src/app/shared/services/chatgroups/Sharedchatservice.service';

@Component({
  selector: 'app-list-group',
  templateUrl: './list-group.component.html',
  styleUrls: ['./list-group.component.scss'],
  animations: [SharedAnimations]
,  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ListGroupComponent implements OnInit{
  groups: any[] = [] ; 
  newGroupsList: any[] = [];  // Ensure this is initialized
  currentMessageList$: Observable<any[]>;
  imageSrc: any;
  private groupsSubject = new BehaviorSubject<any[]>([]);
  groups$: Observable<any[]> = this.groupsSubject.asObservable();
  imageCache: { [email: string]: SafeUrl } = {};
  constructor(private chatService: ChatService,
    private sharedService: SharedChatserviceService,
    private sanitizer: DomSanitizer,
    private sessionStorageService: SessionStorageService,
    private userService: UserService,
    public dialog: MatDialog,
    private cdr: ChangeDetectorRef ) { }

  ngOnInit(): void {
    /*this.userService.getProfileImageBlobUrl(this..email).subscribe((blob: Blob) => {
      const objectURL = URL.createObjectURL(blob);
      this.imageSrc = this.sanitizer.bypassSecurityTrustUrl(objectURL);
    });  */
    this.LoadGroups();
    console.log("Groups in newGroupsList:", this.newGroupsList);

  }

  loadProfileImage(email: string): Observable<SafeUrl> {
    if (this.imageCache[email]) {
      return of(this.imageCache[email]); // Return cached image
    }
  }

  LoadGroups(): void {
    this.chatService.getGroups().subscribe(
      data => {
        console.log("Fetched data:", data);
        if (Array.isArray(data)) {
          this.newGroupsList = data;
          this.cdr.detectChanges(); // Force change detection
          console.log("Assigned newGroupsList:", this.newGroupsList);
        } else {
          console.error("Fetched data is not an array", data);
        }
      },
      error => {
        console.error("Error fetching groups", error);
      }
    );
  }
  
  
  add(id:any){
    const dialogRef = this.dialog.open(AddMemberComponent,{
      width : "40%",
      height: "10%",
      data: { member:id}
    });
    dialogRef.afterClosed().subscribe(res =>{
     this.ngOnInit();
    })   
  }

  onGroupClick(groupId: string): void {
    // Find the selected group based on the clicked ID
    const selectedGroup = this.newGroupsList.find(group => group.id === groupId);
console.log("selectedGroup",selectedGroup)
    if (selectedGroup) {
      // Update the shared service with the selected group
      this.sharedService.updateGroups([selectedGroup]); // You can pass an array or any required data
    }
  }
  
  select(group: any) {
    console.log('Selected group:', group);
  }

/*
  loadToMe(): void {
    this.groups$ = this.chatService.getGroups().pipe(
      tap(res => {
        res.forEach(res => {
          this.loadProfileImage().subscribe(image => {
            mail.sender.profile.profileImage = image;
            console.log(image)
            this.cdr.markForCheck(); // Notify Angular about the change
          });
          this.loadProfileImage(mail.recipient.email).subscribe(image => {
            mail.recipient.profile.profileImage = image;
            console.log(image)
            this.cdr.markForCheck(); // Notify Angular about the change
          });
        });
      })
    );
  }*/
}
