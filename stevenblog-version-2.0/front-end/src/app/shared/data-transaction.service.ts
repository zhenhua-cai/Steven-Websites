import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';
import {Article} from './Article';

@Injectable({
  providedIn: 'root'
})
export class DataTransactionService {

  constructor(private http: HttpClient) {
  }

  fetchArticles(page: number, pageSize: number): Observable<GetArticlesResponse> {
    const url = environment.serverUrl + `articles/?page=${page}&size=${pageSize}`;
    return this.http.get<GetArticlesResponse>(url);
  }

  fetchArticleById(articleId: string): Observable<Article> {
    const url = environment.serverUrl + `articles/${articleId}`;
    return this.http.get<Article>(url);
  }

  fetchArticlesByTitle(searchValue: string, page: number, pageSize: number): Observable<GetArticlesResponse> {
    const url = environment.serverUrl + `articles/search?title=${searchValue}&page=${page}&size=${pageSize}`;
    return this.http.get<GetArticlesResponse>(url);
  }
}

interface GetArticlesResponse {
  articles: any[];
  responsePage: {
    totalElements: number;
    number: number;
    size: 10;
    totalPages: number;
  };
}
