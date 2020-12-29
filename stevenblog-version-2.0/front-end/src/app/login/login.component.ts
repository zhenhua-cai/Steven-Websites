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
export class LoginComponent implements OnInit {
  rememberMe = false;
  blocked = false;

  constructor(private loginService: AuthService) {
  }

  ngOnInit(): void {
  }

  onSubmit(user: AttemptLoginUser): void {
    this.blocked = true;
    this.loginService.login(user);
  }
}
