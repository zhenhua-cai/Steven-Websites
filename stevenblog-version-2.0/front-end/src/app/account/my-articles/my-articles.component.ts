import {Component, OnDestroy, OnInit} from '@angular/core';
import {AccountService} from '../account.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Article} from '../../shared/Article';
import {ActionStatusResponse, ArticlesPageResponse} from '../../shared/data-transaction.service';
import {ArticlesService} from '../../articles-list/articles.service';
import {LazyLoadEvent, MenuItem} from 'primeng/api';
import {Observable} from 'rxjs';
import {ArticleEditorService} from '../../article-editor/article-editor.service';
import {AppService} from '../../app.service';

@Component({
  selector: 'app-my-articles',
  templateUrl: './my-articles.component.html',
  styleUrls: ['./my-articles.component.css']
})
export class MyArticlesComponent implements OnInit, OnDestroy {

  articles: Article[];
  totalArticles: number;
  pageSize = 10;
  currentPage = 0;
  isLoading = true;
  selectedArticle: Article = null;
  items: MenuItem[];
  isDraftRoute = false;
  searchValue: string;
  searchOrderBy: string = null;
  searchOrder = -1;

  constructor(private articleService: ArticlesService,
              private accountService: AccountService,
              private router: Router,
              private appService: AppService,
              private activateRoute: ActivatedRoute,
              private articleEditorService: ArticleEditorService) {
  }

  ngOnDestroy(): void {
  }


  ngOnInit(): void {
    this.accountService.accountRouteChange(this.router.url);
    this.items = [
      {label: 'View', icon: 'pi pi-fw pi-search', command: () => this.viewArticle(this.selectedArticle)},
      {label: 'Edit', icon: 'far pi-fw fa-edit', command: () => this.onEditArticle()},
      {label: 'Delete', icon: 'far pi-fw fa-trash-alt', command: () => this.onDeleteArticle()}
    ];
    this.isDraftRoute = this.router.url !== '/account/articles';
    this.articleEditorService.disableEditor();
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
    this.searchOrderBy = event.sortField;
    this.searchOrder = event.sortOrder;
    this.pageSize = event.rows;
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

  onDeleteArticle(): void {
    if (this.selectedArticle == null) {
      return;
    }
    this.articleEditorService.confirm('Delete Confirmation',
      'Delete cannot be undone. Do you want to continue?',
      'fas fa-skull-crossbones', 'p-button-danger',
      this.deleteArticle.bind(this)
    );
  }

  deleteArticle(): void {
    if (this.selectedArticle == null) {
      return;
    }
    let deleteResponse: Observable<ActionStatusResponse>;
    if (this.isDraftRoute) {
      deleteResponse = this.articleService.deleteArticleDraftById(this.selectedArticle.id);
    } else {
      deleteResponse = this.articleService.deleteArticleById(this.selectedArticle.id);
    }
    deleteResponse.subscribe(
      (response) => {
        if (response.status) {
          this.showDeleteArticleSuccessMsg();
          this.isLoading = true;
          this.updateCurrentAfterDelete();
        } else {
          this.showDeleteArticleFailMsg();
        }
      }, error => {
        this.articleService.handle401Error(error, this.deleteArticle.bind(this));
      }
    );
  }

  private updateCurrentAfterDelete(): void {
    this.updateCurrentPage().subscribe(
      (responseData) => {
        this.resolveArticlesResponse(responseData);
        this.isLoading = false;
      }, error => {
        this.articleService.handle401Error(error, this.updateCurrentAfterDelete.bind(this));
      }
    );
  }

  onEditArticle(): void {
    if (this.selectedArticle == null) {
      return;
    }
    this.articleEditorService.confirm('Edit Confirmation',
      'Do you want to edit this article?',
      'far fa-edit', 'p-button-warning',
      this.editArticle.bind(this)
    );
  }

  editArticle(): void {
    this.articleEditorService.isAbleToAccessEditor = true;
    if (this.isDraftRoute) {
      this.articleEditorService.setEditingDraft(true);
    } else {
      this.articleEditorService.setEditingDraft(false);
    }
    this.router.navigate([`/account/edit/${this.selectedArticle.id}`]);
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
   * search articles by search value(ArticlePublishTitle). order by certain field -- asc or desc
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
        }, error => {
          this.articleService.handle401Error(error, this.searchArticlesBy.bind(this), title, orderBy, order, pageSize);
        });
    } else {
      // sort field not present, by default, sort by last modified date.
      this.searchArticlesByAuthorAndTitle(title, pageSize)
        .subscribe(
          (articlesResponse) => {
            this.resolveArticlesResponse(articlesResponse);
            this.isLoading = false;
          }, error => {
            this.articleService.handle401Error(error, this.searchArticlesBy.bind(this), title, orderBy, order, pageSize);
          });
    }
  }

  /**
   * search article by author name and order by certain field.
   * if this page is in draft route. then it searches ArticleDraft instead.
   * * @param size size of the searching page.
   * @param title article ArticlePublishTitle to be searched.
   * @param sortField order by field.
   * @param sortOrder 1 - asc, -1 desc. desc by default.
   * @private
   */
  private searchArticlesByAuthorAndTitleOrderBy(title: string, sortField: string,
                                                sortOrder: number, size: number): Observable<ArticlesPageResponse> {
    return this.updateTablePage(title, sortField, sortOrder, this.currentPage, size);
  }

  /**
   * search article by author name. if this page is in draft route. then it searches ArticleDraft instead.
   * result will be order by last modified date time by default.
   * @param title article ArticlePublishTitle to be searched
   * @param size size of the searching page.
   * @private
   */
  private searchArticlesByAuthorAndTitle(title: string, size: number): Observable<ArticlesPageResponse> {
    if (!this.isDraftRoute) {
      return this.articleService.fetchMyArticlesByTitle(title, this.currentPage, size);
    }
    return this.articleService.fetchMyDraftsByTitle(title, this.currentPage, size);
  }

  private updateCurrentPage(): Observable<ArticlesPageResponse> {
    if (this.searchOrder == null) {
      return this.searchArticlesByAuthorAndTitle(this.searchValue, this.pageSize);
    }
    return this.updateTablePage(this.searchValue, this.searchOrderBy, this.searchOrder, this.currentPage, this.pageSize);
  }

  private updateTablePage(title: string, sortField: string,
                          sortOrder: number, page: number, size: number): Observable<ArticlesPageResponse> {
    if (!this.isDraftRoute) {
      return this.articleService
        .fetchMyArticlesByTitleOrderBy(title, sortField,
          sortOrder, page, size);
    }
    return this.articleService.fetchMyDraftsByTitleOrderBy(title,
      sortField, sortOrder, page, size);
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
  private resolveArticlesResponse(articlesResponse: ArticlesPageResponse): void {
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

  createNewArticle($event: MouseEvent): void {
    this.articleEditorService.isAbleToAccessEditor = true;
    this.router.navigate(['/account/new']);
  }

  private showDeleteArticleFailMsg(): void {
    this.appService.showErrorToast('Delete Article Failed', 'Unable to delete article. Please try later');
  }

  private showDeleteArticleSuccessMsg(): void {
    let summary;
    let details;
    if (this.isDraftRoute) {
      summary = 'Article Draft Deleted';
      details = 'Successfully deleted article draft.';
    } else {
      summary = 'Article Deleted';
      details = 'Successfully deleted article.';
    }
    this.appService.showSuccessToast(summary, details);
  }
  getClientDateTime(date: string): Date {
    return this.appService.getClientDateTime(new Date(date));
  }
}
