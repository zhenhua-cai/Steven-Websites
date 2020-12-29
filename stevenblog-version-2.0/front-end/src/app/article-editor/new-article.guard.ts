import {Injectable} from '@angular/core';
import {CanActivate, CanDeactivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {ArticleEditorService} from './article-editor.service';

@Injectable({
  providedIn: 'root'
})
export class NewArticleGuard implements CanActivate, CanDeactivate<unknown> {
  constructor(private articleEditorService: ArticleEditorService) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    this.articleEditorService.setNewArticle(true);
    return true;
  }

  canDeactivate(
    component: unknown,
    currentRoute: ActivatedRouteSnapshot,
    currentState: RouterStateSnapshot,
    nextState?: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    this.articleEditorService.setNewArticle(false);
    return true;
  }
}
