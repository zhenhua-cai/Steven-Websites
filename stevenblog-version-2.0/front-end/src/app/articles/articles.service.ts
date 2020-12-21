import {Injectable} from '@angular/core';
import {Article} from '../shared/Article';
import {Subject} from 'rxjs';
import {DataTransactionService} from '../shared/data-transaction.service';

@Injectable({
  providedIn: 'root'
})
export class ArticlesService {
  articles: Article[] = [];
  articlesChangeEvent = new Subject<Article[]>();
  articleSearchPageChangeEvent = new Subject<Page>();
  articleUpdateEvent = new Subject<Article>();

  constructor(private dataTransaction: DataTransactionService) {
  }

  fetchArticles(page: number, pageSize: number): void {
    this.dataTransaction.fetchArticles(page, pageSize).subscribe(
      (data) => {
        this.fireArticlesChangeEvent(data.articles, data.responsePage);
      }
    );
  }

  searchArticleByTitle(searchValue: string, page: number, size: number): void {
    this.dataTransaction.fetchArticlesByTitle(searchValue, page, size).subscribe(
      (data) => {
        this.fireArticlesChangeEvent(data.articles, data.responsePage);
      }
    );
  }

  fireArticlesChangeEvent(articles: Article[], page: Page): void {
    this.articlesChangeEvent.next(articles);
    this.articleSearchPageChangeEvent.next(page);
  }

  searchArticleById(articleId: string): void {
    this.dataTransaction.fetchArticleById(articleId).subscribe(
      (article) => {
        this.articleUpdateEvent.next(article);
      }
    );
  }
}

interface Page {
  size: number;
  totalElements: number;
  totalPages: number;
  number: number;
}
