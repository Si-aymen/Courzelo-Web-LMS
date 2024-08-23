import { Component, OnInit } from '@angular/core';
import { CalendarEvent } from 'angular-calendar';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Utils } from 'src/app/shared/utils';
import { Event } from 'src/app/shared/models/Project/Event';
interface DialogData {
  event?: Event;
  action?: string;
  date?: Date;
}
@Component({
  selector: 'app-calendar-form-project',
  templateUrl: './calendar-form-project.component.html',
  styleUrls: ['./calendar-form-project.component.scss']
})

export class CalendarFormProjectComponent  implements OnInit{

  data: DialogData;
  event: Event;
  dialogTitle: string;
  eventForm: FormGroup;
  action: string;
  constructor(
    public activeModal: NgbActiveModal,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit() {
    setTimeout(() => {
      if (this.action === 'edit') {
        this.dialogTitle = this.event.title;
      } else {
        this.dialogTitle = 'Add Event';
        this.event = new Event(this.data.event);
      }
      this.eventForm = this.buildEventForm(this.event);

    }, 100);
    this.eventForm = this.buildEventForm(this.event);

  }
  buildEventForm(event: Event = new Event()): FormGroup {
    return new FormGroup({
      id: new FormControl(event.id),
      title: new FormControl(event.title, Validators.required),
      start: new FormControl(Utils.dateToNgbDate(event.start), Validators.required),
      end: new FormControl(Utils.dateToNgbDate(event.end)),
   
      allDay: new FormControl(event.allDay),
      color: this.formBuilder.group({
        primary: new FormControl(event.color?.primary),
        secondary: new FormControl(event.color?.secondary)
      }),
      actions: new FormControl(event.actions),  // Add this line if actions need to be part of the form
      notes: new FormControl(event.notes),  // Assuming notes is a direct property of Event
      meta: this.formBuilder.group({
        location: new FormControl(event.meta?.location),
        notes: new FormControl(event.meta?.notes)
      }),
      cssClass: new FormControl(event.cssClass),  // Add this line if cssClass needs to be part of the form
      draggable: new FormControl(event.draggable),  // Add this line if draggable needs to be part of the form
      resizable: this.formBuilder.group({
        beforeStart: new FormControl(event.resizable?.beforeStart),
        afterEnd: new FormControl(event.resizable?.afterEnd)
      })
    });
  }
}   
