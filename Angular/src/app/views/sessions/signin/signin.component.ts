import { Component, OnInit } from '@angular/core';
import { SharedAnimations } from 'src/app/shared/animations/shared-animations';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router, RouteConfigLoadStart, ResolveStart, RouteConfigLoadEnd, ResolveEnd } from '@angular/router';
import {AuthenticationService} from '../../../shared/services/user/authentication.service';
import {ToastrService} from 'ngx-toastr';
import {SessionStorageService} from '../../../shared/services/user/session-storage.service';

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
        private sessionStorageService: SessionStorageService
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
            password: ['1234', Validators.required]
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
                        this.handleSuccessResponse(res);
                        this.sessionStorageService.setUser(res.user);
                        this.router.navigateByUrl('/dashboard/v1');
                    }
            },
                error => {
                    this.loading = false;
                    this.handleErrorResponse(error);
                }
        );
    } else {
            this.loading = false;
            this.toastr.error('Form is invalid', 'Error!', {progressBar: true});
        }
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
