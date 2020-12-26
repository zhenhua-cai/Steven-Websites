import {Injectable} from '@angular/core';
import {
  Router, Resolve,
  RouterStateSnapshot,
  ActivatedRouteSnapshot
} from '@angular/router';
import {Observable, of} from 'rxjs';
import {ArticlesService} from '../articles-list/articles.service';
import {Article} from '../shared/Article';

@Injectable({
  providedIn: 'root'
})
export class DraftResolver implements Resolve<Article> {
  constructor(private articlesService: ArticlesService) {
    this.articlesService = articlesService;
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Article> {
    return this.articlesService.searchArticleDraftById(route.params.id);
  }
}
