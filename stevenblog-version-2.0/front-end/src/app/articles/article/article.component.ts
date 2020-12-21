import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {NavbarService} from '../../navbar/navbar.service';
import {Subscription} from 'rxjs';
import {ArticlesService} from '../articles.service';
import {Article} from '../../shared/Article';
import {ActivatedRoute, Params} from '@angular/router';

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.css'],
  animations: []
})
export class ArticleComponent implements OnInit, OnDestroy {
  articleId: string;
  article: Article;
  articleChangedSubscription: Subscription;
  @ViewChild('articleContent', {static: true}) articleContent: ElementRef;

  constructor(private navbarService: NavbarService,
              private articlesService: ArticlesService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.articleChangedSubscription = this.articlesService.articleUpdateEvent.subscribe(
      (article) => {
        this.article = article;
        document.getElementById('articleContent').innerHTML = this.article.content;
      }
    );
    this.activatedRoute.params.subscribe(
      (params: Params) => {
        this.articleId = params.id;
        if (!this.articleId) {
          return;
        }
        this.articlesService.searchArticleById(this.articleId);
      }
    );
  }

  ngOnDestroy(): void {
    this.articleChangedSubscription.unsubscribe();

  }

}
