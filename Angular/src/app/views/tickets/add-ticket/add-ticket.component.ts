import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-add-ticket',
  templateUrl: './add-ticket.component.html',
  styleUrls: ['./add-ticket.component.scss']
})
export class AddTicketComponent implements OnInit {
  formAddTicket: FormGroup;
  tickets = [];
  statuses = ['waiting', 'doing', 'done'];

  constructor(private fb: FormBuilder) { }

  ngOnInit(): void {
    this.formAddTicket = this.fb.group({
      id: ['', Validators.required],
      type: ['', Validators.required],
      sujet: ['', Validators.required],
      description: ['', Validators.required],
      date: ['', Validators.required],
      status: ['', Validators.required]
    });
  }

  addTicket(): void {
    if (this.formAddTicket.valid) {
      this.tickets.push(this.formAddTicket.value);
      this.formAddTicket.reset();
    }
  }
}
