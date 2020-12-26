import {Component, ElementRef, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {AuthService} from '../shared/auth.service';
import {AuthedUser} from '../shared/ApplicationUser.model';
import {ArticlesService} from '../articles-list/articles.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  animations: []
})
export class NavbarComponent implements OnInit, OnDestroy {
  @Input()
  isLandingPage: boolean;
  loggedIn = false;
  userSubscription: Subscription;
  numOfMsg = 0;

  constructor(private authService: AuthService,
              private router: Router,
              private articlesService: ArticlesService) {
  }

  ngOnInit(): void {
    this.userSubscription = this.authService.userAuthedEvent.subscribe(
      (user) => {
        this.loggedIn = user !== null;
      }
    );
  }

  ngOnDestroy(): void {
    this.userSubscription.unsubscribe();
  }

  logout(): void {
    this.clearSearchTitleIfExists();
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  /**
   * clear title input.
   * this method should be call before navigate to other url if needed.
   */
  clearSearchTitleIfExists(): void {
    this.articlesService.clearSearchTitle();
  }
}
