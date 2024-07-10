import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {AuthenticationService} from './authentication.service';
import {ToastrService} from 'ngx-toastr';


@Injectable()
export class Interceptor implements HttpInterceptor {

  constructor(
              private authService: AuthenticationService,
              private toastr: ToastrService
              ) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const modifiedRequest = request.clone({
      withCredentials: true
    });
    return next.handle(modifiedRequest).pipe(
        catchError((error: HttpErrorResponse) => {
          if (error.status === 401) {
              this.toastr.error('Session Expired', 'Error', {progressBar: true});
            this.authService.logout();
           }
           return throwError(error);
         })
    );
  }
}
