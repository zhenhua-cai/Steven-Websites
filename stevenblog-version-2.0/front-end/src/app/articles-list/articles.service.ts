import {Injectable} from '@angular/core';
import {Article} from '../shared/Article';
import {BehaviorSubject, Observable, Subject} from 'rxjs';
import {DataTransactionService, GetArticlesResponse} from '../shared/data-transaction.service';

@Injectable({
  providedIn: 'root'
})
export class ArticlesService {
  articles: Article[] = [];
  articlesChangeEvent = new Subject<Article[]>();
  articleUpdateEvent = new Subject<Article>();
  searchTitleEvent = new BehaviorSubject<string>(null);

  constructor(private dataTransaction: DataTransactionService) {
  }

  fetchArticles(page: number, pageSize: number): Observable<GetArticlesResponse> {
    this.clearSearchTitle();
    return this.dataTransaction.fetchArticles(page, pageSize);
  }

  searchArticleByTitle(searchValue: string, page: number, size: number): Observable<GetArticlesResponse> {
    this.searchTitleEvent.next(searchValue);
    return this.dataTransaction.fetchArticlesByTitle(searchValue, page, size);
  }

  searchArticleById(articleId: string): Observable<Article> {
    this.clearSearchTitle();
    return this.dataTransaction.fetchArticleById(articleId);
  }

  searchArticleDraftById(id: string): Observable<Article> {
    return this.dataTransaction.fetchArticleDraftById(id);
  }

  clearSearchTitle(): void {
    this.searchTitleEvent.next(null);
  }

  fetchArticlesByAuthorAndTitle(author: string, title: string, page: number, size: number): Observable<GetArticlesResponse> {
    return this.dataTransaction.fetchArticlesByAuthorAndTitle(author, title, page, size);
  }

  fetchArticlesByAuthorAndTitleOrderBy(author: string, title: string, sortField: string,
                                       sortOrder: number, page: number, size: number): Observable<GetArticlesResponse> {
    return this.dataTransaction.fetchArticlesByAuthorAndTitleOrderBy(author, title, sortField, sortOrder, page, size);
  }

  fetchDraftsByAuthorAndTitleOrderBy(author: string, title: string, sortField: string,
                                     sortOrder: number, page: number, size: number): Observable<GetArticlesResponse> {
    return this.dataTransaction.fetchDraftsByAuthorAndTitleOrderBy(author, title, sortField, sortOrder, page, size);
  }

  fetchDraftsByAuthorAndTitle(author: string, title: string, page: number, size: number): Observable<GetArticlesResponse> {
    return this.dataTransaction.fetchDraftsByAuthorAndTitle(author, title, page, size);
  }

}

interface Page {
  size: number;
  totalElements: number;
  totalPages: number;
  number: number;
}
