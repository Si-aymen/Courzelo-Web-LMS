import { Component, Inject, OnInit, Optional } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { PostService } from 'src/app/shared/services/Forum/post.service';
import { TicketDataService } from 'src/app/views/tickets/Services/TicketService/ticketdata.service';
import { TickettypeService } from 'src/app/views/tickets/Services/TicketTypeService/tickettype.service';
import { UpdateTicketComponent } from 'src/app/views/ticketsStudent/update-ticket/update-ticket.component';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-update-post',
  templateUrl: './update-post.component.html',
  styleUrls: ['./update-post.component.scss']
})
export class UpdatePostComponent implements OnInit{
  id: any;
  postForm:FormGroup= new FormGroup({});
  onClose() {
    this.dialogRef.close();
  }
  constructor(public dialogRef: MatDialogRef<UpdateTicketComponent>,private router : Router,private typeticketervice:TickettypeService,private ticketDataService: TicketDataService,private typeservice:TickettypeService
    ,private postService:PostService,
    private formBuilder: FormBuilder,
    @Optional() @Inject(MAT_DIALOG_DATA) public post: any
    ){this.id=post.post}
    ngOnInit(): void {
      console.log(this.id);
      this.createForm();
      this.postService.getPostById(this.id).subscribe((res:any)=>{
        console.log(res);
        this.postForm.patchValue({
          postName: res.postName,
          content: res.content,
          description: res.description,
        });
      })
      }

    createForm() {
      this.postForm = this.formBuilder.group({
        postName: [''],
        content: [''],
        description:['']
      });
    }

    onSubmit() {
      Swal.fire({
        title: 'Are you sure?',
        text: 'Do you want to update the Post with the provided details?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, update it!',
        cancelButtonText: 'Cancel'
      }).then((result) => {
        if (result.isConfirmed) {
          // If confirmed, proceed with the update
          this.postService.updatePost(this.id, this.postForm.value).subscribe((res: any) => {
            if (res) {
              Swal.fire({
                icon: 'success',
                title: 'Success...',
                text: 'Post updated successfully!',
              });
              this.onClose(); // Close the dialog if needed
            }
          }, error => {
            Swal.fire({
              icon: 'error',
              title: 'Oops...',
              text: 'Something went wrong!',
            });
          });
        } else {
          // Optionally handle the case where the user cancels the update
          console.log('Post canceled');
        }
      });
    }
}
