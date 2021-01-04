import {Injectable} from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, CanDeactivate} from '@angular/router';
import {Observable} from 'rxjs';
import {AccountInfoComponent} from './account-info.component';
import {SignUpService} from '../sign-up.service';

@Injectable({
  providedIn: 'root'
})
export class AccountInfoGuard implements CanDeactivate<AccountInfoComponent> {

  constructor(private signUpService: SignUpService) {
  }

  canDeactivate(component: AccountInfoComponent,
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
