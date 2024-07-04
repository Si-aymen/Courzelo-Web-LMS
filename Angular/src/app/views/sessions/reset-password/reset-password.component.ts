import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../../../shared/services/user/user.service';
import {UpdatePasswordRequest} from '../../../shared/models/user/requests/UpdatePasswordRequest';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';

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
                  private responseHandler: ResponseHandlerService
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
                data => {this.responseHandler.handleSuccess(data.message);
                            this.router.navigateByUrl('/sessions/signin');
                },
                error => this.responseHandler.handleError(error)
            );
        } else {
        this.toastr.error('Invalid password', 'Error!', {progressBar: true});
        }
    }
}
