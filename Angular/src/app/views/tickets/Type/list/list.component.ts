import { TickettypeService } from './../../TicketTypeService/tickettype.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit{
  types$: Observable<any[]>;
  constructor(private router : Router,private tickettypeService:TickettypeService){ }

  ngOnInit(): void {
    this.getTickets();
  }
  getTickets(): void {
    this.types$ = this.tickettypeService.getTypeList().pipe(
      tap(data => console.log('Fetched tickets:', data)), 
      tap(data=>console.log(data)),// Log the data to the console
      catchError(err => {
        console.error('Error loading tickets', err);
        return of([]); // Return an empty array on error
      })
    );
  }

}
