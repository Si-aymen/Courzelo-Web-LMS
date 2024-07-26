import {Component, OnInit} from '@angular/core';
import {InstitutionRequest} from '../../../shared/models/institution/InstitutionRequest';
import {InstitutionService} from '../../../shared/services/institution/institution.service';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';
import {FormBuilder, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {ActivatedRoute} from '@angular/router';
import {UserService} from '../../../shared/services/user/user.service';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.scss']
})
export class EditComponent implements OnInit {
    institutionID: string;
    currentInstitution;
    loading = false;
    countries = [];
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
                );            }, error => {
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
}
