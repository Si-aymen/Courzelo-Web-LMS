import { Component, OnInit } from '@angular/core';
import { TicketDataService } from '../../tickets/Services/TicketService/ticketdata.service';
import { Observable } from 'rxjs';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { RatingserviceService } from '../../tickets/Services/Rating/ratingservice.service';
import Swal from 'sweetalert2';
import { Router, ActivatedRoute } from '@angular/router';
import { TicketServiceService } from '../../tickets/Services/TicketService/ticket-service.service';

@Component({
  selector: 'app-rating',
  templateUrl: './rating.component.html',
  styleUrls: ['./rating.component.scss']
})
export class RatingComponent implements OnInit {

  Ticket:any;
  id:any;
  data = new FormGroup({
    rating: new FormControl('', Validators.required),
    /*projet: new FormControl(''),
    client: new FormControl('')*/
  });
  averageRating: number;

  constructor(
    private ActivatedRoute:ActivatedRoute,
    private ticketService:TicketServiceService,
    private ratingservice:RatingserviceService,
    private router:Router){
    this.data = new FormGroup({
      rating: new FormControl(null, [Validators.required, Validators.min(0), Validators.max(5),Validators.pattern(/^\d+(\.\d+)?$/)])
    });
  }
  ngOnInit(): void {
    this.ActivatedRoute.paramMap.subscribe(params => {
      this.id = params.get('id');
      console.log('Received post ID:', this.id);
      // Use the postId as needed
    });
    this.getTicket();
    this.getAverageRating();
  }

  getTicket(){
    this.ticketService.getTicketById(this.id).subscribe((res)=>{
      this.Ticket=res;
      console.log(res);
        })
  }
    middle() {
      this.router.navigate(['ticketsStudent/list']);      ;
      }
      forward(): void {
        this.ratingservice.getRatings().subscribe((res: any[]) => {
            if (!res || res.length === 0) {
                // No ratings found, proceed to add new rating
                this.addRating();
            } else {
                let ratingExists = false;
                res.forEach((rating: any) => {
                    if (rating.ticket.id === this.id) {
                        ratingExists = true;
                        Swal.fire({
                            title: 'Already got a rating',
                            text: "Do you want to update it?",
                            icon: 'warning',
                            showCancelButton: true,
                            confirmButtonColor: '#3085d6',
                            cancelButtonColor: '#d33',
                            confirmButtonText: 'Yes, Update!'
                        }).then((result) => {
                            if (result.isConfirmed) {
                                const ratingValue = parseFloat(this.data.value.rating);
                                this.ratingservice.updateRating(rating.id, { rating: ratingValue }).subscribe(
                                    (updatedRating: any) => {
                                        Swal.fire({
                                            icon: 'success',
                                            title: 'Success...',
                                            text: 'Updated successfully!',
                                        }).then(() => {
                                          this.router.navigate(['ticketsStudent/list']);      ;
                                         });
                                    },
                                    (err: any) => {
                                        const errorMessage = err.error?.message || 'An unexpected error occurred.';
                                        Swal.fire({
                                            icon: 'error',
                                            title: 'Update Failed',
                                            text: errorMessage,
                                        });
                                    }
                                );
                            }
                        });
                    }
                });
                // Add a new rating if no existing rating matches the id
                if (!ratingExists) {
                    this.addRating();
                }
            }
        });
    }
    
    addRating(): void {
        this.ratingservice.addRating(this.id, this.data.value).subscribe(
            (res: any) => {
                Swal.fire({
                    icon: 'success',
                    title: 'Success...',
                    text: 'Rating added successfully!',
                });
                this.router.navigate(['ticketsStudent/list']);      ;
            },
            (err: any) => {
                Swal.fire({
                    icon: 'error',
                    title: 'Failed...',
                    text: err.error?.message || 'An unexpected error occurred.',
                });
            }
        );
    }
    
    getAverageRating(): void {
      this.ratingservice.getRatings().subscribe((res: any[]) => {
        this.averageRating = this.calculateAverageRating(res);
      });
    }
    // Method to calculate the average rating
calculateAverageRating(ratings: any[]): number {
  if (!ratings || ratings.length === 0) {
      return 0; // No ratings available, return 0 or handle accordingly
  }

  const total = ratings.reduce((sum, rating) => sum + parseFloat(rating.rating), 0);
  return total / ratings.length;
}


}
