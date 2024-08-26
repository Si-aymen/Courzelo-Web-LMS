import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {StatusMessageResponse} from '../../models/user/StatusMessageResponse';
import {LoginResponse} from '../../models/user/LoginResponse';
import {BehaviorSubject, Observable} from 'rxjs';
import {LoginRequest} from '../../models/user/requests/LoginRequest';
import {SignupRequest} from '../../models/user/requests/SignupRequest';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {SessionStorageService} from './session-storage.service';
import {ResponseHandlerService} from './response-handler.service';
import {map, tap} from 'rxjs/operators';
import {UserService} from './user.service';
import {NavigationService} from '../navigation.service';


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private baseUrl = 'http://localhost:8080/api/v1/auth';
  constructor(private http: HttpClient,
              private router: Router,
              private toastr: ToastrService,
              private sessionStorageService: SessionStorageService,
              private responseHandlerService: ResponseHandlerService,
              private userService: UserService,
              private navigation: NavigationService) {
  }

  register(signupRequest: SignupRequest) {
    return this.http.post<StatusMessageResponse>(`${this.baseUrl}/signup`, signupRequest);
  }

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, loginRequest);
  }
  logout() {
      this.sessionStorageService.clearUser();
      this.sessionStorageService.setAuthenticated(false);
      return  this.http.get<StatusMessageResponse>(`${this.baseUrl}/logout`);
  }
  logoutImpl() {
      this.sessionStorageService.clearUser();
      this.sessionStorageService.setAuthenticated(false);
      this.userService.image = null;
      this.userService.storedUser = null;
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
    checkAuthState(): Observable<boolean> {
        if (this.sessionStorageService.getAuthenticated() && this.sessionStorageService.getUserFromSession()) {
            console.log('CheckAUTH Authenticated');
            return new BehaviorSubject<boolean>(true);
        }
        return this.http.get<LoginResponse>(`${this.baseUrl}/check-auth`).pipe(
            tap((response: LoginResponse) => {
                if (response.user) {
                    console.log('CheckAUTH Authenticated');
                    this.sessionStorageService.setUser(response.user);
                    this.sessionStorageService.setAuthenticated(true);
                } else {
                    console.log('CheckAUTH Not Authenticated');
                    this.sessionStorageService.setAuthenticated(false);
                    this.sessionStorageService.clearUser();
                }
            }),
            map((response: any) => this.sessionStorageService.getAuthenticated())
        );
    }
    refreshMyInfo(): void {
         this.http.get<LoginResponse>(`${this.baseUrl}/check-auth`).subscribe(
                response => {
                    this.sessionStorageService.setUser(response.user);
                    this.sessionStorageService.setAuthenticated(true);
                },
                error => {
                    this.responseHandlerService.handleError(error);
                    this.sessionStorageService.setAuthenticated(false);
                    this.sessionStorageService.clearUser();
                    this.router.navigateByUrl('/sessions/signin');
                }
            );
    }
    refreshPageInfo(): void {
      this.refreshMyInfo();
      this.navigation.setDefaultMenu();
    }
}