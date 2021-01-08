import {AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ArticlesService} from '../articles-list/articles.service';
import {Article} from '../shared/Article';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {Observable, Subscription} from 'rxjs';
import {ArticleEditorService} from '../article-editor/article-editor.service';
import {ActionStatusResponse} from '../shared/data-transaction.service';
import {AppService} from '../app.service';

declare var hljs: any;

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.css'],
  animations: []
})
export class ArticleComponent implements OnInit, OnDestroy, AfterViewInit {
  article: Article;
  isInOwnerMode = false;
  isDraftArticle = false;
  isJustPublishedArticle = false;
  articlePublishedSubscription: Subscription;
  isComponentRendered = false;
  @ViewChild('articleContent', {static: true}) articleContent: ElementRef;

  constructor(private articlesService: ArticlesService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private appService: AppService,
              private location: Location,
              private articleEditorService: ArticleEditorService) {
  }

  ngOnInit(): void {
    this.isInOwnerMode = this.router.url.startsWith('/account/');
    this.isDraftArticle = this.router.url.startsWith('/account/drafts');
    this.activatedRoute.data.subscribe(
      (data) => {
        if (data.articleResponse.articleResource) {
          this.article = data.articleResponse.articleResource;
        } else if (data.articleResponse.accessToken) {
          this.appService.storeAuthToken(data.articleResponse.accessToken, data.articleResponse.refreshToken);
          this.reloadDataAfterAccessTokenRefresh();
        }
      }
    );

    this.articlePublishedSubscription = this.articlesService.publishedArticleEvent.subscribe(
      (value) => {
        this.isJustPublishedArticle = value;
        if (this.article) {
          this.applyHighlightjs();
        }
      }
    );
    this.articleEditorService.isAbleToAccessEditor = false;
  }

  private reloadDataAfterAccessTokenRefresh(): void {
    let articleResponseData;
    if (this.isDraftArticle) {
      articleResponseData = this.articlesService.searchArticleDraftById(this.activatedRoute.snapshot.params.id);
    } else if (this.isInOwnerMode) {
      articleResponseData = this.articlesService.searchArticleToEditById(this.activatedRoute.snapshot.params.id);
    }
    articleResponseData.subscribe(
      (articleResponse) => {
        this.article = articleResponse.articleResource;
        if (this.isComponentRendered) {
          this.articleContent.nativeElement.innerHTML = this.article.content;
          this.applyHighlightjs();
        }
      }
    );
  }

  ngOnDestroy(): void {
    this.articlesService.setArticlePublished(false);
  }

  ngAfterViewInit(): void {
    if (this.article) {
      this.articleContent.nativeElement.innerHTML = this.article.content;
    }
    if (this.isJustPublishedArticle) {
      setTimeout(() => {
        this.showSuccessPublishedMsg();
      }, 500);
    }
    this.isComponentRendered = true;
  }

  backButtonClicked(ignore: MouseEvent): void {
    this.location.back();
  }

  onEditArticle(ignore: MouseEvent): void {
    this.articleEditorService.confirm('Edit Confirmation',
      'Do you want to edit this article?',
      'far fa-edit', 'p-button-warning',
      this.editArticle.bind(this)
    );
  }

  /**
   * edit article or article draft
   */
  editArticle(): void {
    this.articleEditorService.isAbleToAccessEditor = true;
    if (this.isDraftArticle) {
      this.articleEditorService.setEditingDraft(true);
    } else {
      this.articleEditorService.setEditingDraft(false);
    }
    this.router.navigate([`/account/edit/${this.article.id}`]);
  }

  onDeleteArticle(ignore: MouseEvent): void {
    this.articleEditorService.confirm('Delete Confirmation',
      'Delete cannot be undone. Do you want to continue?',
      'fas fa-skull-crossbones', 'p-button-danger',
      this.deleteArticle.bind(this)
    );
  }

  /**
   * delete article or article draft
   */
  deleteArticle(): void {
    let deleteResponse: Observable<ActionStatusResponse>;
    if (this.isDraftArticle) {
      deleteResponse = this.articlesService.deleteArticleDraftById(this.article.id);
    } else {
      deleteResponse = this.articlesService.deleteArticleById(this.article.id);
    }
    this.appService.blockScreen();
    deleteResponse.subscribe(
      (response) => {
        this.appService.unblockScreen();
        if (response.status) {
          this.location.back();
        } else {
          this.showDeleteArticleFailMsg();
        }
      }, error => {
        this.articlesService.handle401Error(error, this.deleteArticle.bind(this));
      }
    );
  }

  private showSuccessPublishedMsg(): void {
    this.appService.showSuccessToast('Published', 'Successfully Published Article.');
  }

  showDeleteArticleFailMsg(): void {
    this.appService.showErrorToast('Delete Failed', 'Unable to delete. Please try again later.');
  }

  private applyHighlightjs(): void {
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

}
