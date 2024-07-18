import {Component, OnInit} from '@angular/core';
import {SharedAnimations} from '../../../shared/animations/shared-animations';
import {AuthenticationService} from '../../../shared/services/user/authentication.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {LoginRequest} from '../../../shared/models/user/requests/LoginRequest';
import {Router} from '@angular/router';
import {SessionStorageService} from '../../../shared/services/user/session-storage.service';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';

@Component({
  selector: 'app-tfa',
  templateUrl: './tfa.component.html',
  styleUrls: ['./tfa.component.scss'],
  animations: [SharedAnimations]
})
export class TfaComponent implements OnInit {
  constructor(    private authService: AuthenticationService,
                  private formBuilder: FormBuilder,
                  private toastr: ToastrService,
                    private router: Router,
                    private sessionStorageService: SessionStorageService,
                  private responseHandlerService: ResponseHandlerService
  ) { }
  loading: boolean;
  loadingText: string;
  loginRequest: LoginRequest;

  codeForm = this.formBuilder.group({
        code: ['', [Validators.required]],
      }
  );
  ngOnInit() {
    this.loginRequest = history.state.loginRequest;
  }
  tfa() {
    if (this.codeForm.valid) {
      this.loading = true;
      this.loadingText = 'Verifying code...';
      this.authService.tfa(this.codeForm.controls.code.value, this.loginRequest)
          .subscribe(res => {
                  this.loading = false;
    this.responseHandlerService.handleSuccess(res.message);
                  this.sessionStorageService.setUser(res.user);
                this.router.navigateByUrl('/dashboard/v1');
              },
              error => {
                  this.loading = false;
                  this.responseHandlerService.handleError(error);
              }
          );
    } else {
      this.toastr.error('Invalid code', 'Error!', {progressBar: true});
    }
  }
}
