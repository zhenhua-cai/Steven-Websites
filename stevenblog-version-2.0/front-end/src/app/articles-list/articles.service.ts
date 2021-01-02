import {Injectable} from '@angular/core';
import {Article} from '../shared/Article';
import {BehaviorSubject, Observable, Subject, throwError} from 'rxjs';
import {
  ActionStatusResponse,
  DataTransactionService,
  ArticlesPageResponse,
  ArticleResponse,
  AuthResponse
} from '../shared/data-transaction.service';
import {AppService} from '../app.service';
import {catchError} from 'rxjs/operators';
import {AuthService} from '../shared/auth.service';

@Injectable({
  providedIn: 'root'
})
export class ArticlesService {
  private MAX_REQUEST_ATTEMPTS_AFTER_401 = 1;
  articles: Article[] = [];
  articlesChangeEvent = new Subject<Article[]>();
  articleUpdateEvent = new Subject<Article>();
  searchTitleEvent = new BehaviorSubject<string>(null);
  publishedArticleEvent = new BehaviorSubject<boolean>(null);
  articlesPageResponseUpdateEvent = new Subject<ArticlesPageResponse>();
  retryAfter401Error = 0;

  constructor(private dataTransaction: DataTransactionService,
              private appService: AppService,
              private authService: AuthService) {
  }


  setArticlePublished(value: boolean): void {
    this.publishedArticleEvent.next(value);
  }

  fetchArticles(page: number, pageSize: number): Observable<ArticlesPageResponse> {
    this.clearSearchTitle();
    return this.dataTransaction.fetchArticles(page, pageSize);
  }

  searchArticleByTitle(searchValue: string, page: number, size: number): Observable<ArticlesPageResponse> {
    this.searchTitleEvent.next(searchValue);
    return this.dataTransaction.fetchArticlesByTitle(searchValue, page, size);
  }

  searchArticleById(articleId: string): Observable<ArticleResponse> {
    this.clearSearchTitle();
    return this.dataTransaction.fetchArticleById(articleId);
  }

  /**
   * this method will try to load article article draft with the same id first.
   * if draft is not present, then try to load article
   * @param articleId article id.
   */
  searchArticleToEditById(articleId: string): Observable<ArticleResponse> {
    return this.dataTransaction.fetchArticleToEditById(articleId);
  }

  searchArticleDraftById(id: string): Observable<ArticleResponse> {
    return this.dataTransaction.fetchArticleDraftById(id);
  }


  clearSearchTitle(): void {
    this.searchTitleEvent.next(null);
  }

  fetchMyArticlesByTitle(title: string, page: number, size: number): Observable<ArticlesPageResponse> {
    return this.dataTransaction.fetchMyArticlesByTitle(title, page, size);
  }

  fetchMyDraftsByTitle(title: string, page: number, size: number): Observable<ArticlesPageResponse> {
    return this.dataTransaction.fetchMyDraftsByTitle(title, page, size);
  }

  fetchMyArticlesByTitleOrderBy(title: string, sortField: string,
                                sortOrder: number, page: number, size: number): Observable<ArticlesPageResponse> {
    return this.dataTransaction.fetchMyArticlesByTitleOrderBy(title, sortField, sortOrder, page, size);
  }

  fetchMyDraftsByTitleOrderBy(title: string, sortField: string,
                              sortOrder: number, page: number, size: number): Observable<ArticlesPageResponse> {
    return this.dataTransaction.fetchMyDraftsByTitleOrderBy(title, sortField, sortOrder, page, size);
  }

  fetchArticlesByAuthorAndTitle(author: string, title: string, page: number, size: number): Observable<ArticlesPageResponse> {
    return this.dataTransaction.fetchArticlesByAuthorAndTitle(author, title, page, size);
  }

  fetchArticlesByAuthorAndTitleOrderBy(author: string, title: string, sortField: string,
                                       sortOrder: number, page: number, size: number): Observable<ArticlesPageResponse> {
    return this.dataTransaction.fetchArticlesByAuthorAndTitleOrderBy(author, title, sortField, sortOrder, page, size);
  }

  fetchDraftsByAuthorAndTitleOrderBy(author: string, title: string, sortField: string,
                                     sortOrder: number, page: number, size: number): Observable<ArticlesPageResponse> {
    return this.dataTransaction.fetchDraftsByAuthorAndTitleOrderBy(author, title, sortField, sortOrder, page, size);
  }

  fetchDraftsByAuthorAndTitle(author: string, title: string, page: number, size: number): Observable<ArticlesPageResponse> {
    return this.dataTransaction.fetchDraftsByAuthorAndTitle(author, title, page, size);
  }

  saveArticleDraft(article: Article): Observable<ArticleResponse> {
    return this.dataTransaction.saveArticleDraft(article);
  }

  deleteArticleDraftById(articleId: string): Observable<ActionStatusResponse> {
    return this.dataTransaction.deleteArticleDraftById(articleId);
  }

  deleteArticleById(articleId: string): Observable<ActionStatusResponse> {
    return this.dataTransaction.deleteArticleById(articleId);
  }

  publishArticle(article: Article): Observable<ArticleResponse> {
    return this.dataTransaction.publishArticle(article);
  }

  regainAccessToken(): Observable<AuthResponse> {
    return this.dataTransaction.regainAccessToken();
  }

  handle401Error(error: any, callback: (...args: any[]) => void, ...args: any[]): void {

    if (error.status === 401) {
      this.regainAccessToken().subscribe(
        (authResponse) => {
          this.appService.storeAuthToken(authResponse.accessToken, authResponse.refreshToken);
          callback(...args);
        },
        catchError(
          (err) => {
            this.appService.unblockScreen();
            this.authService.logout();
            return throwError(err);
          }
        )
      );
    } else {
      this.appService.unblockScreen();
    }
  }
}
