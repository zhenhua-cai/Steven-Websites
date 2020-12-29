import {Component, ElementRef, HostListener, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MenuItem} from 'primeng/api';
import * as CKEditor from 'src/assets/ckeditor/ckeditor';
import {CKEditor5} from '@ckeditor/ckeditor5-angular';
import {Location} from '@angular/common';
import {AuthService} from '../shared/auth.service';
import {ArticlesService} from '../articles-list/articles.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Observable, Subscription} from 'rxjs';
import {ArticleEditorService} from './article-editor.service';
import {Article} from '../shared/Article';
import {ArticleResponse} from '../shared/data-transaction.service';
import {PublishService} from './publish.service';
import {AppService} from '../app.service';

declare var $: any;

@Component({
  selector: 'app-article-editor',
  templateUrl: './article-editor.component.html',
  styleUrls: ['./article-editor.component.css']
})
export class ArticleEditorComponent implements OnInit {
  public Editor = CKEditor;
  editorConfig = {};
  @ViewChild('editorContainer', {static: true}) editorContainer: ElementRef;
  @ViewChild('articlePreview', {static: true}) articlePreviewElement: ElementRef;
  isProcessing = true;
  sidebarMenuItems: MenuItem[];
  articleId: string;
  articleTitle: string;
  article: Article;
  content = '';
  displaySideBar = false;
  lockArticle = false;
  showSidebarButton = true;
  oldContent: string;
  isDataLoaded = false;
  totalWords = 0;
  totalChars = 0;
  articleContentElement: any;
  contentSaved = false;
  displayLockScreenButton = true;
  showSaveArticlePopupSubscription: Subscription;
  saveBeforeLeave = true;

  constructor(private location: Location,
              private authService: AuthService,
              private articlesService: ArticlesService,
              private router: Router,
              private articleEditorService: ArticleEditorService,
              private activatedRoute: ActivatedRoute,
              private publishService: PublishService,
              private appService: AppService) {
  }

  ngOnInit(): void {
    this.sidebarMenuItems = [
      {
        label: 'Menu',
        items: [
          {label: 'New', icon: 'pi pi-fw pi-plus', command: this.onNew.bind(this)},
          {label: 'Save', icon: 'far pi-fw fa-save', command: this.onSave.bind(this)},
          {label: 'Publish', icon: 'far pi-fw fa-share-square', command: this.onPublish.bind(this)},
          {label: 'Delete', icon: 'pi pi-fw pi-trash', command: this.onDelete.bind(this)},
          {label: 'Revert', icon: 'pi pi-fw pi-undo', command: this.onRevert.bind(this)},
          {label: 'Cancel', icon: 'pi pi-fw pi-times', command: this.onCancel.bind(this)},
          {label: 'Logout', icon: 'pi pi-fw pi-power-off', command: this.onLogout.bind(this)}
        ]
      },
    ];
    this.editorConfig = {
      mediaEmbed: {
        previewsInData: true
      },
      wordCount: {
        displayWords: true,
        displayCharacters: true,
        onUpdate: (stats) => {
          this.totalWords = stats.words;
          this.totalChars = stats.characters;
        }
      }
    };

    this.showSaveArticlePopupSubscription = this.articleEditorService.showSaveArticlePopupEvent.subscribe(
      (showPopup) => {
        if (showPopup) {
          this.showSaveArticlePopupBeforeLeave();
        }
      }
    );

    this.articleEditorService.isAbleToAccessEditor = false;
    // save oldContent, will be used in revert.
    this.oldContent = this.content;

    // if created new article, no need to load data.
    if (this.articleEditorService.isNewArticle()) {
      this.isDataLoaded = true;
      return;
    }
    // if it's editing draft, load draft. otherwise, load published.
    this.articleId = this.activatedRoute.snapshot.params.id;
    let articleResponse: Observable<ArticleResponse> = null;
    if (this.articleEditorService.isEditingDraft()) {
      articleResponse = this.articlesService.searchArticleDraftById(this.articleId);
    } else {
      articleResponse = this.articlesService.searchArticleToEditById(this.articleId);
    }
    articleResponse.subscribe((articleResponseData) => {
        if (articleResponseData.articleResource.content !== null && articleResponseData.articleResource.content.length > 0) {
          console.log('Editor Content Loaded');
        } else {
          console.log(articleResponseData);
        }
        this.article = articleResponseData.articleResource;
        this.content = this.article.content;
        this.oldContent = this.content;
        this.isDataLoaded = true;
        if (this.isProcessing) {
          this.isProcessing = false;
        }
      },
      error => {
        this.showLoadArticleFailMsg();
        this.isProcessing = false;
        this.lockArticle = true;
      });
  }

  /**
   * prevent from closing browser/tab, refresh page
   * @param $event
   */
  @HostListener('window:beforeunload', ['$event']) preventDefaultClosingPage($event): void {
    $event.preventDefault();
    $event.returnValue = 'Your data will be lost!';
  }

  /**
   * prevent from clicking back button
   * @param $event
   */
  @HostListener('window:popstate', ['$event']) preventDefaultBackward($event): void {
    $event.preventDefault();
    $event.returnValue = 'Your data will be lost!';
    if (this.contentChangedSinceSave()) {
      this.articleEditorService.editorHasUnsavedData = true;
    }
  }

  onCancel(): void {
    this.displaySideBar = false;
    if (!this.contentChangedSinceSave()) {
      this.location.back();
      return;
    }
    this.articleEditorService.confirm('Cancel Confirmation',
      'Do you want to cancel editing?',
      'fas fa-skull-crossbones', 'p-button-danger',
      () => {
        this.location.back();
      });
  }

  onLogout(): void {
    this.displaySideBar = false;
    if (!this.contentChangedSinceSave()) {
      this.authService.logout();
      return;
    }
    this.articleEditorService.confirm('Logout Confirmation',
      'Do you want to logout?',
      'fas fa-skull-crossbones', 'p-button-danger',
      () => {
        this.authService.logout();
      });
  }

  onRevert(): void {
    this.displaySideBar = false;
    if (!this.contentChangedSinceSave()) {
      return;
    }
    this.articleEditorService.confirm('Revert Confirmation',
      'Revert cannot be undone. Do you want to continue?',
      'fas fa-skull-crossbones', 'p-button-danger',
      () => {
        this.content = this.oldContent;
        this.showRevertSuccessMsg();
      });
  }

  onDelete(): void {
    this.displaySideBar = false;
    if (this.editorIsEmpty()) {
      this.location.back();
      return;
    }
    this.articleEditorService.confirm('Delete Confirmation',
      'Delete cannot be undone. Do you want to continue?',
      'fas fa-skull-crossbones', 'p-button-danger',
      () => {
        // usually we only need to check if this.article is null,
        // but there are special cases that when we failed to save article,
        // in that case. this.article is not null, we need to check this.article.id
        if (this.article == null || this.article.id == null) {
          // if this.article == null, it means nothing saved yet. just go back to prev page.
          this.location.back();
          return;
        }
        // remove this article from db. we don't delete published article. only the draft.
        this.articlesService.deleteArticleDraftById(this.article.id).subscribe(
          (response) => {
            if (response.status) {
              this.location.back();
            } else {
              this.showDeleteArticleFailMsg();
              return;
            }
          }, error => {
            this.showDeleteArticleFailMsg();
          }
        );
      });
  }

  onNew(): void {
    this.displaySideBar = false;
    if (!this.contentChangedSinceSave()) {
      this.reset();
      return;
    }
    this.articleEditorService.confirm('Confirmation',
      'There are unsaved content. Do you want to continue?',
      'fas fa-skull-crossbones', 'p-button-danger',
      () => {
        this.reset();
      });
  }

  onSave(): void {
    this.displaySideBar = false;
    this.saveArticle();
  }

  onPublish(): void {
    this.displaySideBar = false;

    // if it's editing published article. and nothing changed. don't publish again.
    if (!this.articleEditorService.isNewArticle() && !this.articleEditorService.isEditingDraft()) {
      if (!this.contentSaved && !this.contentChangedSinceSave()) {
        this.showNothingNeedToPublishMsg();
        return;
      }
    }
    // do not publish if content is too short.
    if (!this.validateContentLength()) {
      this.showArticleContentTooShortMsg();
      return;
    }
    this.articleEditorService.confirm('Publish Article',
      'Do you want to publish this article?',
      'fa fa-newspaper',
      'p-button-warning',
      this.publishButtonHandler.bind(this));
  }

  /**
   * navigate without saving.
   */
  discardArticleBeforeLeave(): void {
    this.articleEditorService.continueNavigate();
  }

  showSaveArticlePopupBeforeLeave(): void {
    this.displaySideBar = false;
    this.articleEditorService.saveArticleBeforeLeavePopup();
  }

  SaveAndLeave(): void {
    this.saveBeforeLeave = true;
    this.saveArticle();
  }

  /**
   * save article.
   */
  saveArticle(): void {
    if (!this.validateArticleContentBeforeSave()) {
      return;
    }
    this.updateArticle();
    this.saveArticleAfterValidation();
  }

  /**
   * function actually save article. and get response. if unable to save, display error msg.
   */
  private saveArticleAfterValidation(): void {
    this.appService.blockScreen();
    this.saveArticleAndExecuteAfterSuccess(this.processSaveArticleResponseAndLeaveIfNeeds.bind(this));
  }

  /**
   * process save response data and navigate to other page if saveBeforeLeave if true;
   * @param response
   * @private
   */
  private processSaveArticleResponseAndLeaveIfNeeds(response: ArticleResponse): void {
    this.processSaveArticleResponse(response);
    this.showSuccessSavedMsg();
    if (this.saveBeforeLeave) {
      this.articleEditorService.continueNavigate();
    }
  }

  /**
   * process save response data.
   * @param response
   * @private
   */
  private processSaveArticleResponse(response: ArticleResponse): void {
    this.article = response.articleResource;
    this.oldContent = this.content;
    this.contentSaved = true;
    this.articleEditorService.editorHasUnsavedData = false;
    this.appService.unblockScreen();
  }

  /**
   * function actually save article. and handle behavior after successfully saved.
   * @param callBackOnSuccess
   * @private
   */
  private saveArticleAndExecuteAfterSuccess(callBackOnSuccess: (response: ArticleResponse) => void): void {
    this.articlesService.saveArticleDraft(this.article).subscribe(
      callBackOnSuccess,
        error => {
        this.appService.unblockScreen();
        this.showSaveFailedMsg();
        this.isProcessing = false;
      }
    );
  }

  /**
   * function handles event when ok pressed in publish popup.
   * @private
   */
  private publishButtonHandler(): void {
    this.updateArticle();
    this.saveArticleAndExecuteAfterSuccess(this.prePublishAfterSaveResponse.bind(this));

  }

  /**
   * handle save article response and send updated article to next page.
   * @param response
   * @private
   */
  private prePublishAfterSaveResponse(response: ArticleResponse): void {
    this.processSaveArticleResponse(response);
    this.publishService.startsPublishArticle(this.article);
    this.router.navigate(['/account/publish/title']);
  }

  /**
   * set the height of editor area. make it fill the container. also set isLoading to false.
   * @param editor ckeditor.
   */
  onEditorReady(editor: CKEditor5.Editor): void {
    const editorHeader = $('div.ck.ck-editor__top');
    const editorBody = $('div.ck.ck-editor__main');
    // editor body's height = container - header;
    // since header has a fixed height. we can easily set the height of body.
    editorBody.height(this.editorContainer.nativeElement.offsetHeight - editorHeader.height());
    if (this.isDataLoaded) {
      this.isProcessing = false;
    }
    this.articleContentElement = $('div.ck.ck-content.ck-editor__editable');
  }

  lockScreenIcon(): string {
    if (this.lockArticle) {
      return 'pi pi-lock';
    } else {
      return 'pi pi-lock-open';
    }
  }

  lockOrUnlockScreen($event: MouseEvent): void {
    this.lockArticle = !this.lockArticle;
    this.displaySideBar = false;
    this.showSidebarButton = !this.showSidebarButton;
  }


  /**
   * reset editor to blank page.
   * @private
   */
  private reset(): void {
    this.article = null;
    this.content = '';
    this.oldContent = this.content;
    this.articleTitle = null;
    this.articleId = null;
    this.articleEditorService.setNewArticle(true);
    this.totalWords = 0;
    this.totalChars = 0;
  }

  /**
   * check if content changed, and if article has enough word to save.
   * @private
   */
  private validateArticleContentBeforeSave(): boolean {
    // if nothing changes. display msg.
    if (this.content === this.oldContent) {
      this.showNothingNeedToSaveMsg();
      return false;
    }
    // if article is too short, display msg.
    if (!this.validateContentLength()) {
      this.showArticleContentTooShortMsg();
      return false;
    }
    return true;
  }

  /**
   * check if there are content in editor. if there are, return false, otherwise return true.
   * @private
   */
  private editorIsEmpty(): boolean {
    return this.totalWords === 0 && this.articleContentElement.text().length === 0;
  }

  /**
   * check if content changed since last save
   * @private
   */
  private contentChangedSinceSave(): boolean {
    return this.content !== this.oldContent;
  }

  /**
   * update article object content.
   * if article == null, create new article object, return true;
   * otherwise, update content, return false;
   * @private
   */
  private updateArticle(): boolean {
    if (this.article == null) {
      this.article = new Article(null, null, this.content, this.authService.getUsername(), null, null, null);
      return true;
    }
    this.article.content = this.content;
    this.articleTitle = this.article.title;
    return false;
  }

  /**
   * check the word counts of article. if it's less than 25 words. consider to be too short.
   * @private
   */
  private validateContentLength(): boolean {
    return this.totalWords >= 25 || this.articleContentElement.text().length > 25;
  }

  private showArticleContentTooShortMsg(): void {
    this.appService.showWarningToast('Failed', 'Article has less than 25 words.');
  }

  private showNothingNeedToSaveMsg(): void {
    this.appService.showInfoToast('Nothing To Save', 'Article is not changed.');
  }

  private showNothingNeedToPublishMsg(): void {
    this.appService.showWarningToast('Not Publish', 'Article has not changed.');
  }

  private showSuccessSavedMsg(): void {
    this.appService.showSuccessToast('Saved', 'Successfully saved article');
  }

  private showLoadArticleFailMsg(): void {
    this.appService.showErrorToast('Load Article Failed', 'Unable to load article. Please try later');
  }

  private showDeleteArticleFailMsg(): void {
    this.appService.showErrorToast('Delete Article Failed', 'Unable to delete article. Please try later');
  }

  private showRevertSuccessMsg(): void {
    this.appService.showSuccessToast('Reverted', 'Revert back to last saved content');
  }

  private showSaveFailedMsg(): void {
    this.appService.showErrorToast('Save Failed', 'Unable to save article. Please try later.');
  }


}
