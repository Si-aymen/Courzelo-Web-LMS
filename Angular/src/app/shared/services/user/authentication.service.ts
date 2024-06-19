import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {StatusMessageResponse} from '../../models/user/StatusMessageResponse';
import {LoginResponse} from '../../models/user/LoginResponse';
import {Observable} from 'rxjs';
import {LoginRequest} from '../../models/user/requests/LoginRequest';
import {SignupRequest} from '../../models/user/requests/SignupRequest';
import {Router} from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private baseUrl = 'http://localhost:8080/api/v1/auth';

  constructor(private http: HttpClient, private router: Router) {
  }

  register(signupRequest: SignupRequest) {
    return this.http.post<StatusMessageResponse>(`${this.baseUrl}/signup`, signupRequest);
  }

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, loginRequest);
  }

  logout() {
    this.http.get(`${this.baseUrl}/logout`).subscribe(
        res => {
            console.log(res);
          this.router.navigateByUrl('/sessions/signin');
        },
        error => {
            console.log(error);
        }
    );
  }
}
