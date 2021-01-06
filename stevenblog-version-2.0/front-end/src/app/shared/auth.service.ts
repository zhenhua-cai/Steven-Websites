import {Injectable} from '@angular/core';
import {AuthResponse, DataTransactionService} from './data-transaction.service';
import {AttemptLoginUser, AuthedUser} from './ApplicationUser.model';
import {BehaviorSubject} from 'rxjs';
import {Router} from '@angular/router';
import {AppService} from '../app.service';
import {SignUpService} from '../sign-up/sign-up.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  isLoggedIn = false;
  authedUser: AuthedUser = null;
  userAuthedEvent = new BehaviorSubject<boolean>(null);

  constructor(private dataTransaction: DataTransactionService,
              private router: Router,
              private appService: AppService,
              private signUpService: SignUpService) {
  }

  /**
   * auto login. check if user info is stored.
   * if true, autologin, otherwise, not login.
   */
  autoLogin(): void {
    const refreshToken = this.appService.getRefreshToken();
    if (!refreshToken) {
      this.clearAuthInfo();
      return;
    }
    this.authedUser = this.appService.getUserInfo();
    if (this.authedUser) {
      this.isLoggedIn = true;
      this.userAuthedEvent.next(true);
    } else {
      this.clearAuthInfo();
    }
  }

  userHasRole(role: number): boolean {
    if (!this.isLoggedIn) {
      return false;
    }
    return this.authedUser.roles.includes(role);
  }

  getUserRoles(): number[] {
    if (!this.isLoggedIn) {
      return [];
    }
    return this.authedUser.roles;
  }

  login(user: AttemptLoginUser): void {
    this.appService.blockScreen();
    this.appService.isLoggingIn = true;
    this.dataTransaction.login(user).subscribe(
      (authResponse) => {
        this.appService.unblockScreen();
        this.isLoggedIn = true;
        this.authedUser = this.processAuthResponse(user, authResponse);
        this.userAuthedEvent.next(true);
        this.appService.isLoggingIn = false;
        this.router.navigate(['/']);
      }, error => {
        this.appService.unblockScreen();
        this.appService.isLoggingIn = false;
        this.appService.showErrorToast('Login Failed', error.error.message);
        if (error.error.message.startsWith('Activation')) {
          this.signUpService.activateAccount(user.username);
        }
      }
    );
  }

  logout(): Promise<boolean> {
    this.clearAuthInfo();
    return this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return this.isLoggedIn;
  }


  clearAuthInfo(): void {
    this.appService.clearAuthInfo();
    this.userAuthedEvent.next(false);
    this.isLoggedIn = false;
  }

  /**
   * store authed information: user info, access token , refresh token.
   * @param user
   * @param authResponse
   * @private
   */
  private processAuthResponse(user: AttemptLoginUser, authResponse: AuthResponse): AuthedUser {
    this.appService.storeAuthToken(authResponse.accessToken, authResponse.refreshToken);
    return this.appService.storeUserInfo(user, authResponse.roles);
  }
}
