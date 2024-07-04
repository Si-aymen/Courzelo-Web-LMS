import { Injectable } from '@angular/core';
import {UserResponse} from '../../models/user/UserResponse';
import {UserService} from './user.service';
import {LoginResponse} from '../../models/user/LoginResponse';
import {ResponseHandlerService} from './response-handler.service';
import {Observable, of} from 'rxjs';
import {catchError, map, tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SessionStorageService {

  constructor(private userService: UserService,
              private handleResponse: ResponseHandlerService) { }

  setUser(user: UserResponse): void {
    sessionStorage.setItem('user', JSON.stringify(user));
  }

  getUser(): Observable<UserResponse> {
    const userInSession = sessionStorage.getItem('user');
    if (userInSession) {
      return of(JSON.parse(userInSession));
    } else {
      return this.userService.getUserProfile().pipe(
          tap(loginResponse => {
            this.setUser(loginResponse.user);
          }),
          catchError(error => {
            this.handleResponse.handleError(error);
            return of(null);
          }),
          map(loginResponse => loginResponse.user)
      );
    }
  }

  clearUser(): void {
    sessionStorage.removeItem('user');
  }
}
