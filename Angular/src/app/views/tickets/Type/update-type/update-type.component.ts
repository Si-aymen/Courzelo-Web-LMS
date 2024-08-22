import { TickettypeComponent } from './../tickettype/tickettype.component';
import { Component, Inject, OnInit, Optional } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { TickettypeService } from '../../Services/TicketTypeService/tickettype.service';

@Component({
  selector: 'app-update-type',
  templateUrl: './update-type.component.html',
  styleUrls: ['./update-type.component.scss']
})
export class UpdateTypeComponent implements OnInit{
  onClose() {
    this.dialogRef.close();
  }
  
    typeForm:FormGroup= new FormGroup({});
    id: any;
    sujet: string ="";
  
  
    constructor(public dialogRef: MatDialogRef<UpdateTypeComponent>,private router : Router,
      private typeticketervice:TickettypeService,
    private formBuilder: FormBuilder,
    @Optional() @Inject(MAT_DIALOG_DATA) public type: any
    ){this.id=type.type}
  
  
    ngOnInit(): void {
      console.log(this.id);
      this.createForm();
      //const id = this.route.snapshot.params.id;
      this.typeticketervice.getTypeId(this.id).subscribe((res:any)=>{
        console.log(res);
        this.typeForm.patchValue({
          id: res.id,
          type: res.type,
        });
      })
  
    }
    createForm() {
      this.typeForm = this.formBuilder.group({
        id:[''],
        type: ['',],
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
          this.typeticketervice.updateType(this.typeForm.value).subscribe((res: any) => {
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
  