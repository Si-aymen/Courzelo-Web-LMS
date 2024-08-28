import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {DomSanitizer} from '@angular/platform-browser';
import { Transports } from '../../models/transports/Transports';
import {Observable} from 'rxjs';
import { Stages } from '../../models/stages/stages';


@Injectable({
  providedIn: 'root'
})
export class StagesService {


  private baseUrl = 'http://localhost:8080/api/stages';



  constructor(private http: HttpClient, private sanitizer: DomSanitizer) {

   }

   getStages(): Observable<Stages[]> {
    return this.http.get<Stages[]>(`${this.baseUrl}/GetAll`);
  }

  getStageByID(id: string): Observable<Stages> {
    return this.http.get<Stages>(`${this.baseUrl}/GetById/stages/${id}`);
  }

  getCount(): Observable<number>{
    return this.http.get<number>(`${this.baseUrl}/count/stages`);
  }

  register(stage: Stages) {
    const bodyData = stage;

    this.http.post(`${this.baseUrl}/add-stage`, bodyData, { responseType: 'text' })
      .subscribe((resultData: any) => {
        console.log(resultData);
        alert("internship Registered Successfully");
      });
  }

  deleteStage(id: string): Observable<Object> {
    return this.http.delete(`${this.baseUrl}/remove-Stage/${id}`);
  }

}
