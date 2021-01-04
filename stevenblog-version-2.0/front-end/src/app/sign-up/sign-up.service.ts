import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, Subject} from 'rxjs';
import {SignUpUser} from '../shared/ApplicationUser.model';
import {Router} from '@angular/router';
import {ActionStatusResponse, DataTransactionService, SignUpResponse} from '../shared/data-transaction.service';

@Injectable({
  providedIn: 'root'
})
export class SignUpService {
  signUpUserEvent = new BehaviorSubject<SignUpUser>(null);
  showConfirmationBeforeLeavePopEvent = new Subject<boolean>();
  continueLeaveUrl: string;
  signUpUser: SignUpUser = null;
  backToSignUpStartPageEvent = new Subject<boolean>();
  isInProcess = false;

  constructor(private router: Router, private dataTransactionService: DataTransactionService) {
  }

  startsSignUp(): void {
    this.signUpUser = new SignUpUser();
    this.signUpUserEvent.next(this.signUpUser);
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

  signUp(): void {
    this.dataTransactionService.signUp(this.signUpUser).subscribe(
      (response) => {
        console.log(response);
      }
    );
  }

  moveNext(): void {
    this.isInProcess = true;
  }

  isUsernameValid(username: string): Observable<ActionStatusResponse> {
    return this.dataTransactionService.isUsernameValid(username);
  }

  isEmailValid(email: string): Observable<ActionStatusResponse>  {
    return this.dataTransactionService.isEmailValid(email);
  }
}
