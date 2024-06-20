import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {ProfileInformationRequest} from '../../../shared/models/user/requests/ProfileInformationRequest';
import {UserService} from '../../../shared/services/user/user.service';
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  constructor(
      private formBuilder: FormBuilder,
      private toastr: ToastrService,
      private userService: UserService
  ) { }
  loading: boolean;
  informationForm = this.formBuilder.group({
        name: ['', [Validators.required, Validators.maxLength(20), Validators.minLength(3)]],
        lastname: ['', [Validators.required, Validators.maxLength(20), Validators.minLength(3)]],
        bio: ['', [Validators.maxLength(300), Validators.minLength(20)]],
        title: ['', [  Validators.minLength(3), Validators.maxLength(20)]],
        birthDate: []
      }
  );
  profileInfromationRequest: ProfileInformationRequest = {};

  ngOnInit() {
  }

  updateUserProfile() {
    this.userService.updateUserProfile(this.profileInfromationRequest).subscribe(
        res => {
          this.loading = false;
          this.toastr.success(res.message, 'Success!', {progressBar: true});
        },
        error => {
          this.loading = false;
          this.handleErrorResponse(error);
        }
    );
  }
  updateProfileInformation() {
    this.loading = true;
    if (this.informationForm.valid) {
      this.profileInfromationRequest = this.informationForm.getRawValue();
      this.profileInfromationRequest.birthDate = `${this.informationForm.controls['birthDate'].value.year}-${this.informationForm.controls['birthDate'].value.month.toString().padStart(2, '0')}-${this.informationForm.controls['birthDate'].value.day.toString().padStart(2, '0')}`;

      this.updateUserProfile();
    } else {
      this.loading = false;
      this.toastr.error('Form is invalid', 'Error!', {progressBar: true});
    }
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
