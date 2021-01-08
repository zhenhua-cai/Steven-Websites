import {Injectable} from '@angular/core';
import {CanActivate, CanDeactivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {ArticleEditorService} from './article-editor.service';
import {ArticleEditorComponent} from './article-editor.component';

@Injectable({
  providedIn: 'root'
})
export class ArticleEditorGuard implements CanActivate, CanDeactivate<ArticleEditorComponent> {

  constructor(private articleEditorService: ArticleEditorService, private router: Router) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (!this.articleEditorService.isAbleToAccessEditor) {
      this.router.navigate(['/account/articles']);
    }
    return true;
  }

  canDeactivate(
    component: ArticleEditorComponent,
    currentRoute: ActivatedRouteSnapshot,
    currentState: RouterStateSnapshot,
    nextState?: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.articleEditorService.editorHasUnsavedData) {
      this.articleEditorService.showSaveArticlePopupBeforeLeave();
      this.articleEditorService.continueNavigateUrl = nextState.url;
      return false;
    }
    this.articleEditorService.continueNavigateUrl = null;
    this.articleEditorService.isAbleToAccessEditor = false;
    return true;
  }

}
