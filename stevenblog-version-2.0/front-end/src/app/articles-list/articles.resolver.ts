import {Injectable} from '@angular/core';
import {
  Router, Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import {Observable, of} from 'rxjs';
import {ArticlesService} from './articles.service';
import {GetArticlesResponse} from '../shared/data-transaction.service';

@Injectable({
  providedIn: 'root'
})
export class ArticlesResolver implements Resolve<GetArticlesResponse> {

  constructor(private articlesService: ArticlesService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<GetArticlesResponse> {
    return this.articlesService.fetchArticles(0, 10);
  }
}
