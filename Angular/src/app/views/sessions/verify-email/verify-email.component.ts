import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../../../shared/services/user/authentication.service';
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-verify-email',
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.scss']
})
export class VerifyEmailComponent implements OnInit {
  code: string;
  constructor(private route: ActivatedRoute,
              private router: Router,
              private auth: AuthenticationService,
              private toastr: ToastrService
  ) { }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.code = params['code'];
    });
    this.auth.verifyEmail(this.code).subscribe(
      res => {
        console.log(res.message);
        this.handleSuccessResponse(res);
        this.router.navigateByUrl('/sessions/signin');
      },
      error => {
        console.error(error);
        this.handleErrorResponse(error);
        this.router.navigateByUrl('/sessions/signin');
      }
    );
  }
  handleSuccessResponse(data) {
    console.log(data);
    this.toastr.success(data.message, 'Success!', {progressBar: true});
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
