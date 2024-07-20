import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TicketDataService {
  private ticketDataSubject = new BehaviorSubject<any>(null);
  ticketData$ = this.ticketDataSubject.asObservable();

  constructor() { }

  sendTicketData(data: any) {
    this.ticketDataSubject.next(data);
  }
}
