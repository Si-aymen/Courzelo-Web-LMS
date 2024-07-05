import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {StatusMessageResponse} from '../../models/user/StatusMessageResponse';
import {LoginResponse} from '../../models/user/LoginResponse';
import {Observable} from 'rxjs';
import {LoginRequest} from '../../models/user/requests/LoginRequest';
import {SignupRequest} from '../../models/user/requests/SignupRequest';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {SessionStorageService} from './session-storage.service';
import {ResponseHandlerService} from './response-handler.service';


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private baseUrl = 'http://localhost:8080/api/v1/auth';

  constructor(private http: HttpClient,
              private router: Router,
              private toastr: ToastrService,
              private sessionStorageService: SessionStorageService,
              private responseHandlerService: ResponseHandlerService) {
  }

  register(signupRequest: SignupRequest) {
    return this.http.post<StatusMessageResponse>(`${this.baseUrl}/signup`, signupRequest);
  }

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, loginRequest);
  }
  logout() {
      this.sessionStorageService.clearUser();
    return  this.http.get<StatusMessageResponse>(`${this.baseUrl}/logout`);
  }
  logoutImpl() {
      this.sessionStorageService.clearUser();
    this.http.get<StatusMessageResponse>(`${this.baseUrl}/logout`).subscribe(
        res => {
            this.toastr.success(res.message, 'Success', {progressBar: true} );
          this.router.navigateByUrl('/sessions/signin');
        },
        error => {
            this.responseHandlerService.handleError(error);
            this.router.navigateByUrl('/sessions/signin');
        }
    );
  }
  tfa(code: string, loginRequest: LoginRequest) {
    return this.http.post<LoginResponse>(`${this.baseUrl}/tfa`, loginRequest, {params: {code}});
  }
  verifyEmail(code: string) {
    return this.http.get<StatusMessageResponse>(`${this.baseUrl}/verify-email`, {params: {code}});
  }
  resendVerificationEmail(email: string) {
    return this.http.get<StatusMessageResponse>(`${this.baseUrl}/resend-verification-email`, {params: {email}});
  }
  sendResetPasswordEmail(email: string) {
    return this.http.get<StatusMessageResponse>(`${this.baseUrl}/forgot-password`, {params: {email}});
  }
}
