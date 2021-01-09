import {Injectable} from '@angular/core';
import {
  Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import {Observable} from 'rxjs';
import {ArticlesPageResponse} from '../shared/data-transaction.service';
import {ArticlesService} from './articles.service';

@Injectable({
  providedIn: 'root'
})
export class SearchArticlesResolver implements Resolve<ArticlesPageResponse> {

  constructor(private articlesService: ArticlesService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ArticlesPageResponse> {
    return this.articlesService.searchArticleByTitle(route.params.ArticlePublishTitle, 0, 10);
  }
}
