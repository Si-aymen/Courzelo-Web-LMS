import { Injectable } from '@angular/core';
import {UserResponse} from '../../models/user/UserResponse';

@Injectable({
  providedIn: 'root'
})
export class SessionStorageService {

  constructor() { }

  setUser(user: UserResponse): void {
    sessionStorage.setItem('user', JSON.stringify(user));
  }

  getUser(): UserResponse {
    const user = sessionStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  }

  clearUser(): void {
    sessionStorage.removeItem('user');
  }
}
