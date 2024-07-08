import { Component, OnInit } from '@angular/core';
import { SharedAnimations } from 'src/app/shared/animations/shared-animations';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../../../shared/services/user/authentication.service';
import {SignupRequest} from '../../../shared/models/user/requests/SignupRequest';
import {ToastrService} from 'ngx-toastr';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {UserService} from '../../../shared/services/user/user.service';
import {formatDate} from "@angular/common";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
  animations: [SharedAnimations]
})
export class SignupComponent implements OnInit {
  constructor(    private authService: AuthenticationService,
                  private formBuilder: FormBuilder,
                  private toastr: ToastrService,
                  private responseHandler: ResponseHandlerService,
                  private userService: UserService
                  ) {
      const today = new Date();
      const minAge = new Date(today.getFullYear() - 18, today.getMonth(), today.getDate());

      this.maxDate = {
          year: minAge.getFullYear(),
          month: minAge.getMonth() + 1,
          day: minAge.getDate()
      };
  }
  signupForm = this.formBuilder.group({
        email: ['', [Validators.required, Validators.email]],
        name: ['', [Validators.required, Validators.maxLength(20), Validators.minLength(3)]],
        lastname: ['', [Validators.required, Validators.maxLength(20), Validators.minLength(3)]],
        birthDate: [[Validators.required]],
        gender: ['', [Validators.required]],
        country: ['', [Validators.required]],
        password: ['', [Validators.required, Validators.maxLength(50), Validators.minLength(8)]],
        confirmPassword: ['', [Validators.required]],
      },
      {
        validator: this.ConfirmedValidator('password', 'confirmPassword'),
      }
  );
  signupRequest: SignupRequest = {};
  maxDate: NgbDateStruct;
  countries = [];
  ngOnInit() {
    this.signupForm.controls.password.valueChanges.subscribe(() => {
      this.signupForm.controls.confirmPassword.updateValueAndValidity();
      this.shouldShowError('confirmPassword', 'confirmedValidator');
    });
    this.userService.getCountries().subscribe(
        countries => {
          this.countries = countries;
          console.log(this.countries);
        }
    );
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
    shouldShowError(controlName: string, errorName: string): boolean {
        const control = this.signupForm.get(controlName);
        return control && control.errors && control.errors[errorName] && (control.dirty || control.touched);
    }
  saveUser() {
    if (this.signupForm.valid) {
      this.signupRequest = this.signupForm.getRawValue();
        this.signupRequest.birthDate = `${this.signupForm.controls['birthDate'].value.year}-${this.signupForm.controls['birthDate'].value.month.toString().padStart(2, '0')}-${this.signupForm.controls['birthDate'].value.day.toString().padStart(2, '0')}`;
        console.log(this.signupRequest);
      this.authService.register(this.signupRequest)
          .subscribe(
              data => this.responseHandler.handleSuccess(data.message),
              error => this.responseHandler.handleError(error)
          );
    } else {
      this.showFormInvalidError();
    }
  }


  showFormInvalidError() {
    this.toastr.error('Form is invalid', 'Error!', {progressBar: true});
  }
}
