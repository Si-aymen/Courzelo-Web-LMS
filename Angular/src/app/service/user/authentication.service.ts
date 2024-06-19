import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {StatusMessageResponse} from '../../model/user/StatusMessageResponse';
import {LoginResponse} from '../../model/user/LoginResponse';
import {Observable} from 'rxjs';
import {LoginRequest} from '../../model/user/requests/LoginRequest';
import {SignupRequest} from '../../model/user/requests/SignupRequest';


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private baseUrl = 'http://localhost:8080/api/v1/auth';

  constructor(private http: HttpClient) {
  }

  register(signupRequest: SignupRequest) {
    return this.http.post<StatusMessageResponse>(`${this.baseUrl}/signup`, signupRequest);
  }

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/login`, loginRequest);
  }

  logout() {
    return this.http.get(`${this.baseUrl}/logout`);
  }
}
