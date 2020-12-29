import {Injectable} from '@angular/core';
import {ConfirmationService, MessageService} from 'primeng/api';
import {Subject} from 'rxjs';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ArticleEditorService {
  editingDraft = true;
  private _isNewArticle = false;
  isAbleToAccessEditor = false;
  editorHasUnsavedData = false;
  showSaveArticlePopupEvent = new Subject<boolean>();
  continueNavigateUrl: string = null;

  constructor(private messageService: MessageService,
              private confirmationService: ConfirmationService,
              private router: Router) {
  }

  continueNavigate(): void {
    if (this.continueNavigateUrl == null) {
      return;
    }
    this.editorHasUnsavedData = false;
    this.router.navigate([this.continueNavigateUrl]);
  }

  setEditingDraft(value: boolean): void {
    this.editingDraft = value;
  }

  disableEditor(): void {
    this.isAbleToAccessEditor = false;
    this.setNewArticle(false);
    this.editorHasUnsavedData = false;
    this.continueNavigateUrl = null;
  }

  showSaveArticlePopupBeforeLeave(): void {
    this.showSaveArticlePopupEvent.next(true);
  }

  isEditingDraft(): boolean {
    return this.editingDraft;
  }

  setNewArticle(value: boolean): void {
    this._isNewArticle = value;
  }

  isNewArticle(): boolean {
    return this._isNewArticle;
  }

  confirm(header: string, msg: string, icon: string,
          acceptButtonStyleClass: string,
          onAcceptFunc: () => void,
          onRejectFunc: () => void = null,
          key: string = 'default'): void {
    this.confirmationService.confirm({
      key,
      header,
      icon,
      message: msg,
      acceptButtonStyleClass,
      accept: onAcceptFunc,
      reject: onRejectFunc
    });
  }

  saveArticleBeforeLeavePopup(): void {
    this.confirmationService.confirm({key: 'saveBeforeLeave'});
  }
}
