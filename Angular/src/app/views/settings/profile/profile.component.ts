import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {ProfileInformationRequest} from '../../../shared/models/user/requests/ProfileInformationRequest';
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  constructor(
      private formBuilder: FormBuilder,
      private toastr: ToastrService
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

  submit() {
    this.loading = true;
    setTimeout(() => {
      this.loading = false;
      this.toastr.success('Profile updated.', 'Success!', {progressBar: true});
    }, 3000);
  }
  updateProfileInformation() {
    if (this.informationForm.valid) {
      this.profileInfromationRequest = this.informationForm.getRawValue();
      console.log(this.profileInfromationRequest);
      this.submit();
    } else {
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
      default:
        this.toastr.error(errorMessage, 'Error!', {progressBar: true});
    }
  }
}
