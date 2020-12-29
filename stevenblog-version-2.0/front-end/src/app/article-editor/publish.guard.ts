import {Injectable} from '@angular/core';
import {CanActivate, CanActivateChild, CanDeactivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {PublishService} from './publish.service';
import {PublishComponent} from './publish/publish.component';

@Injectable({
  providedIn: 'root'
})
export class PublishGuard implements CanActivate, CanActivateChild, CanDeactivate<PublishComponent> {

  constructor(private publishService: PublishService, private router: Router) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (!this.publishService.isInPublishProgress) {
      this.router.navigate(['/account/drafts']);
    }
    return true;
  }

  canActivateChild(
    childRoute: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (!this.publishService.isInPublishProgress) {
      this.router.navigate(['/account/drafts']);
    }
    return true;
  }

  canDeactivate(
    component: PublishComponent,
    currentRoute: ActivatedRouteSnapshot,
    currentState: RouterStateSnapshot,
    nextState?: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.publishService.isInPublishProgress) {
      this.publishService.showPopUpBeforeQuit(true);
      return false;
    }
    this.publishService.showPopUpBeforeQuit(false);
    return true;
  }

}
