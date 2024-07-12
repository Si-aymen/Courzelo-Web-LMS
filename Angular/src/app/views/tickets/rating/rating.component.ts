import { Component, OnInit } from '@angular/core';
import { TicketDataService } from '../ticketdata.service';
import { Observable } from 'rxjs';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { RatingserviceService } from '../Services/ratingservice.service';

@Component({
  selector: 'app-rating',
  templateUrl: './rating.component.html',
  styleUrls: ['./rating.component.scss']
})
export class RatingComponent implements OnInit {


  ticketData$: Observable<any>;
  id:any;
  data = new FormGroup({
    rating: new FormControl('', Validators.required),
    /*projet: new FormControl(''),
    client: new FormControl('')*/
  });

  constructor(private ticketDataService:TicketDataService,private ratingservice:RatingserviceService){

  }
  ngOnInit(): void {
    this.ticketData$ = this.ticketDataService.ticketData$;
    this.ticketData$.subscribe(data=>{
      if(data){
        console.log(data)
        this.id=data.id
      }
    })
  }
    forward() {
      this.ratingservice.addRating(this.id,this.data.value);
      console.log(this.ratingservice.addRating(this.id,this.data.value));
          }
      middle() {
      throw new Error('Method not implemented.');
      }

}
