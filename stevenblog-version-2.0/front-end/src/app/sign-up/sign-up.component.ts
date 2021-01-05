import {Component, ElementRef, HostListener, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ConfirmationService, MenuItem} from 'primeng/api';
import {ActivatedRoute, Router} from '@angular/router';
import {SignUpUser} from '../shared/ApplicationUser.model';
import {SignUpService} from './sign-up.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit, OnDestroy {
  signUpSteps: MenuItem[];
  signUpUserSubscription: Subscription;
  showLeaveConfirmationSubscription: Subscription;
  signUpUser: SignUpUser;
  inStartPage = true;
  backToSignUpStartPageSubscription: Subscription;

  constructor(private router: Router,
              private activatedRoute: ActivatedRoute,
              private signUpService: SignUpService,
              private confirmationService: ConfirmationService) {
  }

  ngOnDestroy(): void {
    if (this.signUpUserSubscription) {
      this.signUpUserSubscription.unsubscribe();
    }
    if (this.backToSignUpStartPageSubscription) {
      this.backToSignUpStartPageSubscription.unsubscribe();
    }
  }

  ngOnInit(): void {
    this.backToSignUpStartPageSubscription = this.signUpService.backToSignUpStartPageEvent.subscribe(
      (value) => {
        this.inStartPage = value;
      }
    );
    this.showLeaveConfirmationSubscription = this.signUpService.showConfirmationBeforeLeavePopEvent.subscribe(
      (show) => {
        if (show) {
          this.confirmationService.confirm({
            key: 'quitSignUp',
          });
        }
      }
    );
    this.signUpUserSubscription = this.signUpService.signUpUserEvent.subscribe(
      (user) => {
        this.signUpUser = user;
      });
    this.signUpSteps = [
      {label: 'Account Information', routerLink: '/sign-up/account-info'},
      {label: 'Confirmation', routerLink: '/sign-up/confirmation'},
      {label: 'Verify Email', routerLink: '/sign-up/verifyEmail'},
      {label: 'Complete', routerLink: '/sign-up/complete'},
    ];
    if (this.inStartPage) {
      this.router.navigate(['/sign-up']);
    }
  }

  /**
   * prevent from closing browser/tab, refresh page
   * @param $event window event
   */
  @HostListener('window:beforeunload', ['$event']) preventDefaultClosingPage($event): void {
    $event.preventDefault();
    $event.returnValue = 'Your data will be lost!';
  }

  discard(): void {
    this.signUpService.clear();
    this.signUpService.continueNavigate();
    this.confirmationService.close();
  }

  starts(): void {
    this.inStartPage = false;
    this.signUpService.startsSignUp();
    this.router.navigate(['account-info'], {relativeTo: this.activatedRoute});
  }
}
