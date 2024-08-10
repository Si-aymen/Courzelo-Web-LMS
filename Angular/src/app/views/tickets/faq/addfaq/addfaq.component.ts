import { Component, OnInit } from '@angular/core';
import { FAQService } from '../../Services/FaqService/faq.service';
import { FormGroup, FormBuilder, Form, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { Router } from '@angular/router';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-addfaq',
  templateUrl: './addfaq.component.html',
  styleUrls: ['./addfaq.component.scss']
})
export class AddfaqComponent implements OnInit{
middle() {
  this.router.navigate(['/tickets/faq']); // Navigate to forward component
}
  data: FormGroup = new FormGroup({});

  constructor(private faqService: FAQService,private formBuilder:FormBuilder,
    public dialogRef: MatDialogRef<AddfaqComponent>,
    private router:Router) {}
  ngOnInit(): void {
    this.createForm();
  }
  onClose() {
    this.dialogRef.close();
  }

  createForm() {
    this.data = this.formBuilder.group({
      question: ['', Validators.required],
      answer: ['', Validators.required],
    });
  }

  add() {
    this.faqService.addFAQ(this.data.value).subscribe(
      (response) => {
        if (response) {
          Swal.fire({
            icon: 'success',
            title: 'Success...',
            text: 'Transférer avec succès!',
          })
        }
        else {
          Swal.fire({
            icon: 'success',
            title: 'Success...',
            text: 'Transférer avec succès!',
          })
        }
        console.log(this.data.value)
        console.log('FAQ added successfully', response);
        this.onClose() ;// Navigate to forward component
        // Optionally clear form fields or handle success feedback
      },
      (error) => {
        console.error('Error adding FAQ', error);
        // Handle error feedback
      }
    );
  }
}
