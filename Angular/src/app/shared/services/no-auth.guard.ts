import { Injectable } from '@angular/core';
import {
  CanLoad,
  Route,
  Router,
 UrlSegment
} from '@angular/router';
import { Observable } from 'rxjs';
import {SessionStorageService} from './user/session-storage.service';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class NoAuthGuard implements CanLoad {
  constructor(private sessionStorageService: SessionStorageService, private router: Router) {}

  canLoad(route: Route, segments: UrlSegment[]): Observable<boolean> {
    return this.sessionStorageService.getUser().pipe(
        map(user => {
          if (user) {
            this.router.navigateByUrl('/dashboard/v1');
            console.log('User is already logged in');
            return false;
          }
          console.log('User is not logged in');
          return true;
        })
    );
  }
}
