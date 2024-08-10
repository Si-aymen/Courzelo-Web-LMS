import { Component, Inject, OnInit, Optional } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { CommentService } from 'src/app/shared/services/Forum/comment.service';
import { UpdateTicketComponent } from 'src/app/views/ticketsStudent/update-ticket/update-ticket.component';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-edit-comment',
  templateUrl: './edit-comment.component.html',
  styleUrls: ['./edit-comment.component.scss']
})
export class EditCommentComponent implements OnInit{
  id:any;
  commentForm:FormGroup= new FormGroup({});

  onClose() {
    this.dialogRef.close();
  }
  constructor(public dialogRef: MatDialogRef<UpdateTicketComponent>,
    private router : Router,private commentService:CommentService,
    private formBuilder: FormBuilder,
    @Optional() @Inject(MAT_DIALOG_DATA) public comment: any
    ){this.id=comment.comment}
    
    ngOnInit(): void {
      console.log(this.id)
      this.createForm();
      this.commentService.getCommentByid(this.id).subscribe((res:any)=>{
        console.log(res);
        this.commentForm.patchValue({
          text: res.text,
        });
      })
    }

    createForm() {
      this.commentForm = this.formBuilder.group({
        text: [''],
      });
    }


    onSubmit() {
      Swal.fire({
        title: 'Are you sure?',
        text: 'Do you want to update the ticket with the provided details?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, update it!',
        cancelButtonText: 'Cancel'
      }).then((result) => {
        if (result.isConfirmed) {
          // If confirmed, proceed with the update
          this.commentService.updateComment(this.id, this.commentForm.value).subscribe((res: any) => {
            if (res) {
              Swal.fire({
                icon: 'success',
                title: 'Success...',
                text: 'Ticket updated successfully!',
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
          console.log('Update canceled');
        }
      });
    }
}
