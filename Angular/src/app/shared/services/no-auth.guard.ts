import { Injectable } from '@angular/core';
import {
  CanLoad,
  Route,
  Router,
 UrlSegment
} from '@angular/router';
import {Observable, of} from 'rxjs';
import {SessionStorageService} from './user/session-storage.service';
import {map} from 'rxjs/operators';
import {AuthenticationService} from './user/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class NoAuthGuard implements CanLoad {
  constructor( private authService: AuthenticationService, private router: Router) {}

  canLoad(route: Route, segments: UrlSegment[]): Observable<boolean> {
    return this.authService.checkAuthState().pipe(
        map(isAuthenticated => {
          if (isAuthenticated) {
            console.log(isAuthenticated);
            this.router.navigateByUrl('/dashboard/v1');
            return false;
          }
          console.log(isAuthenticated);
          return true;
        })
    );
  }
}
