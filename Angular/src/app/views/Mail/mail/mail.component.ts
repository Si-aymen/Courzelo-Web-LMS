import { SessionStorageService } from 'src/app/shared/services/user/session-storage.service';
import { MailexchangeService } from '../../tickets/Services/Mailing/mailexchange.service';
import { MailExchange } from './../../../shared/models/Support/mailing';
import { Component, OnInit } from '@angular/core';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { ComposeComponent } from '../compose/compose.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SharedAnimations } from 'src/app/shared/animations/shared-animations';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-mail',
  templateUrl: './mail.component.html',
  styleUrls: ['./mail.component.scss'],
  animations: [SharedAnimations]

})
export class MailComponent implements OnInit {
  //mail: MailExchange = { subject: '', details: '', sender: null, recipient: null };
  mails$: Observable<MailExchange[]>;
  connectedUser: UserResponse;
  selected: any;

  mailForm: FormGroup = new FormGroup({});

  constructor(private mailService: MailexchangeService,
    private sessionStorageService: SessionStorageService,
    private formBuilder:FormBuilder,
    private modalService: NgbModal,
    private router: Router,

    ) { }

  ngOnInit() {
    this.createForm();
    this.loadMyMails();
  }
  select(mail) {
    this.selected = mail;
  }
  createForm() {
    this.mailForm = this.formBuilder.group({
      subject: ['', Validators.required],
      details: ['', Validators.required],
      recipient:['',Validators.required],
      sender:[''],
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
          this.loadMyMails();
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


 
  openComposeModal() {
    this.modalService.open(ComposeComponent, {size: 'lg', centered: true});

  }
  openComposeModal1(action: string, email?: any) {
    // Navigate to ComposeComponent and pass data
    this.router.navigate(['/mailing/compose'], { state: { action, email } });
  }
  deleteMail(mailId: string, event: MouseEvent) {
    event.stopPropagation(); // Prevent click event from bubbling up to parent elements

    Swal.fire({
      title: 'Are you sure?',
      text: 'Do you really want to delete this email?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel!'
    }).then((result) => {
      if (result.isConfirmed) {
        this.mailService.deleteMail(mailId).subscribe({
          next: () => {
            Swal.fire(
              'Deleted!',
              'The email has been deleted.',
              'success'
            );
            this.loadMyMails(); // Reload the mail list
          },
          error: (err) => {
            Swal.fire(
              'Error!',
              'There was an issue deleting the email.',
              'error'
            );
            console.error('Error deleting mail:', err);
          }
        });
      }
    });
  }

  loadMyMails() {
    this.mails$ = this.mailService.getAllMails();
    this.mails$.subscribe({
      next: (data) => console.log('Mails data:', data),  // Log actual data
      error: (err) => console.error('Error loading mails:', err)
    });
  }

  loadToMe() {
    this.mails$ = this.mailService.getAllMails();
    this.mails$.subscribe({
      next: (data) => console.log('Mails data:', data),  // Log actual data
      error: (err) => console.error('Error loading mails:', err)
    });
  }
}