import { TickettypeService } from './../TicketTypeService/tickettype.service';
import { Component, Inject, OnInit, Optional } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ForwardComponent } from '../forward/forward.component';
import { FormGroup, FormBuilder } from '@angular/forms';
import { TicketServiceService } from '../Services/ticket-service.service';
import { Ticket } from 'src/app/shared/models/Ticket';
import { TicketType } from 'src/app/shared/models/TicketType';
import { Router } from '@angular/router';
import { TicketDataService } from '../ticketdata.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-update-ticket',
  templateUrl: './update-ticket.component.html',
  styleUrls: ['./update-ticket.component.scss']
})
export class UpdateTicketComponent implements OnInit{

  ticketForm:FormGroup= new FormGroup({});
  employee = ["touatiahmed","ahmed_touati"];
  ticketDetails!:Ticket;
  id: any;
  sujet: string ="";
  details: string ="";
  typerec:string ="";
  types:any;
  ticketData$: Observable<any>;


  constructor(private router : Router,private typeticketervice:TickettypeService,private ticketDataService: TicketDataService,private typeservice:TickettypeService
  ,private ticketservice:TicketServiceService,private formBuilder: FormBuilder
  ){}


  ngOnInit(): void {
    this.createForm();
    //const id = this.route.snapshot.params.id;
    this.ticketData$ = this.ticketDataService.ticketData$;
    this.ticketData$.subscribe(data => {
      if (data) {
        console.log(data)
        this.id = data.id;
        this.sujet = data.sujet;
        this.details = data.details;
        console.log('ID:', this.id);
        console.log('SUJET:', this.sujet);
        console.log('Description:', this.details);
      }
    });
    this.ticketForm.setValue({
      id: this.id,
      sujet: this.sujet,
      details: this.details,
    });
  }
  createForm() {
    this.ticketForm = this.formBuilder.group({
      id:[],
      sujet: [''],
      details: [''],
    });
  }
  onSubmit() {
    throw new Error('Method not implemented.');
    }

}
