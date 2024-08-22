import { TickettypeService } from '../../Services/TicketTypeService/tickettype.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import Swal from 'sweetalert2';
import { UpdateTypeComponent } from '../update-type/update-type.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit{
  types$: Observable<any[]>;
  constructor(private router : Router,
    private tickettypeService:TickettypeService,
    public dialog:MatDialog){ }

  ngOnInit(): void {
    this.getTickets();
  }
  getTickets(): void {
    this.types$ = this.tickettypeService.getTypeList().pipe(
      tap(data => console.log('Fetched tickets:', data)), 
      tap(data=>console.log(data)),// Log the data to the console
      catchError(err => {
        console.error('Error loading tickets', err);
        return of([]); // Return an empty array on error
      })
    );
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
        this.tickettypeService.deleteTicketType(id).subscribe((res:any) =>{
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
  
  update(id:any){
    const dialogRef = this.dialog.open(UpdateTypeComponent,{
      width : "10%",
      height: "10%",
      data: { type:id}
    });
    dialogRef.afterClosed().subscribe(res =>{
     this.ngOnInit();
    })   
  }
}
