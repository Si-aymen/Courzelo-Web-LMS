import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-projectdetails',
  templateUrl: './projectdetails.component.html',
  styleUrls: ['./projectdetails.component.scss']
})
export class ProjectdetailsComponent {

  constructor(private router: Router) {}
 chat() {
    this.router.navigate(['/chat']);
  }
  calendar() {
    this.router.navigate(['/calendar']);
  }

  ProgressDashboard() {
    this.router.navigate(['/ProgressDashboard']);
  }
}
