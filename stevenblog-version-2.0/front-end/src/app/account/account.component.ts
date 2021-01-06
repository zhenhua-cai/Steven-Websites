import {Component, OnDestroy, OnInit} from '@angular/core';
import {MenuItem} from 'primeng/api';
import {ActivatedRoute, Router} from '@angular/router';
import {AccountService} from './account.service';
import {Subscription} from 'rxjs';
import {ArticleEditorService} from '../article-editor/article-editor.service';
import {AuthService} from '../shared/auth.service';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit, OnDestroy {
  items: MenuItem[];
  activeItem: MenuItem;
  activateItemChangesSubscription: Subscription;

  constructor(private router: Router,
              private authService: AuthService,
              private accountService: AccountService) {
  }

  ngOnInit(): void {
    this.constructMenuItem();
    this.findMatchRoute(this.router.url);
    this.activateItemChangesSubscription = this.accountService.accountRouteEvent.subscribe(
      (route) => {
        this.findMatchRoute(route);
      }
    );
  }

  constructMenuItem(): void {
    this.items = [
      {label: 'Profile', icon: 'pi pi-user', routerLink: '/account/profile'},
    ];
    if (this.authService.userHasRole(3)) {
      this.items.push(
        {label: 'My Articles', icon: 'fas fa-book', routerLink: '/account/articles'}
      );
      this.items.push(
        {label: 'My Draft', icon: 'fas fa-pencil-alt', routerLink: '/account/drafts'}
      );
    }
    this.items.push(
      {label: 'Messages', icon: 'fas fa-envelope', routerLink: '/account/messages'}
    );
  }

  ngOnDestroy(): void {
    this.activateItemChangesSubscription.unsubscribe();
  }

  private findMatchRoute(route: string): void {
    for (const index in this.items) {
      if (route === this.items[index].routerLink) {
        this.activeItem = this.items[index];
        break;
      }
    }
  }
}
