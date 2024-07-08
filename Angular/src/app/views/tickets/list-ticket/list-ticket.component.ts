import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { TicketDataService } from 'src/app/views/tickets/ticketdata.service'; // Adjust the path as necessary

@Component({
  selector: 'app-list-ticket',
  templateUrl: './list-ticket.component.html',
  styleUrls: ['./list-ticket.component.scss']
})
export class ListTicketComponent {
  tickets$: Observable<any[]>;

  constructor(
    private router: Router,
    private ticketDataService: TicketDataService
  ) {
    // Mock data for the table
    this.tickets$ = of([
      { id: 1, type: 'Bug', sujet: 'Issue 1', description: 'Description 1', date: '2024-01-01', status: 'waiting' },
      { id: 2, type: 'Feature', sujet: 'Issue 2', description: 'Description 2', date: '2024-02-01', status: 'doing' },
      { id: 3, type: 'Task', sujet: 'Issue 3', description: 'Description 3', date: '2024-03-01', status: 'done' }
    ]);
  }

  onButtonClick(row: any) {
    this.ticketDataService.sendTicketData(row); // Send row data to service
    this.router.navigate(['tickets/forward']); // Navigate to forward component
  }
}
