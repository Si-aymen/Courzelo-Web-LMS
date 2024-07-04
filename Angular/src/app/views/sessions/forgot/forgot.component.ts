import { Component, OnInit } from '@angular/core';
import { SharedAnimations } from 'src/app/shared/animations/shared-animations';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../../../shared/services/user/authentication.service';
import {ToastrService} from 'ngx-toastr';
import {FormBuilder, Validators} from '@angular/forms';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';

@Component({
  selector: 'app-forgot',
  templateUrl: './forgot.component.html',
  styleUrls: ['./forgot.component.scss'],
  animations: [SharedAnimations]
})
export class ForgotComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private router: Router,
              private auth: AuthenticationService,
              private toastr: ToastrService,
                private formBuilder: FormBuilder,
              private responseHandler: ResponseHandlerService
  ) { }
  emailForm = this.formBuilder.group({
        email: ['', [Validators.required, Validators.email]],
      }
  );

  ngOnInit() {
  }
  sendResetLink() {
    if (this.emailForm.valid) {
      this.auth.sendResetPasswordEmail(this.emailForm.controls.email.value)
          .subscribe(res => {
                  this.responseHandler.handleSuccess(res.message);
                  this.emailForm.reset();
              },
              error => {
                  this.responseHandler.handleError(error);
              }
          );
    } else {
      this.toastr.error('Invalid email', 'Error!', {progressBar: true});
    }
  }
}
