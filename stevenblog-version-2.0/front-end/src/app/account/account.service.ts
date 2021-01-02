import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import {AuthService} from '../shared/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  accountRouteEvent = new Subject<string>();

  constructor() {
  }

  accountRouteChange(route: string): void {
    this.accountRouteEvent.next(route);
  }
}
