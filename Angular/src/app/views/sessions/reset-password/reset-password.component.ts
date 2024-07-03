import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../../../shared/services/user/user.service';
import {UpdatePasswordRequest} from '../../../shared/models/user/requests/UpdatePasswordRequest';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {
  constructor(    private userService: UserService,
                  private formBuilder: FormBuilder,
                  private route: ActivatedRoute,
                  private toastr: ToastrService,
                  private router: Router,
  ) { }
  code: string;
  updatePasswordRequest: UpdatePasswordRequest = {};
  passwordForm = this.formBuilder.group({
        password: ['', [Validators.required, Validators.maxLength(50), Validators.minLength(8)]],
        confirmPassword: ['', [Validators.required]],
      },
      {
        validator: this.ConfirmedValidator('password', 'confirmPassword'),
      }
  );

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.code = params['code'];
    });
    if (this.code === undefined) {
      this.router.navigateByUrl('/others/404');
    }
    this.passwordForm.controls.password.valueChanges.subscribe(() => {
      this.passwordForm.controls.confirmPassword.updateValueAndValidity();
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
    resetPassword() {
        if (this.passwordForm.valid) {
          this.updatePasswordRequest.newPassword = this.passwordForm.controls.password.value;
        this.userService.resetPassword(this.updatePasswordRequest, this.code)
            .subscribe(
                data => {this.handleSuccessResponse(data);
                            this.router.navigateByUrl('/sessions/signin');
                },
                error => this.handleErrorResponse(error)
            );
        } else {
        this.toastr.error('Invalid password', 'Error!', {progressBar: true});
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
