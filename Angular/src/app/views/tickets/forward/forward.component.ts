import { Component, OnInit } from '@angular/core';
import { TicketDataService } from 'src/app/views/tickets/ticketdata.service'; // Adjust the path as necessary
import { Observable } from 'rxjs';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { TrelloserviceService } from './../trello/trelloservice.service'; // Adjust the path as necessary
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-forward',
  templateUrl: './forward.component.html',
  styleUrls: ['./forward.component.scss']
})
export class ForwardComponent implements OnInit {
middle() {
throw new Error('Method not implemented.');
}
  id: any; // Define id property
  sujet: any; // Define sujet property
  details: any; // Define details property

  ticketData$: Observable<any>;
  data = new FormGroup({
    developper: new FormControl('', Validators.required),
    activity: new FormControl('', Validators.required),
    /*projet: new FormControl(''),
    client: new FormControl('')*/
  });

  employee = ["touatiahmed", "ahmed_touati"];

  constructor(private ticketDataService: TicketDataService, private trelloService: TrelloserviceService) { }

  ngOnInit(): void {
    this.ticketData$ = this.ticketDataService.ticketData$;
    // Subscribe to ticketData$ to get the data when it emits
    this.ticketData$.subscribe(data => {
      if (data) {
        // Access id, sujet, and description from the data object
        this.id = data.id;
        this.sujet = data.sujet;
        this.details = data.description;

        console.log('ID:', this.id);
        console.log('SUJET:', this.sujet);
        console.log('Description:', this.details);

        // You can now use id, sujet, and details as needed in your component
      }
    });
  }

  forward() {
    const idListToDo = environment.idListToDo;
    const idListDoing = environment.idListDoing;
    const idListDone = environment.idListDone;
    const member = this.data.value['developper'];
    const complaintId = this.id; // Use this.id instead of this.idd
    const complaintName = this.sujet;
    const complaintDetails = this.details; // Use this.details
    const activity = String(this.data.value['activity']);

    var splitted = activity.split(".", activity.length);

    console.log('Forwarding Data:', member, complaintId, complaintName, complaintDetails, activity);

    this.trelloService.createCard(idListToDo, this.sujet, this.details).subscribe((card: any) => {
      console.log(card);
      console.log("Card ID:", card.id);
      
      this.trelloService.addCheckList(card.id).subscribe((checkList: any) => {
        console.log("Checklist ID:", checkList.id);
        
        splitted.forEach(t => {
          this.trelloService.addItemToCheckList(checkList.id, t).subscribe((res: any) => {
            console.log("show:++++",card.id,member);
            this.trelloService.addEmployeToCard(card.id, member).subscribe((res: any) => {
              // Implement further actions if needed
            });
          })

        });
      });
    });
  }
}
