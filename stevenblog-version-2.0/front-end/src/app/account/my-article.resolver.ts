import {Injectable} from '@angular/core';
import {
  Router, Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import {Observable, of} from 'rxjs';
import {ArticlesService} from '../articles-list/articles.service';
import {ArticleResponse, AuthResponse} from '../shared/data-transaction.service';
import {catchError} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class MyArticleResolver implements Resolve<ArticleResponse | AuthResponse> {
  constructor(private articlesService: ArticlesService) {
    this.articlesService = articlesService;
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ArticleResponse | AuthResponse> {
    return this.articlesService.searchArticleToEditById(route.params.id).pipe(
      catchError(err => {
        return this.articlesService.regainAccessToken();
      })
    );
  }
}
