import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from './shared/auth.service';
import {Subscription} from 'rxjs';
import {AppService} from './app.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  needsToBlock: boolean;
  blockScreenSubscription: Subscription;

  constructor(private authService: AuthService, private appService: AppService) {
  }

  ngOnInit(): void {
    this.authService.autoLogin();
    this.blockScreenSubscription = this.appService.blockScreenEvent.subscribe(
      (block) => {
        if (block) {
          this.needsToBlock = true;
        } else {
          this.needsToBlock = false;
        }
      }
    );
  }

  ngOnDestroy(): void {
    if (this.blockScreenSubscription) {
      this.blockScreenSubscription.unsubscribe();
    }
  }
}
