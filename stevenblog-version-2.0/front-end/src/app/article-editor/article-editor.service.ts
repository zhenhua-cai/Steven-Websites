import {Injectable} from '@angular/core';
import {ConfirmationService, MessageService} from 'primeng/api';
import {Subject} from 'rxjs';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ArticleEditorService {
  editMode = false;
  editingDraft = true;
  private _isNewArticle = false;
  isAbleToAccessEditor = true;
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
    this.editMode = true;
  }

  setEditMode(value: boolean): void {
    this.editMode = value;
  }

  showSaveArticlePopupBeforeLeave(): void {
    this.showSaveArticlePopupEvent.next(true);
  }

  isEditMode(): boolean {
    return this.editMode;
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

  showWarningToast(summary: string, detail: string): void {
    this.showToastMsg('warn', summary, detail);
  }

  showInfoToast(summary: string, detail: string): void {
    this.showToastMsg('info', summary, detail);
  }

  showErrorToast(summary: string, detail: string): void {
    this.showToastMsg('error', summary, detail);
  }

  showSuccessToast(summary: string, detail: string): void {
    this.showToastMsg('success', summary, detail);
  }

  showToastMsg(severity: string, summary: string, detail: string): void {
    this.messageService.add({severity, summary, detail});
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
