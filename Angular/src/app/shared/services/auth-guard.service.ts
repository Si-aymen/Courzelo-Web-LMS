import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {SessionStorageService} from './user/session-storage.service';
import {first, map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private router: Router,
    private sessionService: SessionStorageService,
  ) { }


  canActivate(
      next: ActivatedRouteSnapshot,
      state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const requiredRoles = next.data['roles'] as Array<string>;

    return this.sessionService.getUser().pipe(
        map(user => {
          if (!requiredRoles || requiredRoles.some(role => user.roles.includes(role))) {
            return true;
          } else {
            this.router.navigateByUrl('/sessions/signin');
            return false;
          }
        }),
        first()
    );
  }
}
