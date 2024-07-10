import { Component, OnInit } from '@angular/core';
import {UserResponse} from "../../../shared/models/user/UserResponse";
import {AuthenticationService} from "../../../shared/services/user/authentication.service";
import {SessionStorageService} from "../../../shared/services/user/session-storage.service";
import {UserService} from "../../../shared/services/user/user.service";
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  constructor(
      private sessionStorageService: SessionStorageService,
      private userService: UserService,
      private sanitizer: DomSanitizer
  ) { }
  imageSrc: any;
  user: UserResponse = this.sessionStorageService.getUser();
  ngOnInit() {
    this.userService.getProfileImageBlobUrl().subscribe((blob: Blob) => {
      const objectURL = URL.createObjectURL(blob);
      this.imageSrc = this.sanitizer.bypassSecurityTrustUrl(objectURL);
    });
  }

}
