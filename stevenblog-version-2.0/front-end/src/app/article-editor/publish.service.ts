import {Injectable} from '@angular/core';
import {BehaviorSubject, Subject} from 'rxjs';
import {Article} from '../shared/Article';
import {ArticlesService} from '../articles-list/articles.service';

@Injectable({
  providedIn: 'root'
})
export class PublishService {
  articlePublishCompleteEvent = new Subject<boolean>();
  articlePublishEvent = new BehaviorSubject<Article>(null);
  isInPublishProgress = false;
  showPopUpBeforeQuitEvent = new Subject<boolean>();

  constructor(private articlesService: ArticlesService) {
  }

  startsPublishArticle(article: Article): void {
    if (article != null) {
      this.isInPublishProgress = true;
    }
    this.updatePublishArticle(article);
  }

  updatePublishArticle(article: Article): void {
    this.articlePublishEvent.next(article);
  }

  showPopUpBeforeQuit(value): void {
    this.showPopUpBeforeQuitEvent.next(value);
  }

  publishComplete(): void {
    this.isInPublishProgress = false;
    this.articlesService.setArticlePublished(true);
  }
}
