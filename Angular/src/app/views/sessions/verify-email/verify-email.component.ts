import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../../../shared/services/user/authentication.service';
import {ToastrService} from 'ngx-toastr';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';

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
              private toastr: ToastrService,
                private responseHandlerService: ResponseHandlerService) { }


  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.code = params['code'];
    });
    this.auth.verifyEmail(this.code).subscribe(
      res => {
        this.responseHandlerService.handleSuccess(res.message);
        this.router.navigateByUrl('/sessions/signin');
      },
      error => {
        console.error(error);
        this.responseHandlerService.handleError(error);
        this.router.navigateByUrl('/sessions/signin');
      }
    );
  }

}
