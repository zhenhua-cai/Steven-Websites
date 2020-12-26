import {Injectable} from '@angular/core';
import {
  Router, Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import {Observable, of} from 'rxjs';
import {GetArticlesResponse} from '../shared/data-transaction.service';
import {ArticlesService} from './articles.service';

@Injectable({
  providedIn: 'root'
})
export class SearchArticlesResolver implements Resolve<GetArticlesResponse> {

  constructor(private articlesService: ArticlesService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<GetArticlesResponse> {
    return this.articlesService.searchArticleByTitle(route.params.title, 0, 10);
  }
}
