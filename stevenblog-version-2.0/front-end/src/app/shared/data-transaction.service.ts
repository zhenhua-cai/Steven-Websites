import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {Observable, throwError} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {catchError} from 'rxjs/operators';
import {Article} from './Article';
import {AttemptLoginUser} from './ApplicationUser.model';
import {AppService} from '../app.service';

@Injectable({
  providedIn: 'root'
})
export class DataTransactionService {

  constructor(private http: HttpClient, private appService: AppService) {
  }

  fetchArticles(page: number, pageSize: number): Observable<ArticlesPageResponse> {
    const url = `/api/articles/?page=${page}&size=${pageSize}`;
    return this.http.get<ArticlesPageResponse>(url).pipe(
      catchError(errRes => {
        return this.handleErrorResponse(errRes);
      })
    );
  }

  fetchArticleById(articleId: string): Observable<ArticleResponse> {
    const url = environment.serverUrl + `articles/${articleId}`;
    return this.http.get<ArticleResponse>(url).pipe(
      catchError(errRes => {
        return this.handleErrorResponse(errRes);
      })
    );
  }

  fetchArticleToEditById(articleId: string): Observable<ArticleResponse> {
    const url = environment.serverUrl + `articles/edit/${articleId}`;
    return this.http.get<ArticleResponse>(url).pipe(
      catchError(errRes => {
        return this.handleErrorResponse(errRes);
      })
    );
  }

  fetchArticleDraftById(id: string): Observable<ArticleResponse> {
    const url = environment.serverUrl + `drafts/${id}`;
    return this.http.get<ArticleResponse>(url).pipe(
      catchError(errRes => {
        return this.handleErrorResponse(errRes);
      })
    );
  }

  fetchArticlesByTitle(searchValue: string, page: number, pageSize: number): Observable<ArticlesPageResponse> {
    const url = `/api/articles/search?title=${searchValue}&page=${page}&size=${pageSize}`;
    return this.http.get<ArticlesPageResponse>(url).pipe(
      catchError(errRes => {
        return this.handleErrorResponse(errRes);
      })
    );
  }

  login(loginUser: AttemptLoginUser): Observable<AuthResponse> {
    const url = `/api/auth/login`;
    return this.http.post<AuthResponse>(url, loginUser).pipe(
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
    return this.http.get<ArticlesPageResponse>(url).pipe(
      catchError(errRes => {
        return this.handleErrorResponse(errRes);
      })
    );
  }

  fetchArticlesByAuthorAndTitleOrderBy(author: string, title: string, sortField: string,
                                       sortOrder: number, page: number, size: number): Observable<ArticlesPageResponse> {
    let url = `/api/articles/search?author=${author}&sortBy=${sortField}&sortOrder=${sortOrder}&page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.http.get<ArticlesPageResponse>(url).pipe(
      catchError(errRes => {
        return this.handleErrorResponse(errRes);
      })
    );
  }

  fetchDraftsByAuthorAndTitleOrderBy(author: string, title: string, sortField: string,
                                     sortOrder: number, page: number, size: number): Observable<ArticlesPageResponse> {
    let url = `/api/drafts/search?author=${author}&sortBy=${sortField}&sortOrder=${sortOrder}&page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.http.get<ArticlesPageResponse>(url).pipe(
      catchError(errRes => {
        return this.handleErrorResponse(errRes);
      })
    );
  }

  fetchDraftsByAuthorAndTitle(author: string, title: string, page: number, size: number): Observable<ArticlesPageResponse> {
    let url = `/api/drafts/search?author=${author}&page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.http.get<ArticlesPageResponse>(url).pipe(
      catchError(errRes => {
        return this.handleErrorResponse(errRes);
      })
    );
  }


  saveArticleDraft(article: Article): Observable<ArticleResponse> {
    const url = `/api/drafts/save`;
    return this.http.post<ArticleResponse>(url, article);
  }

  deleteArticleDraftById(articleId: string): Observable<ActionStatusResponse> {
    const url = `/api/drafts/delete/${articleId}`;
    return this.http.delete<ActionStatusResponse>(url).pipe(
      catchError(errRes => {
        return this.handleErrorResponse(errRes);
      })
    );
  }

  deleteArticleById(articleId: string): Observable<ActionStatusResponse> {
    const url = `/api/articles/delete/${articleId}`;
    return this.http.delete<ActionStatusResponse>(url).pipe(
      catchError(errRes => {
        return this.handleErrorResponse(errRes);
      })
    );
  }

  publishArticle(article: Article): Observable<ArticleResponse> {
    const url = `/api/articles/publish`;
    return this.http.post<ArticleResponse>(url, article).pipe(
      catchError(errRes => {
        return this.handleErrorResponse(errRes);
      })
    );
  }

  private handleErrorResponse(errRes): Observable<any> {
    const error = errRes.error;
    let summary = null;
    if (errRes.status === 401) {
      summary = 'Auth Failed';
    } else if (errRes.status === 403) {
      summary = 'Access Denied';
    } else if (errRes.status === 500) {
      summary = 'Unknown error';
    } else {
      summary = 'Failed';
    }
    this.appService.showErrorToast(summary, error.message);
    return throwError(errRes);
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
  jwt: string;
  roles: [];
}
