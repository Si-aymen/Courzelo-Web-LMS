import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CalendarComponent } from './calendar/calendar.component';
import { ProjectCalendarComponent } from 'src/app/shared/components/Project/User/project-calendar/project-calendar.component';

const routes: Routes = [
  {
    path: '',
    component: CalendarComponent ,
  },
 
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CalendarRoutingModule { }
