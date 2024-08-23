import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-apply',
  templateUrl: './apply.component.html',
  styleUrls: ['./apply.component.scss']
})
export class ApplyComponent implements OnInit {

  Id: string;
  constructor(
    private router:Router,
    private activateRoute:ActivatedRoute
  ){}
ngOnInit(): void {
  this.Id = this.activateRoute.snapshot.params.id;
}
}
