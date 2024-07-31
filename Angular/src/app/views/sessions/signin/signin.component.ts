import { Component, OnInit } from '@angular/core';
import { SharedAnimations } from 'src/app/shared/animations/shared-animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router, RouteConfigLoadStart, ResolveStart, RouteConfigLoadEnd, ResolveEnd } from '@angular/router';
import {AuthenticationService} from '../../../shared/services/user/authentication.service';
import {ToastrService} from 'ngx-toastr';
import {SessionStorageService} from '../../../shared/services/user/session-storage.service';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';

@Component({
    selector: 'app-signin',
    templateUrl: './signin.component.html',
    styleUrls: ['./signin.component.scss'],
    animations: [SharedAnimations]
})
export class SigninComponent implements OnInit {
    loading: boolean;
    loadingText: string;
    signinForm: FormGroup;
    constructor(
        private fb: FormBuilder,
        private auth: AuthenticationService,
        private toastr: ToastrService,
        private router: Router,
        private sessionStorageService: SessionStorageService,
        private responseHandler: ResponseHandlerService
    ) { }

    ngOnInit() {
        this.router.events.subscribe(event => {
            if (event instanceof RouteConfigLoadStart || event instanceof ResolveStart) {
                this.loadingText = 'Loading Dashboard Module...';

                this.loading = true;
            }
            if (event instanceof RouteConfigLoadEnd || event instanceof ResolveEnd) {
                this.loading = false;
            }
        });

        this.signinForm = this.fb.group({
            email: ['test@example.com', Validators.required],
            password: ['1234', Validators.required],
            rememberMe: [false]
        });
    }

    signin() {
        if (this.signinForm.valid) {
        this.loading = true;
        this.loadingText = 'Sigining in...';
        this.auth.login(this.signinForm.value)
            .subscribe(res => {
                    this.loading = false;
                    if (res.twoFactorAuth) {
                        this.toastr.info(res.message, '2FA!', {progressBar: true});
                        this.router.navigateByUrl('/sessions/tfa',
                            {state: {loginRequest: this.signinForm.getRawValue()}});
                    } else {
                        this.responseHandler.handleSuccess(res.message);
                        this.sessionStorageService.setUser(res.user);
                        this.sessionStorageService.setAuthenticated(true);
                        this.router.navigateByUrl('/dashboard/v1');
                    }
            },
                error => {
                    this.loading = false;
                    this.responseHandler.handleError(error);
                }
        );
    } else {
            this.loading = false;
            this.toastr.error('Form is invalid', 'Error!', {progressBar: true});
        }
    }

}
