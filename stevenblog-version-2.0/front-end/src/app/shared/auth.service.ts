import {Injectable} from '@angular/core';
import {DataTransactionService} from './data-transaction.service';
import {AttemptLoginUser, AuthedUser} from './ApplicationUser.model';
import {BehaviorSubject, Subject} from 'rxjs';
import {Router} from '@angular/router';
import {AppService} from '../app.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  authedUser: AuthedUser = null;
  userAuthedEvent = new BehaviorSubject<AuthedUser>(null);

  constructor(private dataTransaction: DataTransactionService,
              private router: Router,
              private appService: AppService) {
  }

  autoLogin(): void {
    this.authedUser = JSON.parse(localStorage.getItem('user'));

    this.userAuthedEvent.next(this.authedUser);
  }

  login(user: AttemptLoginUser): void {
    this.appService.blockScreen();
    this.dataTransaction.login(user).subscribe(
      (responseData) => {
        this.authedUser = new AuthedUser();
        this.authedUser.username = user.username;
        this.authedUser.token = 'Bearer ' + responseData.jwt;
        this.authedUser.roles = responseData.roles;
        localStorage.setItem('user', JSON.stringify(this.authedUser));
        this.appService.unblockScreen();
        this.userAuthedEvent.next(this.authedUser);
        this.router.navigate(['/']);
      }, error => {
        this.appService.unblockScreen();
      }
    );
  }

  logout(): void {
    localStorage.removeItem('user');
    this.userAuthedEvent.next(null);
    this.authedUser = null;
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return this.authedUser != null;
  }

  getUsername(): string {
    return this.authedUser != null ? this.authedUser.username : null;
  }
}

export class AuthResult {
  success: boolean;
  msg: string;
}
