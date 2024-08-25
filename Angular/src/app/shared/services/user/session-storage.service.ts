import { Injectable } from '@angular/core';
import {UserResponse} from '../../models/user/UserResponse';
import {UserService} from './user.service';
import {LoginResponse} from '../../models/user/LoginResponse';
import {ResponseHandlerService} from './response-handler.service';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {catchError, map, tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SessionStorageService {
  private userSubject: BehaviorSubject<UserResponse | null>;

  constructor(private userService: UserService,
              private handleResponse: ResponseHandlerService) {
    const user = this.getUserFromSession();
    this.userSubject = new BehaviorSubject<UserResponse | null>(user);
  }
  setUser(user: UserResponse): void {
    this.userSubject.next(user);
    sessionStorage.setItem('user', JSON.stringify(user));
  }
  setAuthenticated(authenticated: boolean): void {
    sessionStorage.setItem('authenticated', JSON.stringify(authenticated));
  }
  getAuthenticated(): boolean {
    return JSON.parse(sessionStorage.getItem('authenticated'));
  }
  getUserFromSession(): UserResponse {
    const userJson = sessionStorage.getItem('user');
    return userJson ? JSON.parse(userJson) : null;
  }
  getUser() {
    return this.userSubject.asObservable();
  }
  /*  getUser(): Observable<UserResponse> {
      const userInSession = sessionStorage.getItem('user');
      if (userInSession) {
        return of(JSON.parse(userInSession));
      } else {
        return this.userService.getUserProfile().pipe(
            tap(loginResponse => {
              this.setUser(loginResponse.user);
              this.setAuthenticated(true);
            }),
            catchError(error => {
              this.handleResponse.handleError(error);
              return of(null);
            }),
            map(loginResponse => loginResponse.user)
        );
      }
    }*/
  clearUser(): void {
    this.userSubject.next(null);
    sessionStorage.removeItem('user');
  }
}
