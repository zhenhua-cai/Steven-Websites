import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import {AuthService} from '../shared/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  accountRouteEvent = new Subject<string>();

  constructor(private authService: AuthService) {
  }

  accountRouteChange(route: string): void {
    this.accountRouteEvent.next(route);
  }
  getUsername(): string{
    return this.authService.getUsername();
  }
}
