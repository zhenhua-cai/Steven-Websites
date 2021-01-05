import {Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {SignUpUser} from '../../shared/ApplicationUser.model';
import {Subscription} from 'rxjs';
import {SignUpService} from '../sign-up.service';
import {AppService} from '../../app.service';

@Component({
  selector: 'app-verify-email',
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.css']
})
export class VerifyEmailComponent implements OnInit, OnDestroy {
  signUpUser: SignUpUser;
  signUpUserSubscription: Subscription;
  numberOfDigits = 6;
  verificationDigits: string[] = null;
  resendEmailTimeCountDown = 60;
  mouseOverButton = false;
  isCopyPasteCode = false;
  isCodeEntered = false;
  failedVerifyBefore = false;

  constructor(private signUpService: SignUpService, private appService: AppService) {
    this.verificationDigits = [].constructor(this.numberOfDigits);
  }

  ngOnInit(): void {
    this.signUpService.signUpUserEvent.subscribe(
      (user) => {
        this.signUpUser = user;
      });
    this.startsResendEmailTimeCountDown();
  }

  ngOnDestroy(): void {
    if (this.signUpUserSubscription) {
      this.signUpUserSubscription.unsubscribe();
    }
  }


  startsResendEmailTimeCountDown(): void {
    this.resendEmailTimeCountDown = 60;
    const resendEmailTimeInterval = setInterval(
      () => {
        this.resendEmailTimeCountDown--;
        if (this.resendEmailTimeCountDown === 0) {
          clearInterval(resendEmailTimeInterval);
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
    this.signUpService.resendVerificationEmail(this.signUpUser.email).subscribe(
      (response) => {
        this.delayToShowEmailToast('Email Sent', 'Please your check your email');
        this.startsResendEmailTimeCountDown();
      }, error => {
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
      const id = +($event.target as HTMLElement).id.slice(-1);
      this.verificationDigits[id] = $event.key[0];
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
    this.appService.blockScreen();
    setTimeout(
      () => {
        const code = this.verificationDigits.join('');
        this.signUpService.checkVerificationCode(code, this.signUpUser.username).subscribe(
          (response) => {
            this.appService.unblockScreen();
            if (response.status) {
              this.signUpService.SignUpComplete();
            } else {
              this.failedVerifyBefore = true;
              this.appService.showErrorToast('Verification Failed', 'Please enter a valid code');
            }
          }, error => {
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
      this.verificationDigits[i] = data[i];
    }
    this.isCodeEntered = true;
    this.checkVerificationCode();
  }
}
