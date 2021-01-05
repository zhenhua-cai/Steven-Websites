import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Article} from '../../../shared/Article';
import {PublishService} from '../../publish.service';
import {Subscription} from 'rxjs';
import {Router} from '@angular/router';
import {MessageService} from 'primeng/api';
import {ArticleEditorService} from '../../article-editor.service';

@Component({
  selector: 'app-article-publish-title',
  templateUrl: './ArticlePublishTitle.component.html',
  styleUrls: ['./ArticlePublishTitle.component.css']
})
export class ArticlePublishTitleComponent implements OnInit, OnDestroy {
  article: Article;
  articlePublishSubscription: Subscription;
  submitted: boolean;

  constructor(private publishService: PublishService,
              private router: Router,
              private messageService: MessageService,
              private articleEditorService: ArticleEditorService) {
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

  nextPage(): void {
    let hasError = false;
    this.submitted = true;

    this.formatArticleTitleAndSummary();
    if (!this.isValidTitle()) {
      this.messageService.add({severity: 'error', detail: 'Article Title\'s length should between 10 and 150 characters'});
      hasError = true;
    }
    if (!this.isValidSummary()) {
      this.messageService.add({severity: 'error', detail: 'Article summary should have at most 100 characters'});
      hasError = true;
    }
    if (hasError) {
      return;
    }
    this.publishService.updatePublishArticle(this.article);
    this.router.navigate(['/account/publish/confirmation']);
    return;

  }

  /**
   * validate article title. valid if length >= 10
   * @private
   */
  private isValidTitle(): boolean {
    return this.article != null && this.article.title != null
      && this.article.title.length >= 10 && this.article.title.length <= 150;
  }

  /**
   * validate article summary.
   * it's valid if summary is null or length <= 256;
   * @private
   */
  private isValidSummary(): boolean {
    return this.article != null && (this.article.summary == null || this.article.summary.length <= 256);
  }

  /**
   * trim left and right spaces of title and summary.
   * @private
   */
  private formatArticleTitleAndSummary(): void {
    if (this.article == null) {
      return;
    }
    if (this.article.title != null) {
      this.article.title = this.article.title.trim();
    }
    if (this.article.summary != null) {
      this.article.summary = this.article.summary.trim();
    }
  }

  prevPage(): void {
    this.cancel();
  }

  cancel(): void {
    this.articleEditorService.setEditingDraft(true);
    this.articleEditorService.isAbleToAccessEditor = true;
    this.router.navigate([`/account/edit/${this.article.id}`]);
  }
}
