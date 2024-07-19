import { TickettypeService } from '../../Services/TicketTypeService/tickettype.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { TrelloserviceService } from '../../Services/trello/trelloservice.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-tickettype',
  templateUrl: './tickettype.component.html',
  styleUrls: ['./tickettype.component.scss']
})
export class TickettypeComponent implements OnInit {
  data: FormGroup = new FormGroup({});
  TrelloData = new FormGroup({
    idBoard: new FormControl('', Validators.required),
    idListToDo: new FormControl('', Validators.required),
    idListDoing: new FormControl('', Validators.required),
    idListDone: new FormControl('', Validators.required),
    type: new FormControl('', Validators.required)
  });

  constructor(private router:Router,
    private trelloService: TrelloserviceService,
    private formBuilder: FormBuilder,
    private tickettypeService: TickettypeService
  ) { }

  ngOnInit(): void {
    this.createForm();
  }

  createForm() {
    this.data = this.formBuilder.group({
      type: ['', Validators.required],
    });
  }

  add() {
    this.tickettypeService.addType(this.data.value).toPromise().then((type: any) => {
      console.log("letype",type)
      if (type.id) {
        console.log("name board", type.type);
        this.trelloService.addBoard(type.type).subscribe((board: any) => {
          console.log("LE BOARD DETAILS:",board)
          this.trelloService.getBoardList(board.id).subscribe((res: any) => {
            res.forEach((r: any) => {
              switch (r.name) {
                case "To Do":
                  this.TrelloData.patchValue({ idListToDo: r.id });
                  break;
                case "Doing":
                  this.TrelloData.patchValue({ idListDoing: r.id });
                  break;
                case "Done":
                  this.TrelloData.patchValue({ idListDone: r.id });
                  break;
                default:
                  break;
              }
            });
            this.TrelloData.patchValue({ idBoard: board.id });
            this.TrelloData.patchValue({ type: type.type });
            console.log(this.TrelloData.value);
            this.tickettypeService.addTrello(this.TrelloData.value).toPromise().then((res: any) => {
              if (res.id) {
                Swal.fire({
                  icon: 'success',
                  title: 'Success...',
                  text: 'Ajouté avec succès !',
                });
                this.data.reset();
                //this.router.navigate(['tickets/list']); // Navigate to forward component
              }
            }).catch((err: any) => {
              if (err.error && err.error.message) {
                Swal.fire({
                  icon: 'error',
                  title: 'Oops...',
                  text: err.error.message,
                });
              } else {
                console.error('Unexpected error:', err);
                Swal.fire({
                  icon: 'error',
                  title: 'Oops...',
                  text: 'Quelque chose s\'est mal passé!',
                });
              }
            });
          });
        });
      } else {
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: "Quelque chose s'est mal passé!",
        });
      }
    }).catch((err: any) => {
      if (err.error && err.error.message) {
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: err.error.message,
        });
      } else {
        console.error('Unexpected error:', err);
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'Quelque chose s\'est mal passé!',
        });
      }
    });
  }

  processList(listItem: any) {
    if (listItem.name === "To Do") {
      this.TrelloData.patchValue({ idListToDo: listItem.id });
    }
    // ... similar logic for Doing and Done lists
  }
  middle() {
    this.router.navigate(['tickets/list']); // Navigate to forward component
      }
}
