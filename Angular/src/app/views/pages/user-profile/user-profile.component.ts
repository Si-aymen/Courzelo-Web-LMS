import { Component, OnInit } from '@angular/core';
import {UserResponse} from '../../../shared/models/user/UserResponse';
import {SessionStorageService} from '../../../shared/services/user/session-storage.service';
import {UserService} from '../../../shared/services/user/user.service';
import {DomSanitizer} from '@angular/platform-browser';
import {ActivatedRoute, Router} from '@angular/router';
import {LoginResponse} from '../../../shared/models/user/LoginResponse';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {

  constructor(
      private sessionStorageService: SessionStorageService,
      private userService: UserService,
      private sanitizer: DomSanitizer,
      private route: ActivatedRoute,
      private router: Router,
      private HandleResponse: ResponseHandlerService
  ) { }
  imageSrc: any;
  user: UserResponse;
  ngOnInit() {
      this.route.paramMap.subscribe(params => {
          let userEmail = params.get('email');
          if (userEmail === null || userEmail === undefined) {
                userEmail = this.sessionStorageService.getUserEmail();
          }
          this.userService.getUserProfileByEmail(userEmail).subscribe((user: LoginResponse) => {
                this.user = user.user;
              this.userService.getProfileImageBlobUrl(this.user.email).subscribe((blob: Blob) => {
                  const objectURL = URL.createObjectURL(blob);
                  this.imageSrc = this.sanitizer.bypassSecurityTrustUrl(objectURL);
              });
          }, error => {
              this.HandleResponse.handleError(error);
              this.router.navigateByUrl('/others/404');
          });

      });

  }

}
