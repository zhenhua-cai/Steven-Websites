import {Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {SignUpUser} from '../../shared/ApplicationUser.model';
import {Subscription} from 'rxjs';
import {SignUpService} from '../sign-up.service';
import {AppService} from '../../app.service';
import {AccountService} from '../../account/account.service';
import {ResetPasswordService} from '../../reset-password/reset-password.service';

@Component({
  selector: 'app-verify-email',
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.css']
})
export class VerifyEmailComponent implements OnInit, OnDestroy {
  signUpUser: SignUpUser;
  signUpUserSubscription: Subscription;
  numberOfDigits = 6;
  verificationDigits: number[] = null;
  resendEmailTimeCountDown = 60;
  mouseOverButton = false;
  isCopyPasteCode = false;
  isCodeEntered = false;
  failedVerifyBefore = false;
  resendEmailTimeInterval;
  failedTime = 0;

  constructor(private signUpService: SignUpService,
              private appService: AppService,
              private accountService: AccountService,
              private resetPasswordService: ResetPasswordService) {
    this.verificationDigits = [].constructor(this.numberOfDigits);
  }

  ngOnInit(): void {
    this.signUpService.signUpUserEvent.subscribe(
      (user) => {
        this.signUpUser = user;
      });
    this.startsResendEmailTimeCountDown();
  }

  /**
   * prevent from closing browser/tab, refresh page
   * @param $event event
   */
  @HostListener('window:beforeunload', ['$event']) preventDefaultClosingPage($event): void {
    $event.preventDefault();
    $event.returnValue = 'Your data will be lost!';
  }

  @HostListener('window:popstate', ['$event'])
  preventDefaultBackward($event): void {
    $event.preventDefault();
    $event.returnValue = 'Your data will be lost!';
    if (this.accountService.resetPassword) {
      this.resetPasswordService.gotoStartsResetEvent.next(true);
    }
  }

  ngOnDestroy(): void {
    if (this.signUpUserSubscription) {
      this.signUpUserSubscription.unsubscribe();
    }
  }


  startsResendEmailTimeCountDown(): void {
    this.resendEmailTimeCountDown = 60;
    this.continueTimeCountDown();
  }

  continueTimeCountDown(): void {
    this.resendEmailTimeInterval = setInterval(
      () => {
        this.resendEmailTimeCountDown--;
        if (this.resendEmailTimeCountDown === 0) {
          clearInterval(this.resendEmailTimeInterval);
          this.resendEmailTimeInterval = null;
        }
      }, 1000);
  }

  resendEmailLabel(): string {
    if (this.isCodeEntered) {
      return 'Verify';
    }
    if (this.resendEmailTimeCountDown === 0) {
      return 'Resend Email';
    }
    return 'Resend Email (' + this.resendEmailTimeCountDown + 's)';
  }

  onButtonClicked(): void {
    if (this.isCodeEntered) {
      this.checkVerificationCode();
      return;
    }
    this.resendEmail();
  }

  private resendEmail(): void {
    const verificationType = this.signUpUser.username == null ? 0 : 1;
    const userInfo = this.signUpUser.username == null ? this.signUpUser.email : this.signUpUser.username;
    this.signUpService.resendVerificationEmail(userInfo, verificationType).subscribe(
      (response) => {
        this.failedTime = 0;
        this.startsResendEmailTimeCountDown();
        if (response.status) {
          this.delayToShowEmailToast('Email Sent', 'Please your check your email');
        }
      }, ignore => {
        this.delayToShowEmailToast('Error', 'Failed to send email');
      }
    );
  }

  delayToShowEmailToast(summary: string, detail: string): void {
    setTimeout(
      () => {
        this.appService.showSuccessToast(summary, detail);
      }, 1000
    );
  }

  @HostListener('window:keydown', ['$event'])
  onKeyDown($event: KeyboardEvent): void {
    if (($event.ctrlKey || $event.metaKey) && ($event.key === 'v' || $event.key === 'V')) {
      this.isCopyPasteCode = true;
    }
  }

  keyUp($event: KeyboardEvent): void {
    if ($event.key.length > 1) {
      return;
    }
    if ($event.key >= '0' && $event.key <= '9') {
      this.isCodeEntered = true;
      const id = +($event.target as HTMLElement).id.slice(-1);
      this.verificationDigits[id] = +$event.key[0];
      if (id === this.numberOfDigits - 1) {
        this.isCodeEntered = true;
        if (!this.failedVerifyBefore) {
          this.checkVerificationCode();
        }
      } else {
        const nextId = 'digit-' + (+id + 1);
        document.getElementById(nextId).focus();
      }
    }
  }

  private checkVerificationCode(): void {
    if (!this.preValidateCode()) {
      return;
    }
    this.appService.blockScreen();
    setTimeout(
      () => {
        const code = this.verificationDigits.join('');
        const verifiedBy = this.signUpUser.username == null ? this.signUpUser.email : this.signUpUser.username;
        this.signUpService.checkVerificationCode(code, verifiedBy, !this.accountService.resetPassword).subscribe(
          (response) => {
            this.appService.unblockScreen();
            if (response.status) {
              if (this.accountService.resetPassword) {
                this.accountService.resetEmailVerified();
              } else {
                this.signUpService.SignUpComplete();
              }
            } else {
              this.failedVerifyBefore = true;
              this.appService.showErrorToast('Verification Failed', 'Please enter a valid code');
              this.failedTime++;
              this.isCodeEntered = false;
              if (this.failedTime === 3) {
                setTimeout(
                  () => {
                    this.appService.showWarningToast('Failed too many time', 'Please request a new code.');
                  }, 200
                );
              }
            }
          }, ignore => {
            this.failedVerifyBefore = true;
            this.appService.unblockScreen();
          }
        );
      }, 500
    );
  }

  onPaste($event: ClipboardEvent): void {
    const clipboardData = $event.clipboardData;
    if (!clipboardData) {
      return;
    }
    const data = clipboardData.getData('text').trim();
    if (!data || data.length !== this.numberOfDigits) {
      return;
    }
    const regex = RegExp(/^[0-9]+$/);
    if (!regex.test(data)) {
      return;
    }
    for (let i = 0; i < this.numberOfDigits; i++) {
      this.verificationDigits[i] = +data[i];
    }
    this.isCodeEntered = true;
    this.checkVerificationCode();
  }

  private preValidateCode(): boolean {
    for (let i = 0; i < this.numberOfDigits; i++) {
      if (this.verificationDigits[i] == null) {
        return false;
      }
      if (this.verificationDigits[i] < 0 || this.verificationDigits[i] > 9) {
        return false;
      }
    }
    return true;
  }
}
