import { Injectable } from '@angular/core';
import {ToastrService} from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class ResponseHandlerService {

  constructor(private toastr: ToastrService) { }

  handleSuccess(message: string = 'Operation successful!') {
    this.toastr.success(message, 'Success!', { progressBar: true });
  }

  handleError(error: any) {
    console.error(error);
    let errorMessage = 'An unexpected error occurred';
    if (error.error && error.error.message) {
      errorMessage = error.error.message;
    }
    switch (error.status) {
      case 409:
        this.toastr.error(errorMessage, 'Error!', {progressBar: true});
        break;
      case 400:
        this.toastr.error(errorMessage, 'Error!', { progressBar: true });
        break;
      default:
        this.toastr.error(errorMessage, 'Error!', { progressBar: true });
    }
  }
}
