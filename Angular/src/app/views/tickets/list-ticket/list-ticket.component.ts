import { TrelloserviceService } from './../trello/trelloservice.service';
import { TrelloBoard } from './../../../shared/models/TrelloBoard';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { TicketDataService } from 'src/app/views/tickets/ticketdata.service'; // Adjust the path as necessary
import { TicketServiceService } from '../Services/ticket-service.service';
import { Ticket } from 'src/app/shared/models/Ticket';
import { catchError, tap } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { UpdateTicketComponent } from '../update-ticket/update-ticket.component';
import { ForwardComponent } from '../forward/forward.component';
import Swal from 'sweetalert2';
import { TickettypeService } from '../TicketTypeService/tickettype.service';

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
  constructor (private router : Router,private typeservice:TickettypeService,private trelloservice:TrelloserviceService,
    private ticketservice:TicketServiceService,private ticketDataService:TicketDataService,public dialog: MatDialog){}

  ngOnInit(): void {
    this.getTickets();
    this.changeStatus();
  }
 changeStatus(): void {
  this.typeservice.getTypeList().subscribe(
    (typeList: any[]) => {
      console.log("Type List:", typeList);
      typeList.forEach(item => {
        console.log("Type Item:", item.type);
        this.typeservice.getTrelloBoard(item.type).subscribe(
          (boardResponse: any) => {
            const trelloBoard: TrelloBoard = {
              idBoard: boardResponse.id,
              idListToDo: boardResponse.idListToDo,
              idListDoing: boardResponse.idListDoing,
              idListDone: boardResponse.idListDone,
              type: boardResponse.type.type
            };
            this.trelloBoardList.push(trelloBoard);
            console.log("Trello Board Added:", trelloBoard);
            this.trelloservice.getCardList("668f38b26047ca5e0499c7a3").subscribe(
              (cardListResponse: any) => {
                console.log("Card List Response:", cardListResponse);
                cardListResponse.forEach(card => {
                  console.log("Card ID:", card.id);
                });
              },
              (error) => {
                console.error('Error fetching Card List:', error);
              }
            );
          },
          (error) => {
            console.error('Error fetching Trello board:', error);
          }
        );
      });
    },
    (error) => {
      console.error('Error fetching type list:', error);
    }
  );
  console.log("Trello Board List at End of Function:", this.trelloBoardList);
}
 getTickets(): void {
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

  update(id:any){
    const dialogRef = this.dialog.open(UpdateTicketComponent,{
      width : "40%",
      height: "80%",
      data: { ticket:id}
    });
    dialogRef.afterClosed().subscribe(res =>{
     this.ngOnInit();
    })   
  }

  Affecter(id: any) {
    const dialogRef = this.dialog.open(ForwardComponent,{
      width : "50%",
      height: "100%",
      data: { Ticket:id}
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
