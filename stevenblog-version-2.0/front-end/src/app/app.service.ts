import {Injectable} from '@angular/core';
import {MessageService} from 'primeng/api';
import {Subject} from 'rxjs';
import jwtDecode, {JwtPayload} from 'jwt-decode';
import {AttemptLoginUser, AuthedUser} from './shared/ApplicationUser.model';

@Injectable({
  providedIn: 'root'
})
export class AppService {
  blockScreenEvent = new Subject<boolean>();
  isLoggingIn = false;
  refreshedToken = false;

  constructor(private messageService: MessageService) {
  }

  showWarningToast(summary: string, detail: string): void {
    this.showToastMsg('warn', summary, detail);
  }

  showInfoToast(summary: string, detail: string): void {
    this.showToastMsg('info', summary, detail);
  }

  showErrorToast(summary: string, detail: string): void {
    this.showToastMsg('error', summary, detail);
  }

  showSuccessToast(summary: string, detail: string): void {
    this.showToastMsg('success', summary, detail);
  }

  showToastMsg(severity: string, summary: string, detail: string): void {
    this.messageService.add({severity, summary, detail});
  }

  blockScreen(): void {
    this.blockScreenEvent.next(true);
  }

  unblockScreen(): void {
    this.blockScreenEvent.next(false);
  }

  getToken(tokenName: string, expiration: Date, expireAfter: Date): string {
    if (!!expiration && expiration > expireAfter) {
      return localStorage.getItem(tokenName);
    }
    return null;
  }

  getTokenExpiration(unixTimeStr: string): Date {
    if (!unixTimeStr) {
      return null;
    }
    return new Date(+unixTimeStr);
  }

  clearAccessToken(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('accessTokenExpiration');
  }

  clearRefreshToken(): void {
    localStorage.removeItem('refreshTokenExpiration');
    localStorage.removeItem('refreshToken');
  }

  clearAuthTokens(): void {
    this.clearAccessToken();
    this.clearRefreshToken();
  }

  clearAuthedUserInfo(): void {
    localStorage.removeItem('user');
  }

  /**
   * clear auth tokens and user info
   */
  clearAuthInfo(): void {
    this.clearAuthedUserInfo();
    this.clearAuthTokens();
  }

  storeAccessToken(accessTokenStr: string): void {
    const accessToken = jwtDecode<JwtPayload>(accessTokenStr);
    localStorage.setItem('accessToken', 'Bearer ' + accessTokenStr);
    localStorage.setItem('accessTokenExpiration', (accessToken.exp * 1000).toString());
  }

  storeRefreshToken(refreshTokenStr: string): void {
    const refreshToken = jwtDecode<JwtPayload>(refreshTokenStr);
    localStorage.setItem('refreshToken', 'Bearer ' + refreshTokenStr);
    localStorage.setItem('refreshTokenExpiration', (refreshToken.exp * 1000).toString());
  }

  storeAuthToken(accessTokenStr: string, refreshTokenStr: string): void {
    this.storeAccessToken(accessTokenStr);
    this.storeRefreshToken(refreshTokenStr);
  }

  storeUserInfo(user: AttemptLoginUser, roles: string[]): AuthedUser {
    const authedUser = new AuthedUser();
    authedUser.roles = roles;
    localStorage.setItem('user', JSON.stringify(authedUser));
    return authedUser;
  }

  public getRefreshToken(): string {
    const expiration = this.getTokenExpiration(localStorage.getItem('refreshTokenExpiration'));
    return this.getToken('refreshToken', expiration, new Date());
  }

  public getAccessToken(): string {
    const expiration = this.getTokenExpiration(localStorage.getItem('accessTokenExpiration'));
    return this.getToken('accessToken', expiration, new Date());
  }

  getUserInfo(): AuthedUser {
    const userStr = localStorage.getItem('user');
    if (!userStr) {
      return null;
    }
    return JSON.parse(userStr);
  }
}
