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

  fetchArticles(page: number, pageSize: number): Observable<GetArticlesResponse> {
    const url = `/api/articles/?page=${page}&size=${pageSize}`;
    return this.http.get<GetArticlesResponse>(url);
  }

  fetchArticleById(articleId: string): Observable<Article> {
    const url = environment.serverUrl + `articles/${articleId}`;
    return this.http.get<Article>(url);
  }

  fetchArticleDraftById(id: string): Observable<Article> {
    const url = environment.serverUrl + `drafts/${id}`;
    return this.http.get<Article>(url);
  }

  fetchArticlesByTitle(searchValue: string, page: number, pageSize: number): Observable<GetArticlesResponse> {
    const url = `/api/articles/search?title=${searchValue}&page=${page}&size=${pageSize}`;
    return this.http.get<GetArticlesResponse>(url);
  }

  login(loginUser: AttemptLoginUser): Observable<any> {
    const url = `/api/account/login`;
    return this.http.post(url, loginUser, {
      withCredentials: true
    });
  }

  fetchArticlesByAuthorAndTitle(author: string, title: string, page: number, size: number): Observable<GetArticlesResponse> {
    let url = `/api/articles/search?author=${author}&page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.http.get<GetArticlesResponse>(url);
  }

  fetchArticlesByAuthorAndTitleOrderBy(author: string, title: string, sortField: string,
                                       sortOrder: number, page: number, size: number): Observable<GetArticlesResponse> {
    let url = `/api/articles/search?author=${author}&sortBy=${sortField}&sortOrder=${sortOrder}&page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.http.get<GetArticlesResponse>(url);
  }

  fetchDraftsByAuthorAndTitleOrderBy(author: string, title: string, sortField: string,
                                     sortOrder: number, page: number, size: number): Observable<GetArticlesResponse> {
    let url = `/api/drafts/search?author=${author}&sortBy=${sortField}&sortOrder=${sortOrder}&page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.http.get<GetArticlesResponse>(url);
  }

  fetchDraftsByAuthorAndTitle(author: string, title: string, page: number, size: number): Observable<GetArticlesResponse> {
    let url = `/api/drafts/search?author=${author}&page=${page}&size=${size}`;
    if (title) {
      url += `&title=${title}`;
    }
    return this.http.get<GetArticlesResponse>(url);
  }


}

export interface GetArticlesResponse {
  articles: any[];
  responsePage: {
    totalElements: number;
    number: number;
    size: 10;
    totalPages: number;
  };
}
