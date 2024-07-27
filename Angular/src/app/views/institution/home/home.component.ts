import {AfterViewInit, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {InstitutionService} from '../../../shared/services/institution/institution.service';
import {InstitutionResponse} from '../../../shared/models/institution/InstitutionResponse';
import * as L from 'leaflet';
import {ToastrService} from 'ngx-toastr';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  institutionID: string;
  constructor(
      private institutionService: InstitutionService,
      private route: ActivatedRoute,
      private toastr: ToastrService,
  ) { }
  imageSrc: any;
  currentInstitution: InstitutionResponse;
  private map: L.Map | undefined;
  private marker: L.Marker | undefined;
  ngOnInit() {
    this.institutionID = this.route.snapshot.paramMap.get('institutionID');
    this.institutionService.getInstitutionByID(this.institutionID).subscribe(
        response => {
          this.currentInstitution = response;
        }
    );

  }

  onTabChange(event: any) {
    if (event.nextId === 'mapTab') {
      setTimeout(() => {
        this.initializeMap();
      }, 0);
    }
  }
  initializeMap() {
    if (this.currentInstitution.latitude === 0 || this.currentInstitution.longitude === 0 ||
        this.currentInstitution.latitude === undefined || this.currentInstitution.longitude === undefined) {
      this.toastr.warning('You Don\'t have a location set, setting default location.');
      this.currentInstitution.latitude = 36.7832;
      this.currentInstitution.longitude = 10.1843;
    }
    if (this.map) {
      this.map.remove();
    }
    this.map = L.map('map').setView([this.currentInstitution.latitude, this.currentInstitution.longitude], 15);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {}).addTo(this.map);
     L.marker([this.currentInstitution.latitude, this.currentInstitution.longitude], {
      draggable: false
    }).addTo(this.map);
  }
}
