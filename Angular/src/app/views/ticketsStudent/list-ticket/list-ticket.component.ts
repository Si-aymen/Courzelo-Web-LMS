import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { Etat } from 'src/app/shared/models/Etat';
import { Message } from 'src/app/shared/models/Message';
import { Ticket } from 'src/app/shared/models/Ticket';
import { TrelloBoard } from 'src/app/shared/models/TrelloBoard';
import { UserResponse } from 'src/app/shared/models/user/UserResponse';
import { SessionStorageService } from 'src/app/shared/services/user/session-storage.service';
import Swal from 'sweetalert2';
import { TicketServiceService } from '../../tickets/Services/TicketService/ticket-service.service';
import { TicketDataService } from '../../tickets/Services/TicketService/ticketdata.service';
import { TickettypeService } from '../../tickets/Services/TicketTypeService/tickettype.service';
import { TrelloserviceService } from '../../tickets/Services/trello/trelloservice.service';
import { UpdateTicketComponent } from '../update-ticket/update-ticket.component';

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
  
          let typeListCounter = 0;
  
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
    this.tickets$ = this.ticketservice.getTicketsByUser(this.connectedUser.email).pipe(
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



  onRate(row: any) {
    this.ticketDataService.sendTicketData(row); // Send row data to service
    this.router.navigate(['ticketsStudent/rate']); // Navigate to forward component
  }
  update(id:any){
    const dialogRef = this.dialog.open(UpdateTicketComponent,{
      width : "40%",
      height: "10%",
      data: { ticket:id}
    });
    dialogRef.afterClosed().subscribe(res =>{
     this.ngOnInit();
    })   
  }

delete(id:any){
  Swal.fire({
    title: 'Êtes-vous sûr ?',
    text: "Voulez-vous vraiment supprimer cette appartment ?",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#d33',
    confirmButtonText: 'Oui, supprimez-le!'
  }).then((result) => {
    if (result.isConfirmed) {
      this.ticketservice.deleteTicket(id).subscribe((res:any) =>{
        if (res.message){
          Swal.fire({
            icon: 'success',
            title: 'Success...',
            text: 'Supprimé avec succès !',
          })
          this.ngOnInit();
        }
        else{
          Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: "Quelque chose s'est mal passé!",
          })
        }
      },
      err =>{
        Swal.fire({
          icon: 'warning',
          title: 'La suppression a échoué!...',
          text: err.error.message,
        })
      }
      )
    }
    this.ngOnInit();
  }
  )
}
}

