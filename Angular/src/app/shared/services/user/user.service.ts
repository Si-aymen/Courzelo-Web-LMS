import { Injectable } from '@angular/core';
import {StatusMessageResponse} from '../../models/user/StatusMessageResponse';
import {HttpClient} from '@angular/common/http';
import {ProfileInformationRequest} from '../../models/user/requests/ProfileInformationRequest';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {LoginResponse} from '../../models/user/LoginResponse';
import {UpdatePasswordRequest} from '../../models/user/requests/UpdatePasswordRequest';
import {QRCodeResponse} from '../../models/user/QRCodeResponse';
import {SessionStorageService} from './session-storage.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:8080/api/v1/user';
  image: Blob;
  storedUser;
  constructor(private http: HttpClient ) { }
  updateUserProfile(profileInfromationRequest: ProfileInformationRequest) {
    return this.http.post<StatusMessageResponse>(`${this.baseUrl}/profile`, profileInfromationRequest);
  }
  getCountries(): Observable<string[]> {
    return this.http.get<string[]>('/assets/country-by-name.json');
  }
  uploadProfileImage(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<StatusMessageResponse>(`${this.baseUrl}/image`, formData);
  }
  getProfileImage(email: string): Observable<ArrayBuffer> {
    return this.http.get(`${this.baseUrl}/image`, { responseType: 'arraybuffer', params: {email} });
  }
  getUserProfile(): Observable<LoginResponse> {
    return this.http.get<LoginResponse>(`${this.baseUrl}/profile`);
  }
  getProfileImageBlobUrl(email: string): Observable<Blob> {
    this.storedUser = JSON.parse(sessionStorage.getItem('user'));
    if (this.image && this.storedUser.email === email) {
        return new Observable<Blob>((observer) => {
            observer.next(this.image);
            observer.complete();
        });
    }
    return this.getProfileImage(email).pipe(
        map((arrayBuffer: ArrayBuffer) => {
          const blob = new Blob([arrayBuffer], { type: 'image/jpeg' });
          if (this.storedUser.email === email) {
            this.image = blob;
          }
          return blob;
        })
    );
  }
  updatePassword(updatePasswordRequest: UpdatePasswordRequest) {
    return this.http.post<StatusMessageResponse>(`${this.baseUrl}/updatePassword`, updatePasswordRequest);
  }
  getQRCode(): Observable<QRCodeResponse> {
    return this.http.get<QRCodeResponse>(`${this.baseUrl}/qrCode`);
  }
  enable2FA(verificationCode: string): Observable<StatusMessageResponse> {
    return this.http.post<StatusMessageResponse>(`${this.baseUrl}/enableTwoFactorAuth`, null, {params: {verificationCode}});
  }
  disable2FA(): Observable<StatusMessageResponse> {
    return this.http.delete<StatusMessageResponse>(`${this.baseUrl}/disableTwoFactorAuth`);
  }
  resetPassword(updatePasswordRequest: UpdatePasswordRequest, code: string): Observable<StatusMessageResponse> {
    return this.http.post<StatusMessageResponse>(`${this.baseUrl}/reset-password`, updatePasswordRequest, {params: {code}});
  }
  getUserProfileByEmail(email: string): Observable<LoginResponse> {
    return this.http.get<LoginResponse>(`${this.baseUrl}/profile/${email}`);
  }
}
