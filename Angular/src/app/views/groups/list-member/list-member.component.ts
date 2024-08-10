import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SharedChatserviceService } from 'src/app/shared/services/chatgroups/Sharedchatservice.service';
import { ChatService } from 'src/app/shared/services/chatgroups/chat.service';
import { AddMemberComponent } from '../add-member/add-member.component';

@Component({
  selector: 'app-list-member',
  templateUrl: './list-member.component.html',
  styleUrls: ['./list-member.component.scss']
})
export class ListMemberComponent implements OnInit {
  groups: any[] = []; // Declare `groups` as an array
  members: any[] = []; // Declare `members` as an array
  selectedGroupId: string | null = null;
  activeGroup: any;

  constructor(
    private sharedService: SharedChatserviceService,
    private chatservice: ChatService,
    public dialog: MatDialog

  ) { }

  ngOnInit(): void {
    this.LoadMembers();
  }

  LoadMembers(): void {
    this.sharedService.currentGroups.subscribe(groups => {
      console.log('Type of groups:', typeof(groups));
      this.groups = groups;
      console.log('Updated groups:', this.groups);

      if (this.groups && this.groups.length > 0) {
        this.activeGroup = this.groups[0];
        console.log('Active Group:', this.activeGroup);

        this.selectedGroupId = this.groups[0].id; // Set the first group's ID as selected
        console.log('Selected Group ID:', this.selectedGroupId);

        // Load members of the selected group
        this.loadGroupMembers(this.selectedGroupId);
      }
    });
  }

  loadGroupMembers(groupId: string): void {
    if (groupId) {
      this.chatservice.getGroupById(groupId).subscribe(
        (res) => {
          // Ensure res.members is an array
          this.members = Array.isArray(res.members) ? res.members : [];
          console.log('Loaded Members:', this.members);
        },
        error => {
          console.error('Error loading group members:', error);
          this.members = []; // Handle error by setting members to an empty array
        }
      );
    }
  }
  

  onGroupClick(groupId: string): void {
    this.selectedGroupId = groupId;
    console.log('Group clicked, ID:', this.selectedGroupId);

    // Load the members of the clicked group
    this.loadGroupMembers(groupId);
  }

  add(){
    const id =this.selectedGroupId;
    console.log("id",id);
    const dialogRef = this.dialog.open(AddMemberComponent,{
      width : "50%",
      height: "300px",
      data: { member:id}
    });
    dialogRef.afterClosed().subscribe(res =>{
     this.ngOnInit();
    })   
  }
}