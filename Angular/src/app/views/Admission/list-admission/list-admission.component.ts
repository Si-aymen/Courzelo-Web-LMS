import { Component, OnInit } from '@angular/core';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { AdmissionService } from 'src/app/shared/services/admission/admission.service';
import { SessionStorageService } from 'src/app/shared/services/user/session-storage.service';

@Component({
  selector: 'app-list-admission',
  templateUrl: './list-admission.component.html',
  styleUrls: ['./list-admission.component.scss']
})
export class ListAdmissionComponent implements OnInit{
Admissions:any[]=[];
connectedUser:UserResponse;
  constructor(private admissionService:AdmissionService,
    private sessionsStorage:SessionStorageService
    ){}
  ngOnInit(): void {
    this.connectedUser=this.sessionsStorage.getUserFromSession();
    this.admissionService.getAdmissions(this.connectedUser.email).subscribe((res)=>{
      console.log("le List",res)
      this.Admissions=res;
    })
  }

}
