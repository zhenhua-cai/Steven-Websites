import {AfterViewInit, Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {SignUpUser} from '../../shared/ApplicationUser.model';
import {SignUpService} from '../sign-up.service';
import {environment} from '../../../environments/environment';
import {Router} from '@angular/router';
import {AbstractControl, FormControl, FormGroup, Validators} from '@angular/forms';
import {EMPTY, Observable, Subscription} from 'rxjs';
import {AppService} from '../../app.service';
import {ActionStatusResponse} from '../../shared/data-transaction.service';


declare var $: any;

@Component({
  selector: 'app-account-info',
  templateUrl: './account-info.component.html',
  styleUrls: ['./account-info.component.css']
})
export class AccountInfoComponent implements OnInit, OnDestroy, AfterViewInit {
  submitted: boolean;
  signUpUser: SignUpUser;
  errorList: Map<string, string[]> = new Map<string, string[]>();
  toolTipForPassword: string;
  toolTipForUsername: string;
  specialsCharsForPassword: string;
  minLengthForUsername: number;
  maxLengthForUsername: number;
  minLengthForPassword: number;
  maxLengthForPassword: number;
  showPassword = false;
  showConfirmPassword = false;
  passwordStrengthBar: any;
  signUpUserSubscription: Subscription;
  signUpForm: FormGroup;
  isConfirmEmailValid = false;
  isConfirmPasswordValid = false;
  usernameAlreadyTaken = false;
  isCheckingUsernameDuplicate = false;
  usernameBeforeCheck = true;
  usernameIconToolTip: string;
  emailAlreadyTaken = false;
  beforeCheckEmail = true;

  constructor(private signUpService: SignUpService,
              private router: Router, private appService: AppService) {
  }

  ngOnDestroy(): void {
    if (this.signUpUserSubscription) {
      this.signUpUserSubscription.unsubscribe();
    }
  }

  ngOnInit(): void {
    this.signUpUserSubscription = this.signUpService.signUpUserEvent.subscribe(
      (user) => {
        this.signUpUser = user;
        this.signUpUser.email = '';
        this.signUpUser.confirmEmail = '';
        this.signUpUser.password = '';
        this.signUpUser.confirmPassword = '';
      });

    this.signUpForm = new FormGroup(
      {
        username: new FormControl(this.signUpUser.username, [
          Validators.required, this.validateUsername.bind(this)
        ]),
        email: new FormControl(this.signUpUser.email, [
          Validators.required, Validators.email, this.validateEmail.bind(this)
        ]),
        confirmEmail: new FormControl(this.signUpUser.confirmEmail, [
          this.validateConfirmEmail.bind(this)
        ]),
        password: new FormControl(this.signUpUser.password, [
          Validators.required, this.validatePassword.bind(this)
        ]),
        confirmPassword: new FormControl(this.signUpUser.confirmPassword, [
          this.validateConfirmPassword.bind(this)
        ])
      }
    );

    this.specialsCharsForPassword = environment.specialCharsForPassword;
    this.toolTipForPassword = this.constructPasswordToolTip(this.specialsCharsForPassword);
    this.minLengthForUsername = +environment.minLengthForUsername;
    this.maxLengthForUsername = +environment.maxLengthForUsername;
    this.minLengthForPassword = +environment.minLengthForPassword;
    this.maxLengthForPassword = +environment.maxLengthForPassword;
  }

  ngAfterViewInit(): void {
    this.passwordStrength();
  }

  /**
   * prevent from clicking back button
   * @param $event
   */
  @HostListener('window:popstate', ['$event']) preventDefaultBackward($event): void {
    $event.preventDefault();
    $event.returnValue = 'Your data will be lost!';
  }

  private passwordStrength(): void {
    const options = {
      common: {minChar: this.minLengthForPassword},
      ui: {
        showVerdictsInsideProgressBar: true,
      }
    };
    $('#password').pwstrength(options);
    this.passwordStrengthBar = $('#password+div.progress');
    this.passwordStrengthBar.addClass('position-absolute w-100');
    this.passwordStrengthBar.hide();
  }

  private constructPasswordToolTip(specialChars: string): string {
    if (!specialChars) {
      return '';
    }
    let resultStr = 'special characters:\n';
    for (const c of specialChars) {
      resultStr += c + ' ';
    }
    return resultStr;
  }

  cancel(): void {
    this.signUpService.cancelSignUp();
  }

  nextPage(): void {
    this.submitted = true;
    if (this.signUpForm.invalid) {
      return;
    }
    this.checkUsernameAndEmailBeforeMove(
      () => {
        this.signUpService.moveNext();
        this.router.navigate(['/sign-up/confirmation']);
      }
    );
  }

  validatePassword(control: FormControl): { [s: string]: boolean } {
    if (!control.dirty) {
      return null;
    }
    this.signUpUser.password = control.value;
    let hasError = false;
    this.errorList.delete('password');
    this.clearConfirmPassword();
    if (control.value.length === 0) {
      this.passwordStrengthBar.hide();
      return null;
    }

    this.passwordStrengthBar.show();
    const passwordErrors: string[] = [];
    if (control.value.length < this.minLengthForPassword) {
      hasError = true;
      passwordErrors.push(`Password should have at least ${this.minLengthForPassword} characters.`);
    } else if (control.value.length > this.maxLengthForPassword) {
      hasError = true;
      passwordErrors.push(`Password should have at most ${this.maxLengthForPassword} characters.`);
    }
    const validateChars = this.validatePasswordCharacters(control.value);
    if (!validateChars[0]) {
      hasError = true;
      passwordErrors.push(`Password should contains upper case letters`);
    }
    if (!validateChars[1]) {
      hasError = true;
      passwordErrors.push(`Password should contains lower case letters`);
    }
    if (!validateChars[2]) {
      hasError = true;
      passwordErrors.push(`Password should contains digits`);
    }
    if (!validateChars[3]) {
      hasError = true;
      passwordErrors.push(`Password should contains special characters`);
    }
    if (!validateChars[4]) {
      hasError = true;
      passwordErrors.push(`Password contains invalid characters`);
    }
    if (hasError) {
      this.errorList.set('password', passwordErrors);
      return {passwordError: true};
    }
    return null;
  }


  validateConfirmPassword(control: FormControl): { [s: string]: boolean } {
    this.isConfirmPasswordValid = false;
    if (!control.dirty) {
      return null;
    }
    this.signUpUser.confirmPassword = control.value;
    let hasError = false;
    this.errorList.delete('confirmPassword');
    if (control.value.length === 0) {
      return null;
    }
    if (control.value !== this.signUpUser.password) {
      hasError = true;
      this.errorList.set('confirmPassword', ['Passwords don\'t match']);
    }
    if (hasError) {
      return {confirmPasswordError: true};
    }
    this.isConfirmPasswordValid = true;
    return null;
  }

  validateEmail(control: FormControl): { [s: string]: boolean } {
    this.clearConfirmEmail();
    this.emailAlreadyTaken = false;
    this.beforeCheckEmail = true;
    this.signUpUser.email = control.value;
    return null;
  }

  clearConfirmEmail(): void {
    if (this.signUpUser.confirmEmail) {
      this.signUpUser.confirmEmail = '';

      this.signUpForm.get('confirmEmail').setValue('');
    }
  }

  clearConfirmPassword(): void {
    if (this.signUpUser.confirmPassword) {
      this.signUpUser.confirmPassword = '';
      this.signUpForm.get('confirmPassword').setValue('');
    }
  }

  /**
   * validate password, check if it matches character constrains.
   * 1. has upper case.
   * 2. has lower case.
   * 3. has digit
   * 4. has special characters.
   * 5. not contains invalid characters.
   * @private
   */
  private validatePasswordCharacters(password: string): boolean[] {

    let hasUpper = false;
    let hasLower = false;
    let hasDigit = false;
    let hasSpecial = false;
    let NoInvalidChar = true;
    for (const char of password) {
      if (char >= 'a' && char <= 'z') {
        hasLower = true;
      } else if (char >= 'A' && char <= 'Z') {
        hasUpper = true;
      } else if (char >= '0' && char <= '9') {
        hasDigit = true;
      } else if (this.specialsCharsForPassword.indexOf(char) > -1) {
        hasSpecial = true;
      } else {
        NoInvalidChar = false;
      }
    }
    return [hasUpper, hasLower, hasDigit, hasSpecial, NoInvalidChar];
  }

  private validateUsername(control: FormControl): { [s: string]: boolean } {
    if (!control.dirty) {
      return null;
    }
    this.signUpUser.username = control.value;
    this.usernameBeforeCheck = true;
    this.usernameAlreadyTaken = false;
    this.errorList.delete('username');
    const usernameErrors: string[] = [];
    let hasError = false;
    if (control.value.length < this.minLengthForUsername) {
      hasError = true;
      usernameErrors.push(`Username should have at least ${this.minLengthForUsername} characters`);
    } else if (control.value.length > this.maxLengthForUsername) {
      hasError = true;
      usernameErrors.push(`Username should have at most ${this.maxLengthForUsername} characters`);
    }
    if (control.value.length > 0 && control.value.startsWith('_')) {
      hasError = true;
      usernameErrors.push(`Username cannot starts with _`);
    }
    const regex = RegExp(/^[a-zA-Z0-9_]+$/);
    if (control.value.length > 0 && !regex.test(control.value)) {
      hasError = true;
      usernameErrors.push(`Username should only contains digits, letters and underscores`);
    }
    if (hasError) {
      this.errorList.set('username', usernameErrors);
      return {usernameError: true};
    }
    return null;
  }

  validateConfirmEmail(control: FormControl): { [s: string]: boolean } {
    this.isConfirmEmailValid = false;
    if (!control.dirty) {
      return null;
    }
    this.signUpUser.confirmEmail = control.value;
    let hasError = false;
    this.errorList.delete('confirmEmail');
    if (control.value.length === 0) {

      return null;
    }
    if (control.value !== this.signUpUser.email) {
      hasError = true;
      this.errorList.set('confirmEmail', ['Emails don\'t match']);
    }
    if (hasError) {
      return {confirmEmailError: true};
    }
    this.isConfirmEmailValid = true;
    return null;
  }

  isUsernameExists(control: AbstractControl): void {
    if (!this.usernameBeforeCheck) {
      return;
    }
    if (control.invalid) {
      this.appService.showWarningToast('Invalid Username', 'Please a valid username.');
      return;
    }
    this.checkUsernameExists(control.value).subscribe(
      (actionResponse) => {
        this.isCheckingUsernameDuplicate = false;
        this.usernameAlreadyTaken = !actionResponse.status;
      },
      ignore => {
        this.showUnknownErrorToast();
      }
    );
  }

  private checkUsernameExists(username: string): Observable<ActionStatusResponse> {
    this.usernameBeforeCheck = false;
    this.isCheckingUsernameDuplicate = true;
    return this.signUpService.isUsernameValid(username);
  }

  isEmailExists(): void {
    this.checkEmailExists().subscribe(
      (actionResponse) => {
        this.emailAlreadyTaken = !actionResponse.status;
      },
      ignore => {
        this.showUnknownErrorToast();
      }
    );
  }

  checkEmailExists(): Observable<ActionStatusResponse> {
    if (this.emailAlreadyTaken) {
      return EMPTY;
    }
    if (this.signUpForm.get('email').invalid) {
      return EMPTY;
    }
    this.beforeCheckEmail = false;
    return this.signUpService.isEmailValid(this.signUpUser.email);
  }

  usernameInputIcon(): string {
    if (this.usernameBeforeCheck) {
      return 'pi-question';
    }
    if (this.isCheckingUsernameDuplicate) {
      return 'pi-spin pi-spinner';
    }
    if (this.usernameAlreadyTaken) {
      return 'pi-times invalid';
    }
    return 'pi-check valid';
  }

  toolTipForUsernameIcon(): string {
    if (this.usernameBeforeCheck) {
      return 'Check If Username Exists.';
    }
    if (this.isCheckingUsernameDuplicate) {
      return 'Checking...';
    }
    if (this.usernameAlreadyTaken) {
      return 'Username was already taken.';
    }
    return 'Username is ready to use.';
  }

  checkUsernameAndEmailBeforeMove(callBack: () => void): void {
    this.checkUsernameExists(this.signUpUser.username).subscribe(
      (actionResponse) => {
        this.usernameAlreadyTaken = !actionResponse.status;
        this.isCheckingUsernameDuplicate = false;
        if (this.usernameAlreadyTaken) {
          this.showUsernameAlreadyTakenToast();
          return;
        }
        this.checkEmailExists()
          .subscribe(
            (response) => {
              this.emailAlreadyTaken = !response.status;
              if (this.emailAlreadyTaken) {
                this.showEmailAlreadyTakenToast();
                return;
              }
              callBack();
            }, ignore => {
              this.showUnknownErrorToast();
            }
          );
      }, ignore => {
        this.showUnknownErrorToast();
      }
    );
  }

  showUsernameAlreadyTakenToast(): void {
    this.appService.showWarningToast('Username already exists', 'Someone already took this username.');
  }

  showEmailAlreadyTakenToast(): void {
    this.appService.showWarningToast('Email exists', 'This email is already in use.');
  }

  showUnknownErrorToast(): void {
    this.appService.showErrorToast('Error', 'Unknown Error');
  }
}
