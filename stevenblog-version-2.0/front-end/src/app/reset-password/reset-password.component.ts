import {Component, ElementRef, HostListener, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {NgModel} from '@angular/forms';
import {AccountService} from '../account/account.service';
import {AppService} from '../app.service';
import {Router} from '@angular/router';
import {ResetPasswordService} from './reset-password.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit, OnDestroy {
  userEmail: string = null;
  isInStartPage = true;
  gotoStartPageSubscription: Subscription;

  constructor(private accountService: AccountService,
              private appService: AppService,
              private router: Router,
              private resetPasswordService: ResetPasswordService) {
  }


  ngOnInit(): void {
    this.gotoStartPageSubscription = this.resetPasswordService.gotoStartsResetEvent.subscribe(
      (data) => {
        this.isInStartPage = data;
      }
    );
    if (this.isInStartPage) {
      this.router.navigate(['/reset-password']);
    }
  }

  forgotUsername(email: NgModel): void {
    if (email.invalid) {
      return;
    }
    this.accountService.forgotUsername(this.userEmail).subscribe(
      (response) => {
        if (response.status) {
          this.appService.showSuccessToast('Request Sent', 'Please check you email');
          this.router.navigate(['/login']);
        } else {
          this.appService.showErrorToast('Request Failed', 'Unable to find this email');
        }
      }
    );
  }

  forgotPassword(email: NgModel): void {
    if (email.invalid) {
      return;
    }
    this.accountService.forgotPassword(this.userEmail).subscribe(
      (response) => {
        if (response.status) {
          this.appService.showSuccessToast('Request Sent', 'Please check you email');
          this.isInStartPage = false;
          this.resetPasswordService.resetPasswordUserEmailEvent.next(this.userEmail);
          this.accountService.verifyResetPasswordEmail(this.userEmail);
        } else {
          this.appService.showErrorToast('Request Failed', 'Unable to find this email');
        }
      }
    );
  }

  ngOnDestroy(): void {
    if (this.gotoStartPageSubscription) {
      this.gotoStartPageSubscription.unsubscribe();
    }
  }

}
