import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {ProfileInformationRequest} from '../../../shared/models/user/requests/ProfileInformationRequest';
import {UserService} from '../../../shared/services/user/user.service';
import {SessionStorageService} from '../../../shared/services/user/session-storage.service';
import {UserResponse} from '../../../shared/models/user/UserResponse';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';
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
      private sessionStorageService: SessionStorageService,
      private responseHandlerService: ResponseHandlerService
  ) { }
  loading: boolean;
  connectedUser: UserResponse;
  informationForm = this.formBuilder.group({
        name: [' ', [Validators.required, Validators.maxLength(20), Validators.minLength(3)]],
        lastname: ['', [Validators.required, Validators.maxLength(20), Validators.minLength(3)]],
        bio: ['', [Validators.maxLength(300), Validators.minLength(20)]],
        title: ['', [  Validators.minLength(3), Validators.maxLength(20)]],
        birthDate: []
      }
  );
  profileInfromationRequest: ProfileInformationRequest = {};
    selectedFileName: string;
    selectedFileUrl: string | ArrayBuffer;
  birthDate: NgbDateStruct;
  file: any;

  ngOnInit() {
   this.connectedUser = this.sessionStorageService.getUser();
      this.initializeFormWithUserData();
    const date = new Date(this.connectedUser.profile.birthDate);
    this.birthDate = {
      year: date.getFullYear(),
      month: date.getMonth() + 1, // JavaScript months are 0-based
      day: date.getDate()
    };
  }
    initializeFormWithUserData() {
        if (this.connectedUser) {
            const date = new Date(this.connectedUser.profile.birthDate);
            this.birthDate = {
                year: date.getFullYear(),
                month: date.getMonth() + 1,
                day: date.getDate()
            };
            this.informationForm.patchValue({
                name: this.connectedUser.profile.name,
                lastname: this.connectedUser.profile.lastname,
                bio: this.connectedUser.profile.bio,
                title: this.connectedUser.profile.title,
            });
        }
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
          this.responseHandlerService.handleError(error);
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
  onFileSelected(event) {
    if (event.target.files.length > 0) {
       this.file = event.target.files[0];
        const reader = new FileReader();
      this.selectedFileName = this.file.name;
      reader.onload = (e) => this.selectedFileUrl = reader.result;
        reader.readAsDataURL(this.file);
    }
  }
  uploadProfilePicture() {
    this.loading = true;
    if (!this.file) {
      this.toastr.error('No file selected', 'Error!', {progressBar: true});
      this.loading = false;
      return;
    }
    this.userService.uploadProfileImage(this.file).subscribe(
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
            this.responseHandlerService.handleError(error);
        }
    );
  }
}
