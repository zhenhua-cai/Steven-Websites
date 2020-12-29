import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {Article} from './Article';
import {AttemptLoginUser} from './ApplicationUser.model';

@Injectable({
  providedIn: 'root'
})
export class DataTransactionService {

  constructor(private http: HttpClient) {
  }

  fetchArticles(page: number, pageSize: number): Observable<ArticlesPageResponse> {
    const url = `/api/articles/?page=${page}&size=${pageSize}`;
    return this.http.get<ArticlesPageResponse>(url);
  }

  fetchArticleById(articleId: string): Observable<ArticleResponse> {
    const url = environment.serverUrl + `articles/${articleId}`;
    return this.http.get<ArticleResponse>(url);
  }

  fetchArticleToEditById(articleId: string): Observable<ArticleResponse> {
    const url = environment.serverUrl + `articles/edit/${articleId}`;
    return this.http.get<ArticleResponse>(url);
  }

  fetchArticleDraftById(id: string): Observable<ArticleResponse> {
    const url = environment.serverUrl + `drafts/${id}`;
    return this.http.get<ArticleResponse>(url);
  }

  fetchArticlesByTitle(searchValue: string, page: number, pageSize: number): Observable<ArticlesPageResponse> {
    const url = `/api/articles/search?title=${searchValue}&page=${page}&size=${pageSize}`;
    return this.http.get<ArticlesPageResponse>(url);
  }

  login(loginUser: AttemptLoginUser): Observable<any> {
    const url = `/api/account/login`;
    return this.http.post(url, loginUser);
  }

  fetchArticlesByAuthorAndTitle(author: string, title: string, page: number, size: number): Observable<ArticlesPageResponse> {
    let url = `/api/articles/search?author=${author}&page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.http.get<ArticlesPageResponse>(url);
  }

  fetchArticlesByAuthorAndTitleOrderBy(author: string, title: string, sortField: string,
                                       sortOrder: number, page: number, size: number): Observable<ArticlesPageResponse> {
    let url = `/api/articles/search?author=${author}&sortBy=${sortField}&sortOrder=${sortOrder}&page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.http.get<ArticlesPageResponse>(url);
  }

  fetchDraftsByAuthorAndTitleOrderBy(author: string, title: string, sortField: string,
                                     sortOrder: number, page: number, size: number): Observable<ArticlesPageResponse> {
    let url = `/api/drafts/search?author=${author}&sortBy=${sortField}&sortOrder=${sortOrder}&page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.http.get<ArticlesPageResponse>(url);
  }

  fetchDraftsByAuthorAndTitle(author: string, title: string, page: number, size: number): Observable<ArticlesPageResponse> {
    let url = `/api/drafts/search?author=${author}&page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.http.get<ArticlesPageResponse>(url);
  }


  saveArticleDraft(article: Article): Observable<ArticleResponse> {
    const url = `/api/drafts/save`;
    return this.http.post<ArticleResponse>(url, article);
  }

  deleteArticleDraftById(articleId: string): Observable<ActionStatusResponse> {
    const url = `/api/drafts/delete/${articleId}`;
    return this.http.delete<ActionStatusResponse>(url);
  }

  deleteArticleById(articleId: string): Observable<ActionStatusResponse> {
    const url = `/api/articles/delete/${articleId}`;
    return this.http.delete<ActionStatusResponse>(url);
  }

  publishArticle(article: Article): Observable<ArticleResponse> {
    const url = `/api/articles/publish`;
    return this.http.post<ArticleResponse>(url, article);
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
