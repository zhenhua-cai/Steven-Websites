import {Injectable} from '@angular/core';
import {Article} from '../shared/Article';
import {BehaviorSubject, Observable, Subject} from 'rxjs';
import {ActionStatusResponse, DataTransactionService, ArticlesPageResponse, ArticleResponse} from '../shared/data-transaction.service';

@Injectable({
  providedIn: 'root'
})
export class ArticlesService {
  articles: Article[] = [];
  articlesChangeEvent = new Subject<Article[]>();
  articleUpdateEvent = new Subject<Article>();
  searchTitleEvent = new BehaviorSubject<string>(null);
  publishedArticleEvent = new BehaviorSubject<boolean>(null);

  constructor(private dataTransaction: DataTransactionService) {
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

}

interface Page {
  size: number;
  totalElements: number;
  totalPages: number;
  number: number;
}
