import { Component, Inject, OnInit, Optional } from '@angular/core';
import { TicketDataService } from 'src/app/views/tickets/Services/TicketService/ticketdata.service'; // Adjust the path as necessary
import { Observable } from 'rxjs';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { TrelloserviceService } from '../Services/trello/trelloservice.service'; // Adjust the path as necessary
import { environment } from 'src/environments/environment';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CardREQ } from 'src/app/shared/models/CardREQ';
import { TicketServiceService } from '../Services/TicketService/ticket-service.service';
import { Etat } from 'src/app/shared/models/Etat';
import Swal from 'sweetalert2';
import { Router, ActivatedRoute } from '@angular/router';
import { th } from 'date-fns/locale';

@Component({
  selector: 'app-forward',
  templateUrl: './forward.component.html',
  styleUrls: ['./forward.component.scss']
})
export class ForwardComponent implements OnInit {

  id: any; // Define id property
  sujet: any; // Define sujet property
  details: any; // Define details property
  type:any;
  CardREQ: CardREQ = { cardId: '', ticketID: '' }; // Initialize CardREQ with default values
  ticket:any;
  data = new FormGroup({
    developper: new FormControl('', Validators.required),
    activity: new FormControl('', Validators.required),

  });

  employee = ["touatiahmed", "ahmed_touati"];

  constructor(private router : Router,
    private ActivatedRoute:ActivatedRoute,
     private trelloService: TrelloserviceService,private ticketservice:TicketServiceService) { }

  ngOnInit(): void {
    this.ActivatedRoute.paramMap.subscribe(res=>{
      this.id=res.get('id');
      console.log(this.id)
    })
    this.ticketservice.getTicketById(this.id).subscribe((res)=>{
      this.ticket=res;
      this.type=res.type;
      this.sujet=res.sujet;
      this.details=res.details
      console.log('ID:', this.id);
        console.log('SUJET:', this.sujet);
        console.log('Description:', this.details);
    })
  }

  forward() {
    const member = this.data.value['developper'];
  
    const ticketId = this.id; 
    const ticketName = this.sujet;
    const ticketDetails = this.details; 
    const ticketType = this.type.type;
    const activity = String(this.data.value['activity']);

    var splitted = activity.split(".", activity.length);

    console.log('Forwarding Data:', member, ticketId, ticketName, ticketDetails, activity,"Le Type",ticketType);
   this.trelloService.getBoardByType(ticketType).subscribe((res:any)=>{
      console.log("le type ",res);
      this.trelloService.createCard(res.idListToDo,this.sujet,this.details).subscribe((card:any)=>{
        console.log("le card",card.id)
        this.CardREQ.cardId=card.id;
        this.CardREQ.ticketID=ticketId ;
        console.log(this.CardREQ)
        this.ticketservice.addCardTrello(this.CardREQ).subscribe(
          (response: any) => {
            console.log("Card added successfully:", response);
          },
          (error: any) => {
            console.error("Error adding card:", error);
          }
        );
        console.log("ajouter Card a DB Success")
        this.trelloService.addCheckList(card.id).subscribe((checklist:any)=>{
          console.log("le checklist",checklist)
          console.log("le member",member)
          this.trelloService.getTrelloUserId(member).subscribe((member:any)=>{
            console.log("le userid :",member.id,card.id)
            this.trelloService.addEmployeToCard(card.id,member.id).subscribe((res:any)=>{
         this.ticketservice.updateStatusdoing(ticketId, Etat.EN_COURS).subscribe((res:any) => {
          splitted.forEach(t=>{
            this.trelloService.addItemToCheckList(checklist.id,t).subscribe((res:any)=>{
              if (res) {
                Swal.fire({
                  icon: 'success',
                  title: 'Success...',
                  text: 'Transférer avec succès!',
                })
                this.router.navigate(['tickets/list']);
              }
              else {
                Swal.fire({
                  icon: 'success',
                  title: 'Success...',
                  text: 'Echec avec succès!',
                })
              }
            })
          })
              }
              )
            },err => {
              console.log('Trello username developper not found');    
            })
            
          })
        }
        )
      })

    })

  }
  middle() {
    this.router.navigate(['tickets/list']); // Navigate to forward component
      }
}
