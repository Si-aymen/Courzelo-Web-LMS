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


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private baseUrl = 'http://localhost:8080/api/v1/auth';

  constructor(private http: HttpClient,
              private router: Router,
              private toastr: ToastrService,
              private sessionStorageService: SessionStorageService   ) {
  }

  register(signupRequest: SignupRequest) {
    return this.http.post<StatusMessageResponse>(`${this.baseUrl}/signup`, signupRequest);
  }

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, loginRequest);
  }

  logout() {
      this.sessionStorageService.clearUser();
      this.http.get<StatusMessageResponse>(`${this.baseUrl}/logout`).subscribe(
        res => {
            this.toastr.success(res.message, 'Success', {progressBar: true} );
          this.router.navigateByUrl('/sessions/signin');
        },
        error => {
            this.handleErrorResponse(error);
            this.router.navigateByUrl('/sessions/signin');
        }
    );
  }
  tfa(code: string, loginRequest: LoginRequest) {
    return this.http.post<LoginResponse>(`${this.baseUrl}/tfa`, loginRequest, {params: {code}});
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
            case 403:
                break;
            default:
                this.toastr.error(errorMessage, 'Error!', {progressBar: true});
        }
    }
}
