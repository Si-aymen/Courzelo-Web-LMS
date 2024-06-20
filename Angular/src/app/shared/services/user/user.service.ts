import { Injectable } from '@angular/core';
import {StatusMessageResponse} from '../../models/user/StatusMessageResponse';
import {HttpClient} from '@angular/common/http';
import {ProfileInformationRequest} from '../../models/user/requests/ProfileInformationRequest';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';

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
  getProfileImageBlobUrl(): Observable<Blob> {
    return this.getProfileImage().pipe(
        map((arrayBuffer: ArrayBuffer) => {
          const blob = new Blob([arrayBuffer], { type: 'image/jpeg' });
          return blob;
        })
    );
  }
}
