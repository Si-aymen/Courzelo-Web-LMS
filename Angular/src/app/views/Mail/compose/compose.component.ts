import { Component, Inject, Input, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { SessionStorageService } from 'src/app/shared/services/user/session-storage.service';
import Swal from 'sweetalert2';
import { MailexchangeService } from '../../tickets/Services/Mailing/mailexchange.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-compose',
  templateUrl: './compose.component.html',
  styleUrls: ['./compose.component.scss']
})
export class ComposeComponent implements OnInit {
  connectedUser: UserResponse;
  action: string;
  @Input() email: string;
    mailForm: FormGroup = new FormGroup({});
  
  constructor(
    private mailService: MailexchangeService,
    private sessionStorageService: SessionStorageService,
    private formBuilder: FormBuilder,
    public activeModal: NgbActiveModal,
    private router: Router,
  ) {}

  ngOnInit() {
    this.createForm();
    console.log(this.email);
    const navigation = this.router.getCurrentNavigation();
    this.mailForm.patchValue({recipient : this.email})
  }

  createForm() {
    this.mailForm = this.formBuilder.group({
      subject: ['', Validators.required],
      details: ['', Validators.required],
      recipient: ['', Validators.required],
      sender: [''],
    });
  }

  onSubmit() {
    this.connectedUser = this.sessionStorageService.getUser();
    this.mailForm.patchValue({ sender: this.connectedUser.email });

    console.log('Form Data Before Submit:', this.mailForm.value);

    this.mailService.sendMail(this.mailForm.value).subscribe({
      next: (res: any) => {
        console.log('Mail sent successfully:', res);
        Swal.fire({
          icon: 'success',
          title: 'Success!',
          text: 'Mail sent successfully!',
          confirmButtonText: 'OK'
        }).then(() => {
          this.mailForm.reset();
          this.activeModal.close();
        });
      },
      error: (err) => {
        console.error('Error sending mail:', err);
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'Failed to send mail. Please check the console for more details.',
          confirmButtonText: 'OK'
        });
      }
    });
  }
}
