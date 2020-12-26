import {AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ArticlesService} from '../articles-list/articles.service';
import {Article} from '../shared/Article';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.css'],
  animations: []
})
export class ArticleComponent implements OnInit, OnDestroy, AfterViewInit {
  article: Article;
  isInOwerMode = false;

  @ViewChild('articleContent', {static: true}) articleContent: ElementRef;

  constructor(private articlesService: ArticlesService,
              private activatedRoute: ActivatedRoute,
              private router: Router,
              private location: Location) {
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(
      (data) => {
        this.article = data.article;
      }
    );
    this.isInOwerMode = this.router.url.startsWith('/account/');
  }

  ngOnDestroy(): void {
  }

  ngAfterViewInit(): void {
    this.articleContent.nativeElement.innerHTML = this.article.content;
  }

  backButtonClicked($event: MouseEvent): void {
    this.location.back();
  }

  editArticle($event: MouseEvent): void {

  }

  deleteArticle($event: MouseEvent): void {

  }
}
