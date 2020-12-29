import {Component, OnDestroy, OnInit} from '@angular/core';
import {Article} from '../../../shared/Article';
import {PublishService} from '../../publish.service';
import {Router} from '@angular/router';
import {MessageService} from 'primeng/api';
import {Subscription} from 'rxjs';
import {ArticleEditorService} from '../../article-editor.service';
import {AppService} from '../../../app.service';
import {ArticlesService} from '../../../articles-list/articles.service';

declare var hljs: any;

@Component({
  selector: 'app-article-publish-confirmation',
  templateUrl: './article-publish-confirmation.component.html',
  styleUrls: ['./article-publish-confirmation.component.css']
})
export class ArticlePublishConfirmationComponent implements OnInit, OnDestroy {
  article: Article;
  articlePublishSubscription: Subscription;
  isInPreview = false;

  constructor(private publishService: PublishService,
              private router: Router,
              private messageService: MessageService,
              private articleEditorService: ArticleEditorService,
              private articlesService: ArticlesService,
              private appService: AppService) {
  }

  ngOnInit(): void {
    this.articlePublishSubscription = this.publishService.articlePublishEvent.subscribe(
      (article) => {
        this.article = article;
      }
    );
  }

  ngOnDestroy(): void {
    if (this.articlePublishSubscription) {
      this.articlePublishSubscription.unsubscribe();
    }
  }

  prevPage(): void {
    this.router.navigate(['/account/publish/title']);
  }

  cancel(): void {
    this.articleEditorService.setEditingDraft(true);
    this.articleEditorService.isAbleToAccessEditor = true;
    this.router.navigate([`/account/edit/${this.article.id}`]);
  }

  previewArticle(): void {
    this.isInPreview = true;
    this.appService.blockScreen();
    const checkArticleContentElementExistsInterval = setInterval(
      () => {
        const container = document.getElementById('articleContent');
        if (container) {
          clearInterval(checkArticleContentElementExistsInterval);
          container.innerHTML = this.article.content;
          this.appService.unblockScreen();
          const waitForArticleContentRenderedInteval = setInterval(
            () => {
              const articleContentElement = document.getElementById('articleContent');
              if (articleContentElement) {
                clearInterval(waitForArticleContentRenderedInteval);
                document.querySelectorAll('pre code').forEach(block => {
                  hljs.highlightBlock(block);
                });
              }
            }, 10
          );
        }
      }, 10);

  }

  cancelPreview(): void {
    this.isInPreview = false;
  }

  /**
   * publish article. show error if fail.
   * if success, jumpt to /account/articles/{id}
   */
  publishArticle(): void {
    this.appService.blockScreen();
    this.articlesService.publishArticle(this.article).subscribe(
      (response) => {
        this.appService.unblockScreen();
        this.publishService.publishComplete();
        this.router.navigate([`/account/articles/${this.article.id}`]);
      },
      error => {
        this.appService.unblockScreen();
        this.showPublishFailedMsg();
      }
    );
  }

  private showPublishFailedMsg(): void {
    this.appService.showErrorToast('Publish Failed', 'Unable to publish article. Please try later.');
  }

}
