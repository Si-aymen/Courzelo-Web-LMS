import { Injectable } from '@angular/core';
import {SignupRequest} from '../../models/user/requests/SignupRequest';
import {StatusMessageResponse} from '../../models/user/StatusMessageResponse';
import {HttpClient} from '@angular/common/http';
import {ProfileInformationRequest} from '../../models/user/requests/ProfileInformationRequest';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:8080/api/v1/user';

  constructor(private http: HttpClient) { }
  updateUserProfile(profileInfromationRequest: ProfileInformationRequest) {
    return this.http.post<StatusMessageResponse>(`${this.baseUrl}/profile`, profileInfromationRequest);
  }
  uploadProfileImage(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<StatusMessageResponse>(`${this.baseUrl}/image`, formData);
  }
}
