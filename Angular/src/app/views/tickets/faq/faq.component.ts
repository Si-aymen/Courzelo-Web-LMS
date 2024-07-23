import { Component, OnInit } from '@angular/core';
import { FAQ } from 'src/app/shared/models/faq';
import { FAQService } from '../Services/FaqService/faq.service';
import { UpdatefaqComponent } from './updatefaq/updatefaq.component';
import Swal from 'sweetalert2';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-faq',
  templateUrl: './faq.component.html',
  styleUrls: ['./faq.component.scss']
})
export class FaqComponent implements OnInit {
  faqs: FAQ[];

  constructor(private faqService: FAQService,
    public dialog: MatDialog
    ) { }

  ngOnInit(): void {
    this.loadFAQs();
  }

  loadFAQs() {
    this.faqService.getAllFAQs().subscribe(data => {
      this.faqs = data;
    });
  }
  editFAQ(id:any){
    const dialogRef = this.dialog.open(UpdatefaqComponent,{
      width : "40%",
      height: "80%",
      data: { faq:id}
    });
    dialogRef.afterClosed().subscribe(res =>{
     this.ngOnInit();
    })   
  }
  deleteFAQ(id:any){
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
        this.faqService.deleteFAQ(id).subscribe((res:any) =>{
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