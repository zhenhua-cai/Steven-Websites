import {AfterViewInit, Component, ElementRef, HostListener, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ArticlesService} from '../articles-list/articles.service';
import {Article} from '../shared/Article';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {Observable, Subject, Subscription} from 'rxjs';
import {ArticleEditorService} from '../article-editor/article-editor.service';
import {ActionStatusResponse} from '../shared/data-transaction.service';

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
  @ViewChild('articleContent', {static: true}) articleContent: ElementRef;

  constructor(private articlesService: ArticlesService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private location: Location,
              private articleEditorService: ArticleEditorService) {
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(
      (data) => {
        this.article = data.articleResponse.articleResource;
      }
    );
    this.isInOwnerMode = this.router.url.startsWith('/account/');
    this.isDraftArticle = this.router.url.startsWith('/account/drafts');
    this.articlePublishedSubscription = this.articlesService.publishedArticleEvent.subscribe(
      (value) => {
        this.isJustPublishedArticle = value;
        this.applyHighlightjs();
      }
    );
    this.articleEditorService.isAbleToAccessEditor = false;

  }

  ngOnDestroy(): void {
    this.articlesService.setArticlePublished(false);
  }

  ngAfterViewInit(): void {
    this.articleContent.nativeElement.innerHTML = this.article.content;
    if (this.isJustPublishedArticle) {
      setTimeout(() => {
        this.showSuccessPublishedMsg();
      }, 500);

    }
  }

  backButtonClicked($event: MouseEvent): void {
    this.location.back();
  }

  onEditArticle($event: MouseEvent): void {
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

  onDeleteArticle($event: MouseEvent): void {
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
    let deleteResponse: Observable<ActionStatusResponse> = null;
    if (this.isDraftArticle) {
      deleteResponse = this.articlesService.deleteArticleDraftById(this.article.id);
    } else {
      deleteResponse = this.articlesService.deleteArticleById(this.article.id);
    }
    deleteResponse.subscribe(
      (response) => {
        if (response.status) {
          this.location.back();
        } else {
          this.showDeleteArticleFailMsg();
        }
      }, error => {
        this.showDeleteArticleFailMsg();
      }
    );
  }

  private showSuccessPublishedMsg(): void {
    this.articleEditorService.showSuccessToast('Published', 'Successfully Published Article.');
  }

  showDeleteArticleFailMsg(): void {
    this.articleEditorService.showErrorToast('Delete Failed', 'Unable to delete. Please try again later.');
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
