import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {ResponseHandlerService} from '../../../shared/services/user/response-handler.service';
import {debounceTime} from 'rxjs/operators';
import {InstitutionResponse} from '../../../shared/models/institution/InstitutionResponse';
import {InstitutionService} from '../../../shared/services/institution/institution.service';
import {PaginatedInstitutionsResponse} from '../../../shared/models/institution/PaginatedInstitutionsResponse';
import {ToastrService} from 'ngx-toastr';
import {InstitutionRequest} from '../../../shared/models/institution/InstitutionRequest';
import {UserService} from '../../../shared/services/user/user.service';

@Component({
  selector: 'app-institutions',
  templateUrl: './institutions.component.html',
  styleUrls: ['./institutions.component.scss']
})
export class InstitutionsComponent implements OnInit {

  get currentPage(): number {
    return this._currentPage;
  }

  set currentPage(value: number) {
    this._currentPage = value;
    if (this.searchControl.value == null) {
      this.loadInstitutions(this._currentPage, this.itemsPerPage, '');
    } else {
      this.loadInstitutions(this._currentPage, this.itemsPerPage, this.searchControl.value);
    }
  }
  constructor(
      private institutionService: InstitutionService,
      private handleResponse: ResponseHandlerService,
      private formBuilder: FormBuilder,
      private toastr: ToastrService,
      private userService: UserService
  ) { }
  institutions: InstitutionResponse[] = [];
  _currentPage = 1;
  totalPages = 0;
  totalItems = 0;
  itemsPerPage = 2;
  loading = false;
  searchControl: FormControl = new FormControl();
    addInstitutionForm = this.formBuilder.group({
            name: ['', [Validators.required, Validators.maxLength(40), Validators.minLength(3)]],
            slogan: ['', [Validators.required, Validators.maxLength(50), Validators.minLength(3)]],
            country: ['', [Validators.required]],
            address: ['', [Validators.required, Validators.maxLength(100), Validators.minLength(10)]],
            description: ['', [Validators.required, Validators.maxLength(500), Validators.minLength(10)]],
            website: ['', [Validators.required, Validators.maxLength(100), Validators.minLength(5)]],
        }
    );
    institutionRequest: InstitutionRequest = {};
    countries = [];
  ngOnInit() {
    this.loadInstitutions(this.currentPage, this.itemsPerPage, '');
    this.searchControl.valueChanges
        .pipe(debounceTime(200))
        .subscribe(value => {
          this.loadInstitutions(1, this.itemsPerPage, value);
        });
      this.userService.getCountries().subscribe(
          countries => {
              this.countries = countries;
              console.log(this.countries);
          }
      );
  }
    shouldShowError(controlName: string, errorName: string): boolean {
        const control = this.addInstitutionForm.get(controlName);
        return control && control.errors && control.errors[errorName] && (control.dirty || control.touched);
    }
    addInstitution() {
        if (this.addInstitutionForm.valid) {
            this.institutionRequest = this.addInstitutionForm.value;
        this.institutionRequest.latitude = 0;
        this.institutionRequest.longitude = 0;
        this.institutionService.addInstitution(this.institutionRequest).subscribe(
            response => {
                this.toastr.success('Institution added successfully');
                this.loadInstitutions(this.currentPage, this.itemsPerPage, '');
            }, error => {
                this.handleResponse.handleError(error);
            }
        );
    }
        this.toastr.error('Please fill all fields');
    }
  loadInstitutions(page: number, size: number, keyword: string) {
    this.loading = true;
    this.institutionService.getInstitutions(page - 1, size, keyword).subscribe((response: PaginatedInstitutionsResponse) => {
          console.log(response);
          this.institutions = response.institutions;
          this._currentPage = response.currentPage + 1;
          this.totalPages = response.totalPages;
          this.totalItems = response.totalItems;
          this.itemsPerPage = response.itemsPerPage;
          this.loading = false;
        }, error => {
          this.handleResponse.handleError(error);
          this.loading = false;
        }
    );
  }
}
