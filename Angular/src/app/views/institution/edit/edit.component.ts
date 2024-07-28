import {Component, OnInit} from '@angular/core';
import {InstitutionRequest} from '../../../shared/models/institution/InstitutionRequest';
import {InstitutionService} from '../../../shared/services/institution/institution.service';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {ActivatedRoute} from '@angular/router';
import {UserService} from '../../../shared/services/user/user.service';
import * as L from 'leaflet';
import {InstitutionMapRequest} from '../../../shared/models/institution/InstitutionMapRequest';
import {CalendarEventRequest} from '../../../shared/models/institution/CalendarEventRequest';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.scss']
})
export class EditComponent implements OnInit {
    constructor(
      private institutionService: InstitutionService,
      private handleResponse: ResponseHandlerService,
      private formBuilder: FormBuilder,
      private toastr: ToastrService,
      private route: ActivatedRoute,
        private userService: UserService
  ) { }
    generationEvent: CalendarEventRequest = {};
    generationEventList: CalendarEventRequest[] = [];
    year: number = new Date().getFullYear(); // Set current year as default
    events: Event[] = []; // User-defined events
    today = new Date();
    bsValue = {
        year: this.today.getFullYear(),
        month: this.today.getMonth() + 1,
        day: this.today.getDate()
    };
    maxDate = {
        year: 2024,
        month: 11,
        day: 31
    };
    minDate = {
        year: 2024,
        month: 0,
        day: 1
    };
    institutionID: string;
    currentInstitution;
    institutionMapRequest: InstitutionMapRequest = {};
    loading = false;
    countries = [];
    private map: L.Map | undefined;
    private marker: L.Marker | undefined;
    latitude = 0;
    longitude = 0;
  updateInstitutionForm = this.formBuilder.group({
        name: ['', [Validators.required, Validators.maxLength(40), Validators.minLength(3)]],
        slogan: ['', [Validators.required, Validators.maxLength(50), Validators.minLength(3)]],
        country: ['', [Validators.required]],
        address: ['', [Validators.required, Validators.maxLength(100), Validators.minLength(10)]],
        description: ['', [Validators.required, Validators.maxLength(500), Validators.minLength(10)]],
        website: ['', [Validators.required, Validators.maxLength(100), Validators.minLength(5)]],
      }
  );
    generateForm = this.formBuilder.group({
        startDate: [this.bsValue, [Validators.required]],
        finishDate: [this.bsValue, [Validators.required]],
        name: ['', [Validators.required, Validators.maxLength(15)]],
        color: ['#FFFF00', [Validators.required]],
    }, { validators: [this.sameMonth, this.dateOrder] });
  ngOnInit() {
      this.generationEvent.startDate = new Date();
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
    downloadExcel() {
        this.institutionService.downloadExcel(this.institutionID).subscribe(
            response => {
                const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
                const url = window.URL.createObjectURL(blob);
                const link = document.createElement('a');
                link.href = url;
                link.download = 'file.xlsx';
                link.click();
                window.URL.revokeObjectURL(url);
            },
            error => {
                console.log('error downloading');
                this.toastr.error('Error downloading Excel.');
            }
        );
    }
    returnEvent(form: FormGroup): CalendarEventRequest {
        return Object.assign(this.generationEvent, form.value, {color: form.controls['color'].value});
    }
    dateOrder(control: FormGroup): {[key: string]: boolean} | null {
        const startDate = new Date(control.get('startDate').value.year, control.get('startDate').value.month - 1, control.get('startDate').value.day);
        const finishDate = new Date(control.get('finishDate').value.year, control.get('finishDate').value.month - 1, control.get('finishDate').value.day);
        if (startDate && finishDate && startDate.getTime() > finishDate.getTime()) {
            return {'invalidDateOrder': true};
        }
        return null;
    }
    isOverlapping(event1: CalendarEventRequest, event2: CalendarEventRequest): boolean {
        return new Date(event1.startDate).getTime() <= new Date(event2.finishDate).getTime() &&
            new Date(event1.finishDate).getTime() >= new Date(event2.startDate).getTime();
    }
    sameMonth(control: FormGroup): {[key: string]: boolean} | null {
        const startDate = new Date(control.get('startDate').value.year, control.get('startDate').value.month - 1, control.get('startDate').value.day);
        const finishDate = new Date(control.get('finishDate').value.year, control.get('finishDate').value.month - 1, control.get('finishDate').value.day);

        if (startDate && finishDate && startDate.getMonth() !== finishDate.getMonth()) {
            return {'differentMonth': true};
        }

        return null;
    }
    convertDatePickerToDate(date: any): Date {
        return new Date(date.year, date.month - 1, date.day);
    }
    addEvent() {
        if (this.generateForm.valid) {
            const newEvent = this.returnEvent(this.generateForm);
            newEvent.startDate = new Date(this.generateForm.controls['startDate'].value.year,
                this.generateForm.controls['startDate'].value.month - 1,
                this.generateForm.controls['startDate'].value.day);
            newEvent.finishDate = new Date(this.generateForm.controls['finishDate'].value.year,
                this.generateForm.controls['finishDate'].value.month - 1,
                this.generateForm.controls['finishDate'].value.day);
            console.log('New Event', newEvent);
            for (const event of this.generationEventList) {
                console.log('Event', event);
                if (this.isOverlapping(newEvent, event)) {
                    console.log('Event overlaps with an existing event.');
                    this.toastr.error('Event overlaps with an existing event.');
                    return;
                }
            }
            const clonedEvent = JSON.parse(JSON.stringify(newEvent));
            this.generationEventList.push(clonedEvent);
            this.toastr.success('Event added successfully.');
        } else {
            this.toastr.error('Form is not valid.');
        }
    }
    generateExcel() {
        console.log('Events being generated : ' + this.generationEventList);
        this.institutionService.generateExcel(this.institutionID, this.generationEventList).subscribe(
            response => {
                console.log('success generating');
                this.toastr.success('Excel generated successfully.');
            }, error => {
                console.log('error generating');
                this.toastr.error('Error generating Excel.');
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
