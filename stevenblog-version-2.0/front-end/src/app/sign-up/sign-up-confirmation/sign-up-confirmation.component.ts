import {Component, OnDestroy, OnInit} from '@angular/core';
import {SignUpService} from '../sign-up.service';
import {SignUpUser} from '../../shared/ApplicationUser.model';
import {Subscription} from 'rxjs';
import {Router} from '@angular/router';

@Component({
  selector: 'app-sign-up-confirmation',
  templateUrl: './sign-up-confirmation.component.html',
  styleUrls: ['./sign-up-confirmation.component.css']
})
export class SignUpConfirmationComponent implements OnInit, OnDestroy {
  signUpUser: SignUpUser;
  signUpUserSubscription: Subscription;

  constructor(private signUpService: SignUpService, private router: Router) {
  }

  ngOnInit(): void {
    this.signUpService.signUpUserEvent.subscribe(
      (user) => {
        this.signUpUser = user;
      });
  }

  ngOnDestroy(): void {
    if (this.signUpUserSubscription) {
      this.signUpUserSubscription.unsubscribe();
    }
  }

  cancel(): void {
    this.signUpService.cancelSignUp();
  }

  prevPage(): void {
    this.signUpService.moveNext();
    this.router.navigate(['/sign-up/account-info']);
  }

  signUp(): void {
    this.signUpService.signUp();
  }
}
