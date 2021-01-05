import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, Subject} from 'rxjs';
import {SignUpUser} from '../shared/ApplicationUser.model';
import {Router} from '@angular/router';
import {ActionStatusResponse, DataTransactionService, SignUpResponse} from '../shared/data-transaction.service';
import {AppService} from '../app.service';

@Injectable({
  providedIn: 'root'
})
export class SignUpService {
  signUpUserEvent = new BehaviorSubject<SignUpUser>(null);
  showConfirmationBeforeLeavePopEvent = new Subject<boolean>();
  continueLeaveUrl: string;
  signUpUser: SignUpUser = null;
  backToSignUpStartPageEvent = new BehaviorSubject<boolean>(true);
  isInProcess = false;

  constructor(private router: Router,
              private dataTransactionService: DataTransactionService,
              private appService: AppService) {
  }

  startsSignUp(): void {
    this.signUpUser = new SignUpUser();
    this.startsSignUpWithUser(this.signUpUser);
  }

  private startsSignUpWithUser(signUpUser: SignUpUser): void {
    this.signUpUserEvent.next(signUpUser);
  }

  cancelSignUp(): void {
    this.router.navigate(['/login']);
  }

  showLeaveConfirmationPopup(): void {
    this.showConfirmationBeforeLeavePopEvent.next(true);
  }

  continueNavigate(): void {
    if (!this.continueLeaveUrl) {
      return;
    }
    this.router.navigate([this.continueLeaveUrl]);
  }

  hasUnsavedData(): boolean {
    if (this.signUpUser == null) {
      return false;
    }
    return !!(this.signUpUser.email || this.signUpUser.username
      || this.signUpUser.password || this.signUpUser.confirmEmail
      || this.signUpUser.confirmPassword);
  }

  clear(): void {
    this.signUpUser = null;
  }

  backToSignUpStartPage(): void {
    this.backToSignUpStartPageEvent.next(true);
  }

  activateAccount(username: string): void {
    this.backToSignUpStartPageEvent.next(false);
    this.signUpUser = new SignUpUser();
    this.signUpUser.username = username;
    this.startsSignUpWithUser(this.signUpUser);
    this.router.navigate(['/sign-up/verifyEmail']);
  }

  signUp(): void {
    this.dataTransactionService.signUp(this.signUpUser).subscribe(
      (response) => {
        if (response.success) {
          this.clearPasswordsAndConfirmEmail();
          this.appService.showSuccessToast('Congrats', 'Your account has been created.');
          this.router.navigate(['/sign-up/verifyEmail']);
        } else {
          this.appService.showErrorToast('Unable to register', response.msg);
        }
      }
    );
  }

  moveNext(): void {
    this.isInProcess = true;
  }

  isUsernameValid(username: string): Observable<ActionStatusResponse> {
    return this.dataTransactionService.isUsernameValid(username);
  }

  isEmailValid(email: string): Observable<ActionStatusResponse> {
    return this.dataTransactionService.isEmailValid(email);
  }

  private clearPasswordsAndConfirmEmail(): void {
    if (this.signUpUser == null) {
      return;
    }
    this.signUpUser.password = null;
    this.signUpUser.confirmPassword = null;
    this.signUpUser.confirmEmail = null;

  }

  resendVerificationEmail(email: string): Observable<ActionStatusResponse> {
    return this.dataTransactionService.resendVerificationEmail(email);
  }

  checkVerificationCode(code: string, username: string): Observable<ActionStatusResponse> {
    return this.dataTransactionService.checkVerificationCode(code, username);
  }

  SignUpComplete(): void {
    this.moveNext();
    this.signUpUser = null;
    this.router.navigate(['/sign-up/complete']);
  }
}
