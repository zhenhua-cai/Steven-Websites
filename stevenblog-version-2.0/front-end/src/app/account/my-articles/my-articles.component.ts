import {Component, OnInit} from '@angular/core';
import {AccountService} from '../account.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Article} from '../../shared/Article';
import {GetArticlesResponse} from '../../shared/data-transaction.service';
import {ArticlesService} from '../../articles-list/articles.service';
import {LazyLoadEvent, MenuItem} from 'primeng/api';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-my-articles',
  templateUrl: './my-articles.component.html',
  styleUrls: ['./my-articles.component.css']
})
export class MyArticlesComponent implements OnInit {

  articles: Article[];
  totalArticles: number;
  pageSize = 10;
  currentPage = 0;
  isLoading = true;
  selectedArticle: Article = null;
  items: MenuItem[];
  isDraftRoute = false;
  searchValue: string;

  constructor(private articleService: ArticlesService,
              private accountService: AccountService,
              private router: Router,
              private activateRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.accountService.accountRouteChange(this.router.url);
    this.items = [
      {label: 'View', icon: 'pi pi-fw pi-search', command: () => this.viewArticle(this.selectedArticle)},
      {label: 'Edit', icon: 'far pi-fw fa-edit', command: () => this.editArticle(this.selectedArticle)},
      {label: 'Delete', icon: 'far pi-fw fa-trash-alt', command: () => this.deleteArticle(this.selectedArticle)}
    ];
    this.isDraftRoute = this.router.url !== '/account/articles';
  }

  /**
   * search article by pagination
   * @param event pagination event
   */
  loadingArticles(event: LazyLoadEvent): void {
    this.selectedArticle = null;
    // calculate current page.
    // event.first is the index of the first element in the next showing page.
    // event.rows is the number of rows showing in the table.
    // ex: Page 0 - first element index is 0. Page 1 - first element index is event.row + 1;
    this.currentPage = event.first / event.rows;
    this.searchArticlesBy(this.searchValue, event.sortField, event.sortOrder, event.rows);
  }


  /**
   * clear input field and show default search results
   * @param $event
   */
  clearIfEmpty($event: Event): void {
    if (this.searchValue !== null && this.searchValue.length === 0) {
      this.searchValue = null;
      this.searchArticlesBy(null, null, -1, this.pageSize);
    }
  }


  deleteArticle(selectedArticle: Article): void {

  }

  editArticle(selectedArticle: Article): void {

  }

  /**
   * search articles when press enter on input field or click search icon
   * @param $event
   */
  searchArticles($event: any): void {
    if (!this.validateSearchValue()) {
      return;
    }
    this.searchArticlesBy(this.searchValue, null, null, this.pageSize);
  }


  private validateSearchValue(): boolean {
    this.searchValue = this.formatSearchValue(this.searchValue);
    return this.searchValue.length > 0;
  }

  /**
   * format search value. return empty string is it's null. otherwise trim left and right spaces.
   * @param str
   * @private
   */
  private formatSearchValue(str: string): string {
    if (str == null) {
      return '';
    }
    return str.trim();
  }

  /**
   * search articles by search value(title). order by certain field -- asc or desc
   * @param title search value.
   * @param orderBy
   * @param order
   * @param pageSize
   * @private
   */
  private searchArticlesBy(title: string, orderBy: string, order: number, pageSize: number): void {
    this.isLoading = true;
    if (orderBy) {
      this.searchArticlesByAuthorAndTitleOrderBy(title, orderBy, order, pageSize)
        .subscribe((articlesResponse) => {
          this.resolveArticlesResponse(articlesResponse);
          this.isLoading = false;
        });
    } else {
      // sort field not present, by default, sort by last modified date.
      this.searchArticlesByAuthorAndTitle(title, pageSize)
        .subscribe(
          (articlesResponse) => {
            this.resolveArticlesResponse(articlesResponse);
            this.isLoading = false;
          });
    }
  }

  /**
   * search article by author name and order by certain field.
   * if this page is in draft route. then it searches ArticleDraft instead.
   * * @param size size of the searching page.
   * @param title article title to be searched.
   * @param sortField order by field.
   * @param sortOrder 1 - asc, -1 desc. desc by default.
   * @private
   */
  private searchArticlesByAuthorAndTitleOrderBy(title: string, sortField: string,
                                                sortOrder: number, size: number): Observable<GetArticlesResponse> {
    if (!this.isDraftRoute) {
      return this.articleService
        .fetchArticlesByAuthorAndTitleOrderBy(this.accountService.getUsername(), title, sortField,
          sortOrder, this.currentPage, size);
    }
    return this.articleService.fetchDraftsByAuthorAndTitleOrderBy(this.accountService.getUsername(), title,
      sortField, sortOrder, this.currentPage, size);
  }

  /**
   * search article by author name. if this page is in draft route. then it searches ArticleDraft instead.
   * result will be order by last modified date time by default.
   * @param title article title to be searched
   * @param size size of the searching page.
   * @private
   */
  private searchArticlesByAuthorAndTitle(title: string, size: number): Observable<GetArticlesResponse> {
    if (!this.isDraftRoute) {
      return this.articleService.fetchArticlesByAuthorAndTitle(this.accountService.getUsername(), title, this.currentPage, size);
    }
    return this.articleService.fetchDraftsByAuthorAndTitle(this.accountService.getUsername(), title, this.currentPage, size);
  }

  /**
   * resolve article response. extract articles list and page information
   * @param articlesResponse article response:
   *  {
   *    articles: Article[];
   *    responsePage:{
   *      totalElements: number;
   *      size: number;
   *      number: number; // current page
   *    }
   *  }
   * @private
   */
  private resolveArticlesResponse(articlesResponse: GetArticlesResponse): void {
    this.articles = articlesResponse.articles;
    this.totalArticles = articlesResponse.responsePage.totalElements;
    this.pageSize = articlesResponse.responsePage.size;
    this.currentPage = articlesResponse.responsePage.number;
  }

  /**
   * view selected article.
   * @param selectedArticle selected article.
   * @private
   */
  private viewArticle(selectedArticle: Article): void {
    if (this.selectedArticle == null) {
      return;
    }
    if (this.isDraftRoute) {
      this.router.navigate([`/account/drafts/${this.selectedArticle.id}`], {relativeTo: this.activateRoute});
    } else {
      this.router.navigate([`/account/articles/${this.selectedArticle.id}`]);
    }
  }
}
