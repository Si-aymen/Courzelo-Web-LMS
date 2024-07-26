import {Component, OnInit} from '@angular/core';
import {InstitutionRequest} from '../../../shared/models/institution/InstitutionRequest';
import {InstitutionService} from '../../../shared/services/institution/institution.service';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';
import {FormBuilder, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {ActivatedRoute} from '@angular/router';
import {UserService} from '../../../shared/services/user/user.service';
import * as L from 'leaflet';
import {InstitutionMapRequest} from '../../../shared/models/institution/InstitutionMapRequest';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.scss']
})
export class EditComponent implements OnInit {
    institutionID: string;
    currentInstitution;
    institutionMapRequest: InstitutionMapRequest ={};
    loading = false;
    countries = [];
    private map: L.Map | undefined;
    private marker: L.Marker | undefined;
    latitude = 0;
    longitude = 0;
    constructor(
      private institutionService: InstitutionService,
      private handleResponse: ResponseHandlerService,
      private formBuilder: FormBuilder,
      private toastr: ToastrService,
      private route: ActivatedRoute,
        private userService: UserService
  ) { }
  updateInstitutionForm = this.formBuilder.group({
        name: ['', [Validators.required, Validators.maxLength(40), Validators.minLength(3)]],
        slogan: ['', [Validators.required, Validators.maxLength(50), Validators.minLength(3)]],
        country: ['', [Validators.required]],
        address: ['', [Validators.required, Validators.maxLength(100), Validators.minLength(10)]],
        description: ['', [Validators.required, Validators.maxLength(500), Validators.minLength(10)]],
        website: ['', [Validators.required, Validators.maxLength(100), Validators.minLength(5)]],
      }
  );
  ngOnInit() {
      this.institutionID = this.route.snapshot.paramMap.get('institutionID');
      this.userService.getCountries().subscribe(
          countries => {
              this.countries = countries;
              console.log(this.countries);
          }
      );
      this.institutionService.getInstitutionByID(this.institutionID).subscribe(
            response => {
                this.currentInstitution = response;
                this.updateInstitutionForm.patchValue(
                    {
                        name: this.currentInstitution.name,
                        slogan: this.currentInstitution.slogan,
                        country: this.currentInstitution.country,
                        address: this.currentInstitution.address,
                        description: this.currentInstitution.description,
                        website: this.currentInstitution.website
                    }
                );
                this.setLocation();
            }, error => {
                this.handleResponse.handleError(error);
            }
      );
  }
  updateInstitution(id: string) {
    if (this.updateInstitutionForm.valid) {
      const institution: InstitutionRequest = this.updateInstitutionForm.value;
      this.institutionService.updateInstitution(id, institution).subscribe(
          response => {
            this.toastr.success('Institution updated successfully');
          }, error => {
            this.handleResponse.handleError(error);
          }
      );
    } else {
      this.toastr.error('Please fill all fields');
    }
  }
    shouldShowError(controlName: string, errorName: string): boolean {
        const control = this.updateInstitutionForm.get(controlName);
        return control && control.errors && control.errors[errorName] && (control.dirty || control.touched);
    }
    setLocation() {
        if (this.map) {
            this.map.remove();
        }
        if (this.currentInstitution.latitude === 0 || this.currentInstitution.longitude === 0 ||
            this.currentInstitution.latitude === undefined || this.currentInstitution.longitude === undefined) {
            this.toastr.warning('You Don\'t have a location set, setting default location.');
            this.currentInstitution.latitude = 36.7832;
            this.currentInstitution.longitude = 10.1843;
        }
        this.map = L.map('map').setView([this.currentInstitution.latitude, this.currentInstitution.longitude], 15);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            // ...
        }).addTo(this.map);
        // Add a marker to the map that the user can drag to set a new location
        const marker = L.marker([this.currentInstitution.latitude, this.currentInstitution.longitude], {
            draggable: true
        }).addTo(this.map);
        // Update the institution's latitude and longitude when the marker is dragged
        marker.on('dragend', () => {
            const position = marker.getLatLng();
            this.latitude = position.lat;
            this.longitude = position.lng;
            console.log('Marker latitude', this.latitude);
            console.log('Marker longitude', this.longitude);
        });
    }
    saveLocation() {
        this.institutionMapRequest.latitude = this.latitude;
        this.institutionMapRequest.longitude = this.longitude;
        console.log('Institution Map Request', this.institutionMapRequest);
        this.institutionService.setInstitutionMap(this.institutionID, this.institutionMapRequest).subscribe(
            response => {
                console.log('Location saved successfully.', this.institutionMapRequest);
                this.toastr.success('Location saved successfully.');
            },
            error => {
                this.toastr.error('Error saving location.');
            }
        );
    }
}
