import { TrelloserviceService } from '../Services/trello/trelloservice.service';
import { TrelloBoard } from './../../../shared/models/TrelloBoard';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { TicketDataService } from 'src/app/views/tickets/Services/TicketService/ticketdata.service'; // Adjust the path as necessary
import { TicketServiceService } from '../Services/TicketService/ticket-service.service';
import { Ticket } from 'src/app/shared/models/Ticket';
import { catchError, tap } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import Swal from 'sweetalert2';
import { TickettypeService } from '../Services/TicketTypeService/tickettype.service';
import { Etat } from 'src/app/shared/models/Etat';
import { Message } from 'src/app/shared/models/Message';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { SessionStorageService } from 'src/app/shared/services/user/session-storage.service';

@Component({
  selector: 'app-list-ticket',
  templateUrl: './list-ticket.component.html',
  styleUrls: ['./list-ticket.component.scss']
})
export class ListTicketComponent implements OnInit{
  tickets$: Observable<any[]>;
  ticket:Ticket []=[];
  trelloBoardList: TrelloBoard[] = [];
  TrelloBoard:TrelloBoard;
  types:any[]
  idCards: string[] = [];
ticketIds: string[] = [];
cardDetails: any[] = [];
connectedUser: UserResponse;
 message: Message = {
  to: "spnahmed2@gmail.com",
  subject: "Ticket",
  text: "Hello, I am happy to inform you that your problem is resolved.",
};
    constructor (private router : Router,
      private typeservice:TickettypeService,
      private trelloservice:TrelloserviceService,
    private ticketservice:TicketServiceService,
    private ticketDataService:TicketDataService,
    private sessionStorageService: SessionStorageService,
    public dialog: MatDialog){}

  ngOnInit(): void {
    this.getTickets();
  //this.changeStatus();
   this.getAllCards();
  }

  getAllCards() {
    this.ticketservice.getCards().subscribe((res: any[]) => {
      console.log("Le Details de Card", res);
  
      this.typeservice.getTypeList().subscribe(
        (typeList: any[]) => {
          console.log("Type List:", typeList);
          const trelloBoardList: TrelloBoard[] = [];
  
          let typeListCounter = 0; // To keep track of the completed requests
  
          typeList.forEach(item => {
            console.log("Type Item:", item.type);
  
            this.typeservice.getTrelloBoard(item.type).subscribe(
              (boardResponse: any) => {
                if (boardResponse && boardResponse.id && boardResponse.type) {
                  const trelloBoard: TrelloBoard = {
                    idBoard: boardResponse.id,
                    idListToDo: boardResponse.idListToDo,
                    idListDoing: boardResponse.idListDoing,
                    idListDone: boardResponse.idListDone,
                    type: boardResponse.type.type
                  };
                  trelloBoardList.push(trelloBoard);
                  console.log("Trello Board Added:", trelloBoard);
                } else {
                  console.warn(`Invalid board response for type: ${item.type}`);
                }
  
                typeListCounter++;
  
                if (typeListCounter === typeList.length) {
                  // Proceed only after all trello boards are fetched
                  res.forEach(card => {
                    console.log("le id de traitmene", card.idCard);
  
                    this.trelloservice.getListOfCard(card.idCard).subscribe((cardRes: any) => {
                      if (cardRes && cardRes.id) {
                        console.log("le id de list=====", cardRes.id);
  
                        trelloBoardList.forEach(board => {
                          res.forEach(card => {
                            this.trelloservice.getListOfCard(card.idCard).subscribe(
                              (cardRes: any) => {
                                console.log("Trello card fetched:", cardRes);
                                if (cardRes.id === board.idListDone) {
                                  console.log("Match found. Updating status for ticketId:", card.ticket.id);
                                  this.updateTicketStatus(card.ticket.id, Etat.FINIE);
                                }
                              },
                              error => {
                                console.error('Error fetching Trello card:', error);
                              }
                            );
                          });
                        });
                      } else {
                        console.warn(`Invalid card response for cardId: ${card.idCard}`);
                      }
                    });
                  });
                }
              },
              (error) => {
                console.error('Error fetching Trello board:', error);
                typeListCounter++;
                if (typeListCounter === typeList.length) {
                  // Handle the scenario where all requests are completed but some failed
                  console.warn('Some Trello board requests failed.');
                }
              }
            );
          });
        },
        (error) => {
          console.error('Error fetching type list:', error);
        }
      );
    });
  }
  

// Assuming this is within an Angular component or service
updateTicketStatus(ticketId: string, status: string) {
  this.ticketservice.updateStatusdone(ticketId, status).subscribe(
    (response: any) => {
      console.log(response);
      console.log("Status updated successfully for ticketId:", ticketId);
      
      /*// Check if response.status is 'FINIE'
      if (response.status === 'FINIE') {
        // Handle additional logic when status is 'FINIE'
        this.ticketservice.sendMessage(this.message).subscribe(
          (res: any) => {
            console.log("Message sent:", res);
          },
          error => {
            console.error('Error sending message:', error);
          }
        );
      }*/
    },
    error => {
      console.error('Error updating status:', error);
    }
  );
}


 getTickets(): void {
  this.connectedUser = this.sessionStorageService.getUserFromSession();
  console.log("Le USERRRRR CONNECTED :",this.connectedUser);
    this.tickets$ = this.ticketservice.getTicketList().pipe(
      tap(data => {
        console.log('Fetched tickets:', data);
        // Iterate through each ticket and call changeStatus
        data.forEach(ticket => {
          console.log(ticket.id); // Pass the id to changeStatus
        });
      }),
      catchError(err => {
        console.error('Error loading tickets', err);
        return of([]); // Return an empty array on error
      })
    );
  }


  onButtonClick(row: any) {
    this.ticketDataService.sendTicketData(row); // Send row data to service
    this.router.navigate(['tickets/forward']); // Navigate to forward component
  }


}
