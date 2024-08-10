import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { ForumService } from 'src/app/shared/services/Forum/forum.service';
import { SessionStorageService } from 'src/app/shared/services/user/session-storage.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-forum',
  templateUrl: './add-forum.component.html',
  styleUrls: ['./add-forum.component.scss']
})
export class AddForumComponent implements OnInit {
discard() {
throw new Error('Method not implemented.');
}
  connectedUser: UserResponse;
  addforum: FormGroup = new FormGroup({});

  constructor(
    private forumservice: ForumService,
    private sessionStorageService: SessionStorageService,
    private formBuilder: FormBuilder,
    private router: Router,
  ) {}

  ngOnInit() {
    this.createForm();
    console.log("lelele");
    this.connectedUser = this.sessionStorageService.getUser();
  }
  createForm() {
    this.addforum = this.formBuilder.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      user: [''],
    });
  }
  onSubmit() {
    this.addforum.patchValue({ user: this.connectedUser.email });

    console.log('Forum Data Before Submit:', this.addforum.value);

    this.forumservice.addSubForum(this.addforum.value).subscribe({
      next: (res: any) => {
        console.log('Mail sent successfully:', res);
        Swal.fire({
          icon: 'success',
          title: 'Success!',
          text: 'Add  successfully!',
          confirmButtonText: 'OK'
        })
      },
      error: (err) => {
        console.error('Error sending mail:', err);
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'Failed to add . Please check the console for more details.',
          confirmButtonText: 'OK'
        });
      }
    });
  }
}
