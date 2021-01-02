import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {Observable, throwError} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {catchError} from 'rxjs/operators';
import {Article} from './Article';
import {AttemptLoginUser} from './ApplicationUser.model';
import {AppService} from '../app.service';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class DataTransactionService {

  constructor(private http: HttpClient, private appService: AppService, private router: Router) {
  }

  fetchArticles(page: number, pageSize: number): Observable<ArticlesPageResponse> {
    const url = `/api/articles/?page=${page}&size=${pageSize}`;
    return this.sendGetArticlesPagesResponseRequest(url);
  }

  fetchArticleById(articleId: string): Observable<ArticleResponse> {
    const url = environment.serverUrl + `articles/${articleId}`;
    return this.sendGetArticleResponseRequest(url);
  }

  private sendGetArticleResponseRequest(url: string): Observable<ArticleResponse> {
    return this.http.get<ArticleResponse>(url).pipe(
      catchError(errRes => {
        return this.handleErrorResponse(errRes);
      })
    );
  }

  fetchArticleToEditById(articleId: string): Observable<ArticleResponse> {
    const url = environment.serverUrl + `articles/edit/${articleId}`;
    return this.sendGetArticleResponseRequest(url);
  }

  fetchArticleDraftById(id: string): Observable<ArticleResponse> {
    const url = environment.serverUrl + `drafts/${id}`;
    return this.sendGetArticleResponseRequest(url);
  }

  fetchArticlesByTitle(searchValue: string, page: number, pageSize: number): Observable<ArticlesPageResponse> {
    const url = `/api/articles/search?title=${searchValue}&page=${page}&size=${pageSize}`;
    return this.sendGetArticlesPagesResponseRequest(url);
  }

  login(loginUser: AttemptLoginUser): Observable<AuthResponse> {
    const url = `/api/auth/login`;
    return this.http.post<AuthResponse>(url, loginUser).pipe(
      catchError(errRes => {
        return this.handleErrorResponse(errRes);
      })
    );
  }
  // fetch my articles/drafts methods
  fetchMyArticlesByTitle(title: string, page: number, size: number): Observable<ArticlesPageResponse> {
    let url = `/api/account/articles/search?page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.sendGetArticlesPagesResponseRequest(url);
  }

  fetchMyDraftsByTitle(title: string, page: number, size: number): Observable<ArticlesPageResponse> {
    let url = `/api/account/drafts/search?page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.sendGetArticlesPagesResponseRequest(url);
  }

  fetchMyArticlesByTitleOrderBy(title: string, sortField: string,
                                sortOrder: number, page: number, size: number): Observable<ArticlesPageResponse> {
    let url = `/api/account/articles/search?sortBy=${sortField}&sortOrder=${sortOrder}&page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.sendGetArticlesPagesResponseRequest(url);
  }

  fetchMyDraftsByTitleOrderBy(title: string, sortField: string,
                              sortOrder: number, page: number, size: number): Observable<ArticlesPageResponse> {
    let url = `/api/account/drafts/search?sortBy=${sortField}&sortOrder=${sortOrder}&page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.sendGetArticlesPagesResponseRequest(url);
  }
  // end of fetch my articles/drafts methods

  private sendGetArticlesPagesResponseRequest(url: string): Observable<ArticlesPageResponse> {
    return this.http.get<ArticlesPageResponse>(url).pipe(
      catchError(errRes => {
        return this.handleErrorResponse(errRes);
      })
    );
  }

  fetchArticlesByAuthorAndTitle(author: string, title: string, page: number, size: number): Observable<ArticlesPageResponse> {
    let url = `/api/articles/search?author=${author}&page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.sendGetArticlesPagesResponseRequest(url);
  }

  fetchArticlesByAuthorAndTitleOrderBy(author: string, title: string, sortField: string,
                                       sortOrder: number, page: number, size: number): Observable<ArticlesPageResponse> {
    let url = `/api/articles/search?author=${author}&sortBy=${sortField}&sortOrder=${sortOrder}&page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.sendGetArticlesPagesResponseRequest(url);
  }


  fetchDraftsByAuthorAndTitleOrderBy(author: string, title: string, sortField: string,
                                     sortOrder: number, page: number, size: number): Observable<ArticlesPageResponse> {
    let url = `/api/drafts/search?author=${author}&sortBy=${sortField}&sortOrder=${sortOrder}&page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.sendGetArticlesPagesResponseRequest(url);
  }

  fetchDraftsByAuthorAndTitle(author: string, title: string, page: number, size: number): Observable<ArticlesPageResponse> {
    let url = `/api/drafts/search?author=${author}&page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.sendGetArticlesPagesResponseRequest(url);
  }

  saveArticleDraft(article: Article): Observable<ArticleResponse> {
    const url = `/api/drafts/save`;
    return this.sendPostArticleResponseRequest(url, article);
  }

  deleteArticleDraftById(articleId: string): Observable<ActionStatusResponse> {
    const url = `/api/drafts/delete/${articleId}`;
    return this.aendDeleteActionStatusResponseRequest(url);
  }

  deleteArticleById(articleId: string): Observable<ActionStatusResponse> {
    const url = `/api/articles/delete/${articleId}`;
    return this.aendDeleteActionStatusResponseRequest(url);
  }

  private aendDeleteActionStatusResponseRequest(url: string): Observable<ActionStatusResponse> {
    return this.http.delete<ActionStatusResponse>(url).pipe(
      catchError(errRes => {
        return this.handleErrorResponse(errRes);
      })
    );
  }

  publishArticle(article: Article): Observable<ArticleResponse> {
    const url = `/api/articles/publish`;
    return this.sendPostArticleResponseRequest(url, article);
  }

  private sendPostArticleResponseRequest(url: string, article: Article): Observable<ArticleResponse> {
    return this.http.post<ArticleResponse>(url, article).pipe(
      catchError(errRes => {
        return this.handleErrorResponse(errRes);
      })
    );
  }


  logout(): Promise<boolean> {
    return this.router.navigate(['/login']);
  }

  regainAccessToken(): Observable<AuthResponse> {
    const refreshToken = this.appService.getRefreshToken();
    return this.gainAccessTokenByRefreshToken(refreshToken);
  }

  private handleErrorResponse(errRes): Observable<any> {
    const error = errRes.error;
    let summary = null;
    if (errRes.status === 401) {
      return throwError(errRes);
    }
    if (errRes.status === 403) {
      summary = 'Access Denied';
      this.router.navigate(['/error/access-denied']);
    } else if (errRes.status === 500) {
      summary = 'Unknown error';
    } else {
      summary = 'Error';
    }
    this.appService.showErrorToast(summary, error.message);
    return throwError(errRes);
  }

  private gainAccessTokenByRefreshToken(refreshToken: string): Observable<AuthResponse> {
    if (!refreshToken) {
      this.appService.unblockScreen();
      this.reLoginAfterAuthFail();
      return throwError('Auth Fail');
    }
    const url = `/api/auth/accessToken`;
    return this.http.post<AuthResponse>(url, {refreshToken});
  }

  private reLoginAfterAuthFail(): void {
    this.appService.clearAuthInfo();
    this.appService.showErrorToast('Authentication Failed', 'Please login.');
    this.router.navigate(['/login']).then();
  }
}

export interface ArticlesPageResponse {
  articles: any[];
  responsePage: {
    totalElements: number;
    number: number;
    size: 10;
    totalPages: number;
  };
}

export interface ActionStatusResponse {
  status: boolean;
}

export interface ArticleResponse {
  articleResource: Article;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  roles: [];
}

