import { Component, OnInit } from '@angular/core';
import { SharedAnimations } from 'src/app/shared/animations/shared-animations';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../../../shared/services/user/authentication.service';
import {SignupRequest} from '../../../shared/models/user/requests/SignupRequest';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
  animations: [SharedAnimations]
})
export class SignupComponent implements OnInit {
  constructor(    private authService: AuthenticationService,
                  private formBuilder: FormBuilder,
                  private toastr: ToastrService
                  ) { }
  signupForm = this.formBuilder.group({
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.maxLength(50), Validators.minLength(8)]],
        confirmPassword: ['', [Validators.required]],
      },
      {
        validator: this.ConfirmedValidator('password', 'confirmPassword'),
      }
  );
  signupRequest: SignupRequest = {};
  ngOnInit() {
    this.signupForm.controls.password.valueChanges.subscribe(() => {
      this.signupForm.controls.confirmPassword.updateValueAndValidity();
    });
  }
  ConfirmedValidator(controlName: string, matchingControlName: string) {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[controlName];
      const matchingControl = formGroup.controls[matchingControlName];
      if (matchingControl.errors) {
        return;
      }
      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({confirmedValidator: true});
      } else {
        matchingControl.setErrors(null);
      }
    };
  }
  saveUser() {
    if (this.signupForm.valid) {
      this.signupRequest = this.signupForm.getRawValue();
      this.authService.register(this.signupRequest)
          .subscribe(
              data => this.handleSuccessResponse(data),
              error => this.handleErrorResponse(error)
          );
    } else {
      this.showFormInvalidError();
    }
  }

  handleSuccessResponse(data) {
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

  showFormInvalidError() {
    this.toastr.error('Form is invalid', 'Error!', {progressBar: true});
  }
}
