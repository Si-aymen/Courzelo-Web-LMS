import { TicketServiceService } from './../Services/ticket-service.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { TickettypeService } from '../TicketTypeService/tickettype.service';
import { TicketType } from 'src/app/shared/models/TicketType';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-ticket',
  templateUrl: './add-ticket.component.html',
  styleUrls: ['./add-ticket.component.scss']
})
export class AddTicketComponent implements OnInit {
  formAddTicket: FormGroup;
  tickets = [];
  statuses = ['waiting', 'doing', 'done'];
  ticketForm: FormGroup = new FormGroup({});
  types:String[]=[];
  constructor(private fb: FormBuilder,private router:Router,private formBuilder: FormBuilder,private ticketservice:TicketServiceService,private typeservice:TickettypeService) { }

  ngOnInit(): void {
    this.createForm();
    this.ticketForm.get('type')?.valueChanges.subscribe(selectedValue => {
      console.log('Selected value:', selectedValue);
    });
    this.typeservice.getTypeList().subscribe((data: TicketType[]) => {
      // Extracting 'type' property from each element and storing in an array
this.types = data.map(item => item.type);
console.log(this.types);
  })
 }

  reclamationtypeValue() {
    return this.ticketForm.get('type')?.value;
  }
  createForm() {
    this.ticketForm = this.formBuilder.group({
      sujet: ['', Validators.required],
      details: ['', Validators.required],
      type:['',Validators.required]
    });
  }

  onSubmit(){
    // Specify all control values, or just the ones you want to change
//this.reclamationADD.
this.ticketForm.get('type')?.valueChanges.subscribe(selectedValue => {
console.log('Selected value:', selectedValue);
});
  console.log(this.ticketForm.value)
   console.log("-----------",  this.reclamationtypeValue())        
   this.ticketservice.addTicket(this.ticketForm.value).subscribe((res) =>{  
    console.log("heere",res)
    if (res){
      Swal.fire({
        icon: 'success',
        title: 'Success...',
        text: 'Ajouter avec succès !',
      })
      this.ngOnInit();
      console.log("success")
     // this.router.navigate(['tickets/list']); // Navigate to forward component
    }
    else{
      Swal.fire({
        icon: 'error',
        title: 'Success...',
        text: "Ajouter a échoué!",

      })
      console.log("faill")
    }
  },
  err =>{
    Swal.fire({
      icon: 'warning',
      title: 'La suppression a échoué!...',
      text: err.error.message,
    })
    console.log("allah a3lem")
  }
  )
//      this.reclamationservice.forwardToEmployee(res.id,this.token.getUser())
    }
    middle() {
      this.router.navigate(['tickets/list']); // Navigate to forward component
        }
}
