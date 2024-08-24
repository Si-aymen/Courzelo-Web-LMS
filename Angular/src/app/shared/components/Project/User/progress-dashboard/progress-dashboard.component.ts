import { Component } from '@angular/core';

@Component({
  selector: 'app-progress-dashboard',
  templateUrl: './progress-dashboard.component.html',
  styleUrls: ['./progress-dashboard.component.scss']
})
export class ProgressDashboardComponent {
 // Define task arrays or properties as needed
 todoTasks$ = [];
 inProgressTasks$ = [];
 doneTasks$ = [];

 constructor() {}

 // Method to handle task dropping
 onTaskDropped(event: any, status: string) {
   // Implement your logic here to update task status
   console.log('Task dropped with status:', status);
   // Example logic: update tasks based on status
   if (status === 'InProgress') {
     // Move task from Todo to InProgress
   } else if (status === 'Done') {
     // Move task from InProgress to Done
   }
 }

 calculateProgress() {
   // Implement your progress calculation logic
   return 50; // Example: return a calculated percentage
 }

}

