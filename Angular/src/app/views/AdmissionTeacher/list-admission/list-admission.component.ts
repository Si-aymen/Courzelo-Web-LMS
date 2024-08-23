import { Component, OnInit } from '@angular/core';
import { Interview } from 'src/app/shared/models/admission/Interview';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { InterviewService } from 'src/app/shared/services/admission/interview.service';
import { SessionStorageService } from 'src/app/shared/services/user/session-storage.service';

@Component({
  selector: 'app-list-admission',
  templateUrl: './list-admission.component.html',
  styleUrls: ['./list-admission.component.scss']
})
export class ListAdmissionComponent implements OnInit{
connectedUser:UserResponse;
admissions:any[]=[];

constructor(private sessionsStorage:SessionStorageService,
  private interviewService:InterviewService){
    this.connectedUser=sessionsStorage.getUserFromSession();
  }

ngOnInit(): void {
  this.Load();
}

Load() {
  this.interviewService.getInterviewByUser(this.connectedUser.email).subscribe((interviews: Interview[]) => {
    interviews.forEach(interview => {
      this.admissions.push(interview.admission); // Add each admission to the array
    });
    console.log(this.admissions); // You can see all the collected admissions
  });
}
}
