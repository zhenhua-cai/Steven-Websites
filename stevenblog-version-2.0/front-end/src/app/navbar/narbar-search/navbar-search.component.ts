import {Component, OnInit, OnDestroy} from '@angular/core';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs';
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
   * search article by ArticlePublishTitle.
   */
  searchArticle(): void {
    if (!this.validateTitle()) {
      return;
    }
    this.router.navigate([`/articles/search/${this.searchTitle}`]);
  }

  /**
   * check if search ArticlePublishTitle is valid.
   * return true if length > 0, otherwise return false;
   */
  validateTitle(): boolean {
    this.searchTitle = this.formatTitle(this.searchTitle);
    return this.searchTitle.length > 0;
  }

  /**
   * trim leading spaces. if ArticlePublishTitle is null, return empty string.
   * @param title ArticlePublishTitle to be format
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
