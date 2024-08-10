import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ChatService } from 'src/app/shared/services/chatgroups/chat.service';

@Component({
  selector: 'app-add-group',
  templateUrl: './add-group.component.html',
  styleUrls: ['./add-group.component.scss']
})
export class AddGroupComponent implements OnInit{
middle() {
throw new Error('Method not implemented.');
}
  groupForm: FormGroup = new FormGroup({});

constructor(private chatservice:ChatService,
  private formBuilder:FormBuilder,
  ){
}

ngOnInit(): void {
  this.createForm();
  }

  createForm() {
    this.groupForm = this.formBuilder.group({
      name: ['', Validators.required],
    });
  }
  onSubmit() {
    this.chatservice.addgroup(this.groupForm.value).subscribe((res:any)=>{
      console.log("done",res)
    })
  }


}
