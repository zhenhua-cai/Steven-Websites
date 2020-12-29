import {Injectable} from '@angular/core';
import {
  Router, Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import {Observable, of} from 'rxjs';
import {ArticlesService} from '../articles-list/articles.service';
import {Article} from '../shared/Article';
import {ArticleResponse} from '../shared/data-transaction.service';

@Injectable({
  providedIn: 'root'
})
export class DraftResolver implements Resolve<ArticleResponse> {
  constructor(private articlesService: ArticlesService) {
    this.articlesService = articlesService;
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ArticleResponse> {
    return this.articlesService.searchArticleDraftById(route.params.id);
  }
}
