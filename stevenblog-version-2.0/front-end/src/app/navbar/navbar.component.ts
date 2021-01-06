import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {AuthService} from '../shared/auth.service';
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
  isWriter = false;

  constructor(private authService: AuthService,
              private router: Router,
              private articlesService: ArticlesService) {
  }

  ngOnInit(): void {
    this.userSubscription = this.authService.userAuthedEvent.subscribe(
      (isLoggedIn) => {
        this.loggedIn = isLoggedIn;
      }
    );
    this.isWriter = this.authService.userHasRole(3);
  }

  ngOnDestroy(): void {
    this.userSubscription.unsubscribe();
  }

  logout(): void {
    this.clearSearchTitleIfExists();
    this.authService.logout();
  }

  /**
   * clear ArticlePublishTitle input.
   * this method should be call before navigate to other url if needed.
   */
  clearSearchTitleIfExists(): void {
    this.articlesService.clearSearchTitle();
  }
}
