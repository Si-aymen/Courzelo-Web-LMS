import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {UserService} from '../../../shared/services/user/user.service';
import {SessionStorageService} from '../../../shared/services/user/session-storage.service';
import {UpdatePasswordRequest} from '../../../shared/models/user/requests/UpdatePasswordRequest';
import {SignupRequest} from '../../../shared/models/user/requests/SignupRequest';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {
  constructor(
      private formBuilder: FormBuilder,
      private toastr: ToastrService,
      private userService: UserService,
      private sessionStorageService: SessionStorageService
  ) { }
  loading: boolean;
  updatePasswordForm = this.formBuilder.group({
        password: ['', [Validators.required]],
        newPassword: ['', [Validators.required, Validators.maxLength(50), Validators.minLength(8)]],
        cPassword: ['', [Validators.required]],
      },
      {
        validator: this.ConfirmedValidator('newPassword', 'cPassword'),
      }
  );
  updatePasswordRequest: UpdatePasswordRequest;
  ngOnInit() {
    this.updatePasswordForm.controls.newPassword.valueChanges.subscribe(() => {
      this.updatePasswordForm.controls.cPassword.updateValueAndValidity();
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
  updatePassword() {
    if (this.updatePasswordForm.invalid) {
      this.toastr.error('Please fill out the form correctly', 'Error!', {progressBar: true});
      return;
    }
    this.loading = true;
    this.updatePasswordRequest = this.updatePasswordForm.getRawValue();
    console.log(this.updatePasswordRequest);
    this.userService.updatePassword(this.updatePasswordRequest).subscribe(
        () => {
          this.toastr.success('Password updated successfully', 'Success!', {progressBar: true});
          this.updatePasswordForm.reset();
          this.loading = false;
        },
        error => {
          this.handleErrorResponse(error);
          this.loading = false;
        }
    );
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
      case 401:
        break;
      default:
        this.toastr.error(errorMessage, 'Error!', {progressBar: true});
    }
  }
}
