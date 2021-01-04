import {Injectable} from '@angular/core';
import {CanDeactivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {SignUpConfirmationComponent} from './sign-up-confirmation.component';
import {SignUpService} from '../sign-up.service';

@Injectable({
  providedIn: 'root'
})
export class SignUpConfirmationGuard implements CanDeactivate<SignUpConfirmationComponent> {
  constructor(private signUpService: SignUpService) {
  }

  canDeactivate(
    component: SignUpConfirmationComponent,
    currentRoute: ActivatedRouteSnapshot,
    currentState: RouterStateSnapshot,
    nextState?: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.signUpService.hasUnsavedData() && !this.signUpService.isInProcess) {
      this.signUpService.showLeaveConfirmationPopup();
      this.signUpService.continueLeaveUrl = nextState.url;
      return false;
    }
    if (!this.signUpService.isInProcess) {
      this.signUpService.isInProcess = false;
      this.signUpService.clear();
      this.signUpService.backToSignUpStartPage();
    }
    this.signUpService.isInProcess = false;
    return true;
  }

}
