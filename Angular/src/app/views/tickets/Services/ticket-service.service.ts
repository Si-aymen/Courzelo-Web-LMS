import { HttpClient, HttpErrorResponse, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { CardDetails } from 'src/app/shared/models/CardDetails';
import { Message } from 'src/app/shared/models/Message';
import { Ticket } from 'src/app/shared/models/Ticket';
import { TicketREQ } from 'src/app/shared/models/TicketREQ';

@Injectable({
  providedIn: 'root'
})
export class TicketServiceService {

  private baseURL = 'http://localhost:8080/v1/ticket';
  private baseURL1 ='/tk/v1/ticket';
  //localhost:8080/v1/ticket/update/status/{{idticket}}/{{status}}
  constructor(private http:HttpClient) {}
  getTicketList(): Observable<Ticket[]> {
    return this.http.get<Ticket[]>('/tk/v1/ticket/all');
  }

  getCards(): Observable<CardDetails[]> {
    return this.http.get<CardDetails[]>('/tk/v1/Board/getCards');
  }
      
      updateStatusdone(id:any,statusId:any):any{
        return this.http.post(`${this.baseURL1}/update/statusdone/${id}/${statusId}`,null);
       }
       updateStatusdoing(id:any,statusId:any):any{
        return this.http.post(`${this.baseURL1}/update/statusdoing/${id}/${statusId}`,null);
       }

       addCardTrello(data:any):any{
        return this.http.post('tk/vi/ticket/card/add',data);
        } 
      forwardToEmployee(id:any,idEmployee:any):Observable<HttpEvent<Object>>{
        return this.http.post<HttpEvent<any>>(`${this.baseURL}/updateEmp/${id}/${idEmployee}`, null, {
          observe: 'events' // Specify 'events' to receive events including progress events
        }).pipe(
          catchError((error: HttpErrorResponse) => {
            // Handle error
            return throwError(error);
          })
        );
            }

  addTicket(ticket: TicketREQ): Observable<Object>{
    return this.http.post('/tk/v1/ticket/add', ticket);
  }
  sendMessage(message: Message): Observable<Object>{
    return this.http.post('/tk/send-email', message);
  }
 
  getTicketById(id: string): Observable<Ticket>{
    return this.http.get<Ticket>(`${this.baseURL}/get/${id}`);
  }

  updateTicket(ticket: Ticket,id:string): Observable<Object>{
    return this.http.put(`${this.baseURL}/update/${id}`, ticket);
  }

  deleteTicket(id: string): Observable<Object>{
    return this.http.delete(`${this.baseURL}/delete1/${id}`);
  }

   
}
