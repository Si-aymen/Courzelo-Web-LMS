import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {ProfileInformationRequest} from '../../../shared/models/user/requests/ProfileInformationRequest';
import {UserService} from '../../../shared/services/user/user.service';
import {SessionStorageService} from '../../../shared/services/user/session-storage.service';
import {UserResponse} from '../../../shared/models/user/UserResponse';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  constructor(
      private formBuilder: FormBuilder,
      private toastr: ToastrService,
      private userService: UserService,
      private sessionStorageService: SessionStorageService
  ) { }
  loading: boolean;
  connectedUser: UserResponse = this.sessionStorageService.getUser();
  informationForm = this.formBuilder.group({
        name: [this.connectedUser.profile.name, [Validators.required, Validators.maxLength(20), Validators.minLength(3)]],
        lastname: [this.connectedUser.profile.lastname, [Validators.required, Validators.maxLength(20), Validators.minLength(3)]],
        bio: [this.connectedUser.profile.bio, [Validators.maxLength(300), Validators.minLength(20)]],
        title: [this.connectedUser.profile.title, [  Validators.minLength(3), Validators.maxLength(20)]],
        birthDate: []
      }
  );
  profileInfromationRequest: ProfileInformationRequest = {};
    selectedFileName: string;
    selectedFileUrl: string | ArrayBuffer;
  birthDate: NgbDateStruct;

  ngOnInit() {
    const date = new Date(this.connectedUser.profile.birthDate);
    this.birthDate = {
      year: date.getFullYear(),
      month: date.getMonth() + 1, // JavaScript months are 0-based
      day: date.getDate()
    };
  }

  updateUserProfile() {
    this.userService.updateUserProfile(this.profileInfromationRequest).subscribe(
        res => {
          this.userService.getUserProfile().subscribe(
                user => {
                    this.sessionStorageService.setUser(user.user);
                }
            );
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
      if (this.informationForm.controls['birthDate'].value) {
      this.profileInfromationRequest.birthDate = `${this.informationForm.controls['birthDate'].value.year}-${this.informationForm.controls['birthDate'].value.month.toString().padStart(2, '0')}-${this.informationForm.controls['birthDate'].value.day.toString().padStart(2, '0')}`;
        }

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
  onFileSelected(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
        const reader = new FileReader();
      this.selectedFileName = file.name;
      reader.onload = (e) => this.selectedFileUrl = reader.result;
        reader.readAsDataURL(file);
      this.userService.uploadProfileImage(file).subscribe(
          res => {
            this.toastr.success(res.message, 'Success!', {progressBar: true});
            event.target.value = '';
          },
          error => {
            this.handleErrorResponse(error);
            event.target.value = '';
          }
      );
    }
  }
}
