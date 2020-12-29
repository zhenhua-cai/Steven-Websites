import {Injectable} from '@angular/core';
import {DataTransactionService} from './data-transaction.service';
import {AttemptLoginUser, AuthedUser} from './ApplicationUser.model';
import {BehaviorSubject, Subject} from 'rxjs';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  authProcessResponse = new Subject<AuthResult>();
  authedUser: AuthedUser = null;
  userAuthedEvent = new BehaviorSubject<AuthedUser>(null);

  constructor(private dataTransaction: DataTransactionService, private router: Router) {
  }

  autoLogin(): void {
    this.authedUser = JSON.parse(localStorage.getItem('user'));

    this.userAuthedEvent.next(this.authedUser);
  }

  login(user: AttemptLoginUser): void {
    this.dataTransaction.login(user).subscribe(
      (responseData) => {
        this.authedUser = new AuthedUser();
        this.authedUser.username = user.username;
        this.authedUser.token = 'Bearer ' + responseData.jwt;
        this.authedUser.roles = responseData.roles;
        localStorage.setItem('user', JSON.stringify(this.authedUser));
        const result = new AuthResult();
        result.success = true;
        this.authProcessResponse.next(result);
        this.userAuthedEvent.next(this.authedUser);
      }, error => {
        const result = new AuthResult();
        result.success = false;
        result.msg = error.error.msg;
        this.authProcessResponse.next(result);
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
