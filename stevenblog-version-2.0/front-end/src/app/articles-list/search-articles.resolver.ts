import {Injectable} from '@angular/core';
import {
  Router, Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import {Observable, of, throwError} from 'rxjs';
import {ArticlesPageResponse} from '../shared/data-transaction.service';
import {ArticlesService} from './articles.service';
import {catchError} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class SearchArticlesResolver implements Resolve<ArticlesPageResponse> {

  constructor(private articlesService: ArticlesService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ArticlesPageResponse> {
    return this.articlesService.searchArticleByTitle(route.params.title, 0, 10);
  }
}
