import { SessionStorageService } from 'src/app/shared/services/user/session-storage.service';
import { MailexchangeService } from '../../tickets/Services/Mailing/mailexchange.service';
import { MailExchange } from './../../../shared/models/Support/mailing';
import { ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { ComposeComponent } from '../compose/compose.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SharedAnimations } from 'src/app/shared/animations/shared-animations';
import { Observable, of } from 'rxjs';
import { Router } from '@angular/router';
import { catchError, map, tap } from 'rxjs/operators';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { UserService } from 'src/app/shared/services/user/user.service';

@Component({
  selector: 'app-mail',
  templateUrl: './mail.component.html',
  styleUrls: ['./mail.component.scss'],
  animations: [SharedAnimations]
,  changeDetection: ChangeDetectionStrategy.OnPush
})
export class MailComponent implements OnInit {
  //mail: MailExchange = { subject: '', details: '', sender: null, recipient: null };
  mails$: Observable<MailExchange[]>;
  connectedUser: UserResponse;
  selected: any;
  mailsReceived$: Observable<any[]>;
  mailsSent$: Observable<any[]>;
  currentMailList$: Observable<any[]>;
  isSentView: boolean = false; // To track if the "Sent" view is active
  imageSrc: any;


  mailForm: FormGroup = new FormGroup({});
  imageCache: { [email: string]: SafeUrl } = {};

  constructor(private mailService: MailexchangeService,
    private sessionStorageService: SessionStorageService,
    private formBuilder:FormBuilder,
    private modalService: NgbModal,
    private router: Router,
    private userService: UserService,
    private sanitizer: DomSanitizer,
    private cdr: ChangeDetectorRef // Add this line
    ) { }

  ngOnInit() {
    this.createForm();
    this.connectedUser = this.sessionStorageService.getUserFromSession();
    this.loadToMe();
    this.userService.getProfileImageBlobUrl(this.connectedUser.email).subscribe((blob: Blob) => {
      const objectURL = URL.createObjectURL(blob);
      this.imageSrc = this.sanitizer.bypassSecurityTrustUrl(objectURL);
    });    
    console.log(this.currentMailList$)
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
          this.loadToMe();
          this.ngOnInit();
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
  openComposeModal2(email: string) {
    const modalRef = this.modalService.open(ComposeComponent, { size: 'lg', centered: true });
    modalRef.componentInstance.email = email;  // Pass email to the ComposeComponent
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
            this.loadToMe(); // Reload the mail list
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

  loadToMe(): void {
    this.mailsReceived$ = this.mailService.getMailsByRecipient(this.connectedUser.email).pipe(
      tap(mails => {
        mails.forEach(mail => {
          this.loadProfileImage(mail.sender.email).subscribe(image => {
            mail.sender.profile.profileImage = image;
            console.log(image)
            this.cdr.markForCheck(); // Notify Angular about the change
          });
          this.loadProfileImage(mail.recipient.email).subscribe(image => {
            mail.recipient.profile.profileImage = image;
            console.log(image)
            this.cdr.markForCheck(); // Notify Angular about the change
          });
        });
      })
    );
    this.currentMailList$ = this.mailsReceived$;
    this.isSentView = false;
  }
  
  loadProfileImage(email: string): Observable<SafeUrl> {
    if (this.imageCache[email]) {
      return of(this.imageCache[email]); // Return cached image
    }

    return this.userService.getProfileImageBlobUrl(email).pipe(
      map(blob => {
        const objectURL = URL.createObjectURL(blob);
        const safeURL = this.sanitizer.bypassSecurityTrustUrl(objectURL);
        this.imageCache[email] = safeURL;
        this.cdr.detectChanges(); // Force Angular to re-run change detection
        return safeURL;
      }),
      catchError(() => {
        const defaultImageURL = 'assets/default-profile-image.png'; // Provide a default image
        const safeDefaultURL = this.sanitizer.bypassSecurityTrustUrl(defaultImageURL);
        this.imageCache[email] = safeDefaultURL;
        this.cdr.detectChanges(); // Force Angular to re-run change detection
        return of(safeDefaultURL);
      })
    );
  }

  loadByMe(): void {
    this.mailsSent$ = this.mailService.getMailsBySender(this.connectedUser.email).pipe(
        tap(mails => {
            mails.forEach(mail => {
                this.loadProfileImage(mail.sender.email).subscribe(image => {
                    mail.sender.profile.profileImage = image;
                    console.log(`Sender Image: ${image}`);
                    this.cdr.markForCheck(); // Notify Angular about the change
                });
                this.loadProfileImage(mail.recipient.email).subscribe(image => {
                    mail.recipient.profile.profileImage = image;
                    console.log(`Recipient Image: ${image}`);
                    this.cdr.markForCheck(); // Notify Angular about the change
                });
            });
        })
    );
    this.currentMailList$ = this.mailsSent$;
    this.isSentView = true;
}



}