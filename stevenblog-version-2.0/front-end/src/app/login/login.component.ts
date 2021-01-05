import {Component, OnInit} from '@angular/core';
import {AuthService} from '../shared/auth.service';
import {AttemptLoginUser} from '../shared/ApplicationUser.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  rememberMe = false;
  constructor(private authService: AuthService) {
  }

  ngOnInit(): void {
  }

  onSubmit(user: AttemptLoginUser): void {
    this.authService.login(user);
  }
}
