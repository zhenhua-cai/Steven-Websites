import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {AuthService} from '../shared/auth.service';
import {AttemptLoginUser} from '../shared/ApplicationUser.model';
import {Subscription} from 'rxjs';
import {Router} from '@angular/router';
import {MessageService} from 'primeng/api';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
  rememberMe = false;
  authProcessSubscription: Subscription;
  blocked = false;

  constructor(private loginService: AuthService,
              private router: Router,
              private messageService: MessageService) {
  }

  ngOnInit(): void {
    // get response from authentication process.
    this.authProcessSubscription = this.loginService.authProcessResponse.subscribe((result) => {
      this.blocked = false;
      if (result.success) {
        this.router.navigate(['/']);
      } else {
        this.messageService.add({
          severity: 'error',
          summary: 'Login Error',
          detail: result.msg
        });
      }
    });
  }

  onSubmit(user: AttemptLoginUser): void {
    this.blocked = true;
    this.loginService.login(user);
  }

  ngOnDestroy(): void {
    this.authProcessSubscription.unsubscribe();
  }
}
