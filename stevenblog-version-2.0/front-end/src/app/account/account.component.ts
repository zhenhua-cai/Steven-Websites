import {Component, OnDestroy, OnInit} from '@angular/core';
import {MenuItem} from 'primeng/api';
import {ActivatedRoute, Router} from '@angular/router';
import {AccountService} from './account.service';
import {Subscription} from 'rxjs';
import {ArticleEditorService} from '../article-editor/article-editor.service';

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
              private accountService: AccountService) {
  }

  ngOnInit(): void {
    this.items = [
      {label: 'Profile', icon: 'pi pi-user', routerLink: '/account/profile'},
      {label: 'My Articles', icon: 'fas fa-book', routerLink: '/account/articles'},
      {label: 'My Draft', icon: 'fas fa-pencil-alt', routerLink: '/account/drafts'},
      {label: 'Messages', icon: 'fas fa-envelope', routerLink: '/account/messages'},
    ];
    this.findMatchRoute(this.router.url);
    this.activateItemChangesSubscription = this.accountService.accountRouteEvent.subscribe(
      (route) => {
        this.findMatchRoute(route);
      }
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
