import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {DomSanitizer} from '@angular/platform-browser';
import { Transports } from '../../models/transports/Transports';
import {Observable} from 'rxjs';



@Injectable({
  providedIn: 'root'
})
export class TransportsService {

  private baseUrl = 'http://localhost:8080/api/transports';



  constructor(private http: HttpClient, private sanitizer: DomSanitizer) {

   }

   getTransports(): Observable<Transports[]> {
    return this.http.get<Transports[]>(`${this.baseUrl}/GetAll`);
  }

  getCount(): Observable<number>{
    return this.http.get<number>(`${this.baseUrl}/count/transports`);
  }

  register(transport: Transports) {
    const bodyData = transport;

    this.http.post(`${this.baseUrl}/add-Transports`, bodyData, { responseType: 'text' })
      .subscribe((resultData: any) => {
        console.log(resultData);
        alert("Transport Registered Successfully");
      });
  }

}
