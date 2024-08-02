import { CalendarEvent, CalendarEventAction } from "angular-calendar";
import {startOfDay} from 'date-fns';

export class Event implements CalendarEvent{
    id?: string;
    title: string;
    start: Date;
    end: Date;
    projectId: string;  // Add this line
    color?: {
      primary: string;
      secondary: string;
    };
    actions?: CalendarEventAction[];
    notes: string;
    allDay?: boolean;
    cssClass?: string;
    resizable?: {
      beforeStart?: boolean;
      afterEnd?: boolean;
    };
    draggable?: boolean;
    meta?: {
      location: string,
      notes: string
    };
  
    constructor(data?) {
      data = data || {};
      this.start = new Date(data.start) || startOfDay(new Date());
      this.end = data.end ? new Date(data.end) : null;
      this.id = data.id ;
      this.title = data.title || '';
      this.color = {
        primary: data.color && data.color.primary || '#247ba0',
        secondary: data.color && data.color.secondary || '#D1E8FF'
      };
      this.draggable = data.draggable || true;
      this.resizable = {
        beforeStart: data.resizable && data.resizable.beforeStart || true,
        afterEnd: data.resizable && data.resizable.afterEnd || true
      };
      this.actions = data.actions || [];
      this.allDay = data.allDay || false;
      this.cssClass = data.cssClass || '';
      this.meta = {
        location: data.meta && data.meta.location || '',
        notes: data.meta && data.meta.notes || ''
      };
    }
  }
  