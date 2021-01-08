import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {ActionStatusResponse, DataTransactionService} from '../shared/data-transaction.service';
import {Router} from '@angular/router';
import {SignUpService} from '../sign-up/sign-up.service';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  accountRouteEvent = new Subject<string>();
  resetPassword = false;

  constructor(private dataTransactionService: DataTransactionService,
              private router: Router,
              private signUpService: SignUpService) {
  }

  accountRouteChange(route: string): void {
    this.accountRouteEvent.next(route);
  }

  forgotUsername(email: string): Observable<ActionStatusResponse> {
    return this.dataTransactionService.forgotUsername(email);
  }

  forgotPassword(email: string): Observable<ActionStatusResponse> {
    return this.dataTransactionService.forgotPassword(email);
  }

  tryResetPassword(): void {
    this.resetPassword = true;
  }

  finishResetPassword(): void {
    this.resetPassword = false;
  }

  verifyResetPasswordEmail(email: string): void {
    this.tryResetPassword();
    this.signUpService.preVerifyAccount(null, email);
    this.router.navigate(['/reset-password/verify']);
  }

  resetEmailVerified(): void {
    this.router.navigate(['/reset-password/reset']);
  }
}
