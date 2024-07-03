import { Component, OnInit } from '@angular/core';
import { SharedAnimations } from 'src/app/shared/animations/shared-animations';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../../../shared/services/user/authentication.service';
import {ToastrService} from 'ngx-toastr';
import {FormBuilder, Validators} from '@angular/forms';

@Component({
  selector: 'app-forgot',
  templateUrl: './forgot.component.html',
  styleUrls: ['./forgot.component.scss'],
  animations: [SharedAnimations]
})
export class ForgotComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private router: Router,
              private auth: AuthenticationService,
              private toastr: ToastrService,
                private formBuilder: FormBuilder,
  ) { }
  emailForm = this.formBuilder.group({
        email: ['', [Validators.required, Validators.email]],
      }
  );

  ngOnInit() {
  }
  sendResetLink() {
    if (this.emailForm.valid) {
      this.auth.sendResetPasswordEmail(this.emailForm.controls.email.value)
          .subscribe(res => {
                  this.handleSuccessResponse(res);
                  this.emailForm.reset();
              },
              error => {
                  this.handleErrorResponse(error);
              }
          );
    } else {
      this.toastr.error('Invalid email', 'Error!', {progressBar: true});
    }
  }
  handleSuccessResponse(data) {
    console.log(data);
    this.toastr.success(data.message, 'Success!', {progressBar: true});
  }
  handleErrorResponse(error) {
    console.error(error);
    let errorMessage = 'An unexpected error occurred';
    if (error.error && error.error.message) {
      errorMessage = error.error.message;
    }
    switch (error.status) {
      case 409:
        this.toastr.error(errorMessage, 'Error!', {progressBar: true});
        break;
      case 400:
        this.toastr.error(errorMessage, 'Error!', {progressBar: true});
        break;
      default:
        this.toastr.error(errorMessage, 'Error!', {progressBar: true});
    }
  }
}
