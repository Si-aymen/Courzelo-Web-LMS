import { Component, Inject, OnInit, Optional } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ChatService } from 'src/app/shared/services/chatgroups/chat.service';

@Component({
  selector: 'app-add-member',
  templateUrl: './add-member.component.html',
  styleUrls: ['./add-member.component.scss']
})
export class AddMemberComponent implements OnInit{
  id:any;
  memberForm:FormGroup= new FormGroup({});

  constructor(public dialogRef: MatDialogRef<AddMemberComponent>,private router : Router,
    private formBuilder: FormBuilder,private chatservice:ChatService, 
    @Optional() @Inject(MAT_DIALOG_DATA) public member: any
    ){this.id=member.member}
  ngOnInit(): void {
    console.log(this.id);
    this.createForm();
    this.memberForm.patchValue({
      id:this.id,
    })
    
  }

  createForm() {
    this.memberForm = this.formBuilder.group({
      id: [''],
      members: ['',Validators.required],
    });
  }

  onSubmit() {
    console.log("form",this.memberForm.value);
    this.chatservice.addmember1(this.memberForm.value).subscribe((res)=>{
      console.log(res);
      this.onClose();
    })
    }

    onClose() {
      this.dialogRef.close();
    }
}
