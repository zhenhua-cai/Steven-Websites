import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, Subject} from 'rxjs';
import {ActionStatusResponse, DataTransactionService} from '../shared/data-transaction.service';

@Injectable({
  providedIn: 'root'
})
export class ResetPasswordService {
  gotoStartsResetEvent = new Subject<boolean>();
  resetPasswordUserEmailEvent = new BehaviorSubject<string>(null);

  constructor(private dataTransaction: DataTransactionService) {
  }

  resetPassword(password: string, confirmPassword: string, userEmail: string): Observable<ActionStatusResponse> {
    return this.dataTransaction.resetPassword(password, confirmPassword, userEmail);
  }
}
