import {Injectable} from '@angular/core';
import {
  Router, Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import {Observable, of} from 'rxjs';
import {Article} from '../shared/Article';
import {ArticlesService} from '../articles-list/articles.service';
import {ArticleResponse} from '../shared/data-transaction.service';

@Injectable({
  providedIn: 'root'
})
export class ArticleResolver implements Resolve<ArticleResponse> {

  constructor(private articlesService: ArticlesService) {
    this.articlesService = articlesService;
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ArticleResponse> {
    return this.articlesService.searchArticleById(route.params.id);
  }
}
