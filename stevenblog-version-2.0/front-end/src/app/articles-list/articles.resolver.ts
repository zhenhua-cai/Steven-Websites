import {Injectable} from '@angular/core';
import {
  Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import {Observable} from 'rxjs';
import {ArticlesService} from './articles.service';
import {ArticlesPageResponse} from '../shared/data-transaction.service';

@Injectable({
  providedIn: 'root'
})
export class ArticlesResolver implements Resolve<ArticlesPageResponse> {

  constructor(private articlesService: ArticlesService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ArticlesPageResponse> {
    return this.articlesService.fetchArticles(0, 10);
  }
}
