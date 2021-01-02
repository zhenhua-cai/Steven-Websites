import {Component, HostListener, OnDestroy, OnInit} from '@angular/core';
import {ConfirmationService, MenuItem, MessageService} from 'primeng/api';
import {Article} from '../../shared/Article';
import {PublishService} from '../publish.service';
import {Subscription} from 'rxjs';
import {ArticleEditorService} from '../article-editor.service';
import {Router} from '@angular/router';
import {ArticlesService} from '../../articles-list/articles.service';
import {AppService} from '../../app.service';

@Component({
  selector: 'app-publish',
  templateUrl: './publish.component.html',
  styleUrls: ['./publish.component.css']
})
export class PublishComponent implements OnInit, OnDestroy {
  publishSteps: MenuItem[];
  article: Article;
  articlePublishCompleteSubscription: Subscription;
  articlePublishSubscription: Subscription;
  showPopupBeforeQuitSubscription: Subscription;

  constructor(private publishService: PublishService,
              private messageService: MessageService,
              private articleEditorService: ArticleEditorService,
              private router: Router,
              private appService: AppService,
              private articlesService: ArticlesService) {
  }

  ngOnInit(): void {
    this.publishSteps = [
      {label: 'Article Title', routerLink: 'account/publish/title'},
      {label: 'Confirmation', routerLink: 'account/publish/confirmation'}
    ];
    this.articlePublishCompleteSubscription = this.publishService.articlePublishCompleteEvent.subscribe(
      (complete) => {
        if (complete) {
          this.messageService.add({severity: 'success', summary: 'Article Published'});
        }
      }
    );
    this.articlePublishSubscription = this.publishService.articlePublishEvent.subscribe(
      (article) => {
        this.article = article;
      }
    );
    this.showPopupBeforeQuitSubscription = this.publishService.showPopUpBeforeQuitEvent.subscribe(
      (show) => {
        if (show) {
          this.articleEditorService.saveArticleBeforeLeavePopup();
        }
      }
    );
  }

  /**
   * prevent from closing browser/tab, refresh page
   * @param $event window event
   */
  @HostListener('window:beforeunload', ['$event']) preventDefaultClosingPage($event): void {
    $event.preventDefault();
    $event.returnValue = 'Your data will be lost!';
  }

  ngOnDestroy(): void {
    if (this.articlePublishCompleteSubscription) {
      this.articlePublishCompleteSubscription.unsubscribe();
    }
    if (this.articlePublishSubscription) {
      this.articlePublishSubscription.unsubscribe();
    }
  }

  discardArticleBeforeLeave(): void {
    this.goToEditor();
  }

  SaveAndLeave(): void {
    this.appService.blockScreen();
    this.articlesService.saveArticleDraft(this.article).subscribe(
      (response) => {
        this.appService.unblockScreen();
        this.goToEditor();
      },
      error => {
        this.articlesService.handle401Error(error, this.SaveAndLeave.bind(this));
      }
    );
  }

  goToEditor(): void {
    this.articleEditorService.setEditingDraft(true);
    this.articleEditorService.isAbleToAccessEditor = true;
    this.publishService.isInPublishProgress = false;
    this.router.navigate([`/account/edit/${this.article.id}`]);
  }
}
