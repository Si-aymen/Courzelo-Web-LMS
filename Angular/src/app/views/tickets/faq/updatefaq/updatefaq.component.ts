import { FAQService } from './../../Services/FaqService/faq.service';
import { Component, Inject, OnInit, Optional } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-updatefaq',
  templateUrl: './updatefaq.component.html',
  styleUrls: ['./updatefaq.component.scss']
})
export class UpdatefaqComponent implements OnInit{
  data: FormGroup = new FormGroup({});
  id: any;
  onClose() {
    this.dialogRef.close();
  }
  constructor(public dialogRef: MatDialogRef<UpdatefaqComponent>,
    private router : Router,
    private formBuilder: FormBuilder,
    private faqservice: FAQService,
    @Optional() @Inject(MAT_DIALOG_DATA) public faq: any
    ){this.id=faq.faq}
  ngOnInit(): void {
    this.createForm();
    this.faqservice.getFAQById(this.id).subscribe((res:any)=>{
      console.log(res);
      this.data.patchValue({
        id: res.id,
        question: res.question,
        answer: res.answer,
      });
    })
  }

  createForm() {
    this.data = this.formBuilder.group({
      question: ['', Validators.required],
      answer: ['', Validators.required],
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
        this.faqservice.updateFAQ(this.id,this.data.value).subscribe((res: any) => {
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

