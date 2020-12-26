import {AfterViewInit, Component, ElementRef, OnInit, Output, ViewChild, EventEmitter, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subject, Subscription} from 'rxjs';
import {ArticlesService} from '../../articles-list/articles.service';

@Component({
  selector: 'app-navbar-search',
  templateUrl: './navbar-search.component.html',
  styleUrls: ['./navbar-search.component.css']
})
export class NavbarSearchComponent implements OnInit, OnDestroy {
  searchTitle: string = null;
  searchTitleSubscription: Subscription;

  constructor(private router: Router, private articleService: ArticlesService) {
  }

  ngOnInit(): void {
    this.searchTitleSubscription = this.articleService.searchTitleEvent.subscribe(
      (title) => {
        this.searchTitle = title;
      }
    );
  }

  /**
   * search article by title.
   */
  searchArticle(): void {
    if (!this.validateTitle()) {
      return;
    }
    this.router.navigate([`/articles/search/${this.searchTitle}`]);
  }

  /**
   * check if search title is valid.
   * return true if length > 0, otherwise return false;
   */
  validateTitle(): boolean {
    this.searchTitle = this.formatTitle(this.searchTitle);
    return this.searchTitle.length > 0;
  }

  /**
   * trim leading spaces. if title is null, return empty string.
   * @param title title to be format
   */
  formatTitle(title: string): string {
    if (title == null) {
      return '';
    }
    return title.trim();
  }

  ngOnDestroy(): void {
    this.searchTitleSubscription.unsubscribe();
  }
}
