import { Component, OnInit } from '@angular/core';
import { SharedAnimations } from 'src/app/shared/animations/shared-animations';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../../../service/user/authentication.service';
import {SignupRequest} from '../../../model/user/requests/SignupRequest';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
  animations: [SharedAnimations]
})
export class SignupComponent {

  constructor(    private authService: AuthenticationService,
                  private formBuilder: FormBuilder) { }
  signupForm = this.formBuilder.group({
        email: ['', [Validators.required, Validators.email]],
        password: ['', [Validators.required, Validators.maxLength(50), Validators.minLength(8)]],
        confirmPassword: ['', [Validators.required, Validators.maxLength(50), Validators.minLength(8)]],
      },
      {
        validator: this.ConfirmedValidator('password', 'confirmPassword'),
      }
  );
  signupRequest: SignupRequest = {};
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
      console.log(this.signupRequest);
      this.authService.register(this.signupRequest)
          .subscribe(data => {
                console.log('register data :', data);
              },
              error => {
                console.log('register error :', error);
              });
    } else {
      console.log('Form is invalid');
    }
  }
}
