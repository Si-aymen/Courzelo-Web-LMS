import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TicketType } from 'src/app/shared/models/TicketType';
import { TrelloBoard } from 'src/app/shared/models/TrelloBoard';

@Injectable({
  providedIn: 'root'
})
export class TickettypeService {

  private baseURL = '/tk/api/v1/tickettype/';
  constructor(private http:HttpClient) {}
    getTypeList():Observable<TicketType[]>{
      return this.http.get<TicketType[]>(`${this.baseURL}/all`);    }

  addType(type: TicketType): Observable<Object>{
    return this.http.post(`${this.baseURL}/add`, type);
  }

  getTypeId(id: string): Observable<TicketType>{
    return this.http.get<TicketType>(`${this.baseURL}/get/${id}`);
  }

  updateType(type: TicketType): Observable<Object>{
    return this.http.put(`${this.baseURL}/update`, type);
  }

  addTrello(data:any):any{
    return this.http.post(`${this.baseURL}/trello/add`,data);
    } 

  deleteType(id: string): Observable<Object>{
    return this.http.delete(`${this.baseURL}/delete/${id}`);
  }
  deleteTicketType(id: string): Observable<any> {
    return this.http.delete(`${this.baseURL}/delete1/${id}`);
  }
  getTrelloBoard(type:string):Observable<Object>{
    return this.http.get('/tk/v1/Board/bytype',{params:{
      type: type
    }})  }

    

    
}
