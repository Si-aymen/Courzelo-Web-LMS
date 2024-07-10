import { Injectable } from '@angular/core';
import {StatusMessageResponse} from '../../models/user/StatusMessageResponse';
import {HttpClient} from '@angular/common/http';
import {ProfileInformationRequest} from '../../models/user/requests/ProfileInformationRequest';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';
import {LoginResponse} from '../../models/user/LoginResponse';
import {UpdatePasswordRequest} from '../../models/user/requests/UpdatePasswordRequest';
import {QRCodeResponse} from '../../models/user/QRCodeResponse';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:8080/api/v1/user';

  constructor(private http: HttpClient,    private sanitizer: DomSanitizer) { }
  updateUserProfile(profileInfromationRequest: ProfileInformationRequest) {
    return this.http.post<StatusMessageResponse>(`${this.baseUrl}/profile`, profileInfromationRequest);
  }
  uploadProfileImage(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<StatusMessageResponse>(`${this.baseUrl}/image`, formData);
  }
  getProfileImage(): Observable<ArrayBuffer> {
    return this.http.get(`${this.baseUrl}/image`, { responseType: 'arraybuffer' });
  }
  getUserProfile(): Observable<LoginResponse> {
    return this.http.get<LoginResponse>(`${this.baseUrl}/profile`);
  }
  getProfileImageBlobUrl(): Observable<Blob> {
    return this.getProfileImage().pipe(
        map((arrayBuffer: ArrayBuffer) => {
          const blob = new Blob([arrayBuffer], { type: 'image/jpeg' });
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
}
