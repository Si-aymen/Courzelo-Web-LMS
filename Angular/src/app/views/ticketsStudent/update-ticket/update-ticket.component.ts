import { Component, Inject, OnInit, Optional } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Ticket } from 'src/app/shared/models/Ticket';
import { TicketType } from 'src/app/shared/models/TicketType';
import Swal from 'sweetalert2';
import { TicketServiceService } from '../../tickets/Services/TicketService/ticket-service.service';
import { TicketDataService } from '../../tickets/Services/TicketService/ticketdata.service';
import { TickettypeService } from '../../tickets/Services/TicketTypeService/tickettype.service';

@Component({
  selector: 'app-update-ticket',
  templateUrl: './update-ticket.component.html',
  styleUrls: ['./update-ticket.component.scss']
})
export class UpdateTicketComponent implements OnInit{
  onClose() {
    this.dialogRef.close();
  }
  
    ticketForm:FormGroup= new FormGroup({});
    employee = ["touatiahmed","ahmed_touati"];
    ticketDetails!:Ticket;
    id: any;
    sujet: string ="";
    details: string ="";
    types:any;
    date:Date;
    status:any;
    ticketData$: Observable<any>;
  
  
    constructor(public dialogRef: MatDialogRef<UpdateTicketComponent>,private router : Router,private typeticketervice:TickettypeService,private ticketDataService: TicketDataService,private typeservice:TickettypeService
    ,private ticketservice:TicketServiceService,
    private formBuilder: FormBuilder,
    @Optional() @Inject(MAT_DIALOG_DATA) public ticket: any
    ){this.id=ticket.ticket}
  
  
    ngOnInit(): void {
      console.log(this.id);
      this.createForm();
      this.typeticketervice.getTypeList().subscribe((data: TicketType[]) => {
        // Extracting 'type' property from each element and storing in an array
  this.types = data.map(item => item.type);
  console.log(this.types); // This will log the array of 'type' values
  // Assign the data (array of Typereclamation) to this.type
    });
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
      this.ticketservice.getTicketById(this.id).subscribe((res:any)=>{
        console.log(res);
        this.ticketForm.patchValue({
          id: res.id,
          sujet: res.sujet,
          details: res.details,
        });
      })
  
    }
    createForm() {
      this.ticketForm = this.formBuilder.group({
        sujet: [''],
        details: [''],
      });
    }
    onSubmit() {
      Swal.fire({
        title: 'Are you sure?',
        text: 'Do you want to update the ticket with the provided details?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, update it!',
        cancelButtonText: 'Cancel'
      }).then((result) => {
        if (result.isConfirmed) {
          // If confirmed, proceed with the update
          this.ticketservice.updateTicket(this.id, this.ticketForm.value).subscribe((res: any) => {
            if (res) {
              Swal.fire({
                icon: 'success',
                title: 'Success...',
                text: 'Ticket updated successfully!',
              });
              this.onClose(); // Close the dialog if needed
            }
          }, error => {
            Swal.fire({
              icon: 'error',
              title: 'Oops...',
              text: 'Something went wrong!',
            });
          });
        } else {
          // Optionally handle the case where the user cancels the update
          console.log('Update canceled');
        }
      });
    }
  
  }
  
