import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CalendarEvent, CalendarEventAction, CalendarEventTimesChangedEvent } from 'angular-calendar';
import { isSameDay, isSameMonth } from 'date-fns';
import { Subject } from 'rxjs';
import { SharedAnimations } from 'src/app/shared/animations/shared-animations';
import { CalendarAppEvent } from 'src/app/shared/models/calendar-event.model';
import { Event as ProjectEvent } from 'src/app/shared/models/Project/Event'; // Renamed to avoid conflict with CalendarEvent
import { Project } from 'src/app/shared/models/Project/Project';
import { EventService } from 'src/app/shared/services/Project/event.service';
import { ProjectService } from 'src/app/shared/services/Project/project.service';
import { Utils } from 'src/app/shared/utils';
import { CalendarFormProjectComponent } from '../calendar-form-project/calendar-form-project.component';

@Component({
  selector: 'app-project-calendar',
  templateUrl: './project-calendar.component.html',
  styleUrls: ['./project-calendar.component.scss'],
  animations: [SharedAnimations]
})
export class ProjectCalendarComponent implements OnInit {
  public view = 'month';
  public viewDate = new Date();
  @ViewChild('eventDeleteConfirm', { static: true }) eventDeleteConfirm;

  public activeDayIsOpen = true;
  public refresh: Subject<any> = new Subject();
  public events: CalendarEvent[] = []; // Use CalendarEvent type
  private actions: CalendarEventAction[];
  private projectId: string; // To store the project ID
  public project: Project; // To store the project details

  constructor(
    private modalService: NgbModal,
    private projectService: ProjectService,
    private route: ActivatedRoute,
    private eventService: EventService,
  ) {
    this.actions = [
      {
        label: '<i class="i-Edit m-1 text-secondary"></i>',
        onClick: ({ event }: { event: CalendarEvent }): void => { // Changed type to CalendarEvent
          this.handleEvent('edit', event);
        }
      },
      {
        label: '<i class="i-Close m-1 text-danger"></i>',
        onClick: ({ event }: { event: CalendarEvent }): void => { // Changed type to CalendarEvent
          this.removeEvent(event);
        }
      }
    ];
  }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.projectId = params.get('id');
      if (this.projectId) {
        this.loadProjectDetails();
      }
    });
  }

  private loadProjectDetails() {
    if (!this.projectId) {
      console.error('No project ID available for fetching project details');
      return;
    }

    // Fetch project details
    this.projectService.getProjectById(this.projectId).subscribe((project: Project) => {
      this.project = project;

      // Fetch events related to the project
      this.eventService.getEventsByProjectId(this.projectId).subscribe(
        (events: ProjectEvent[]) => { // Changed type to ProjectEvent
          // Combine project details and events
          const calendarEvents = this.createCalendarEventsFromProject(project);
          this.events = [...calendarEvents, ...this.transformEventsToCalendarEvents(events)];
          this.refresh.next();
        },
        (error) => {
          console.error('Error fetching events:', error);
        }
      );
    }, (error) => {
      console.error('Error fetching project details:', error);
    });
  }

  private transformEventsToCalendarEvents(events: ProjectEvent[]): CalendarEvent[] { // Changed return type to CalendarEvent[]
    return events.map(event => ({
      start: new Date(event.start),
      end: new Date(event.end),
      title: event.title,
      color: event.color,
      actions: this.actions,
      allDay: event.allDay,
      cssClass: event.cssClass,
      resizable: event.resizable,
      draggable: event.draggable,
      meta: event.meta
    }));
  }

  private createCalendarEventsFromProject(project: Project): CalendarEvent[] { // Changed return type to CalendarEvent[]
    return [
      {
        id: '', // Provide an appropriate id or leave empty if it’s a dummy event
        start: new Date(project.datedebut),
        end: new Date(project.deadline),
        title: project.name,
        color: { primary: '#1e90ff', secondary: '#D1E8FF' },
        actions: this.actions,
        allDay: true,
        draggable: false,
        resizable: {
          beforeStart: false,
          afterEnd: false
        },
        meta: {
          projectId: project.id.toString(),
          notes: ''
        }
      }
    ];
  }

  public removeEvent(event: CalendarEvent): void { // Changed type to CalendarEvent
    this.modalService.open(this.eventDeleteConfirm, { ariaLabelledBy: 'modal-basic-title', centered: true })
      .result.then((result) => {
        if (result === 'Ok') {
          console.log('Attempting to delete event with ID:', event.id);
          if (!event.id) {
            console.error('No event ID provided');
            return;
          }
          this.eventService.deleteEvent(event.id.toString()).subscribe(
            () => {
              this.events = this.events.filter(e => e.id !== event.id);
              this.refresh.next();
              console.log('Event deleted and events updated:', this.events);
            },
            (error) => {
              console.error('Error deleting event:', error);
            }
          );
        }
      }, (reason) => {
        console.log('Delete dismissed:', reason);
      });
  }

  public addEvent() {
    if (!this.projectId) {
      console.error('No project ID available for adding event');
      return;
    }

    const dialogRef = this.modalService.open(CalendarFormProjectComponent, { centered: true });
    dialogRef.componentInstance.data = {
      action: 'add',
      date: new Date()
    };

    dialogRef.result
      .then((res) => {
        if (!res) {
          return;
        }
        const responseEvent = res.event;
        responseEvent.start = Utils.ngbDateToDate(responseEvent.start);
        responseEvent.end = Utils.ngbDateToDate(responseEvent.end);

        console.log('Adding event:', responseEvent);

        this.eventService.createEvent(this.projectId, responseEvent).subscribe(
          (createdEvent) => {
            this.events.push({
              ...createdEvent,
              actions: this.actions,
              color: { primary: '#1e90ff', secondary: '#D1E8FF' }
            });
            this.refresh.next();
          },
          (error) => {
            console.error('Error creating event:', error);
          }
        );
      })
      .catch((e) => {
        console.log('Dialog result:', e);
      });
  }

  public handleEvent(action: string, event: CalendarEvent): void { // Changed type to CalendarEvent
    console.log('Handling event:', event); // Log event details
  
    // Open the modal for editing or viewing event details
    const dialogRef = this.modalService.open(CalendarFormProjectComponent, { centered: true });
    dialogRef.componentInstance.data = { event, action };
  
    dialogRef.result
      .then(res => {
        if (!res) {
          return;
        }
  
        const dialogAction = res.action;
        const responseEvent = res.event;
        responseEvent.start = Utils.ngbDateToDate(responseEvent.start);
        responseEvent.end = Utils.ngbDateToDate(responseEvent.end);
  
        console.log('Event after modal close:', responseEvent); // Log updated event
  
        if (dialogAction === 'save') {
          if (responseEvent.id) {
            this.updateEvent(responseEvent);
          } else {
            console.error('No event ID provided for update');
          }
        } else if (dialogAction === 'delete') {
          if (event.id) {
            this.removeEvent(event);
          } else {
            console.error('No event ID provided for delete');
          }
        }
      })
      .catch(e => {
        console.log(e);
      });
  }
  

  //function dosent work

  private updateEvent(event: CalendarEvent): void { // Changed type to CalendarEvent
    if (!event.id) {
      console.error('No event ID provided for update');
      return;
    }
  
    console.log('Updating event:', event); // Log event being updated
    this.eventService.updateEvent(event.id.toString(), event).subscribe(
      (updatedEvent) => {
        const index = this.events.findIndex(e => e.id === event.id);
        if (index !== -1) {
          this.events[index] = {
            ...updatedEvent,
            actions: this.actions,
            color: { primary: '#1e90ff', secondary: '#D1E8FF' }
          };
          this.refresh.next();
          console.log('Event updated successfully:', this.events);
        } else {
          console.error('Event not found in events array');
        }
      },
      (error) => {
        console.error('Error updating event:', error);
      }
    );
  }

  public dayClicked({ date, events }: { date: Date, events: CalendarEvent[] }): void { // Changed type to CalendarEvent[]
    if (isSameMonth(date, this.viewDate)) {
      if (
        (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
        events.length === 0
      ) {
        this.activeDayIsOpen = false;
      } else {
        this.activeDayIsOpen = true;
        this.viewDate = date;
      }
    }
  }

  public eventTimesChanged({ event, newStart, newEnd }: CalendarEventTimesChangedEvent): void {
    event.start = newStart;
    event.end = newEnd;
    // Handle event time change if necessary
  }
}
