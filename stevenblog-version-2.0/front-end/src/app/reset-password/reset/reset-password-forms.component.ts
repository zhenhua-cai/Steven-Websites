import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import {environment} from '../../../environments/environment';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ResetPasswordService} from '../reset-password.service';
import {Subscription} from 'rxjs';
import {AppService} from '../../app.service';
import {Router} from '@angular/router';

declare var $: any;

@Component({
  selector: 'app-reset',
  templateUrl: './reset-password-forms.component.html',
  styleUrls: ['./reset-password-forms.component.css']
})
export class ResetPasswordFormsComponent implements OnInit, AfterViewInit, OnDestroy {
  toolTipForPassword: string;
  specialsCharsForPassword: string;
  resetPasswordForm: FormGroup;
  errorList: Map<string, string[]> = new Map<string, string[]>();
  password: string;
  confirmPassword: string;
  passwordStrengthBar: any;
  private minLengthForUsername: number;
  private maxLengthForUsername: number;
  private minLengthForPassword: number;
  private maxLengthForPassword: number;
  showPassword = false;
  submitted: boolean;
  showConfirmPassword = false;
  isConfirmPasswordValid: boolean;
  userEmail: string;
  userEmailUpdateSubscription: Subscription;

  constructor(private resetService: ResetPasswordService,
              private appService: AppService,
              private router: Router) {
  }

  ngOnDestroy(): void {
    if (this.userEmailUpdateSubscription) {
      this.userEmailUpdateSubscription.unsubscribe();
    }
  }

  ngOnInit(): void {
    this.userEmailUpdateSubscription = this.resetService.resetPasswordUserEmailEvent.subscribe(
      (email) => {
        this.userEmail = email;
      }
    );
    this.resetPasswordForm = new FormGroup(
      {
        password: new FormControl(this.password, [
          Validators.required, this.validatePassword.bind(this)
        ]),
        confirmPassword: new FormControl(this.confirmPassword, [
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

  onSubmit(): void {
    this.submitted = true;
    if (this.resetPasswordForm.invalid) {
      return;
    }
    this.resetService.resetPassword(this.password, this.confirmPassword, this.userEmail).subscribe(
      (response) => {
        if (response.status) {
          this.appService.showSuccessToast('Password Reset', 'Successfully updated password');
          this.router.navigate(['/login']);
        } else {
          this.appService.showErrorToast('Failed', 'Password is invalid');
        }
      }
    );
  }

  ngAfterViewInit(): void {
    this.passwordStrength();
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

  validatePassword(control: FormControl): { [s: string]: boolean } {
    if (!control.dirty) {
      return null;
    }
    let hasError = false;
    this.errorList.delete('password');
    this.password = control.value;
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

  validateConfirmPassword(control: FormControl): { [s: string]: boolean } {
    this.isConfirmPasswordValid = false;
    if (!control.dirty) {
      return null;
    }
    this.confirmPassword = control.value;
    let hasError = false;
    this.errorList.delete('confirmPassword');
    if (control.value.length === 0) {
      return null;
    }
    if (control.value !== this.password) {
      hasError = true;
      this.errorList.set('confirmPassword', ['Passwords don\'t match']);
    }
    if (hasError) {
      return {confirmPasswordError: true};
    }
    this.isConfirmPasswordValid = true;
    return null;
  }


  clearConfirmPassword(): void {
    if (this.confirmPassword) {
      this.confirmPassword = '';
      this.resetPasswordForm.get('confirmPassword').setValue('');
    }
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
}
