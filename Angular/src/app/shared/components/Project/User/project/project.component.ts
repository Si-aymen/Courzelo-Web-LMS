import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.scss']
})
export class ProjectComponent {

  constructor(private router: Router) {}

  navigateToProjectDetails() {
    this.router.navigate(['/projectdetails']);
  }
}
