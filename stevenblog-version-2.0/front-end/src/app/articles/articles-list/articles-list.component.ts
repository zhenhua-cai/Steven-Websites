import {Component, OnDestroy, OnInit} from '@angular/core';
import {Article} from '../../shared/Article';
import {ArticlesService} from '../articles.service';

import {Subscription} from 'rxjs';
import {ActivatedRoute, Params, Router} from '@angular/router';

@Component({
  selector: 'app-articles-list',
  templateUrl: './articles-list.component.html',
  styleUrls: ['./articles-list.component.css'],
  animations: []
})
export class ArticlesListComponent implements OnInit, OnDestroy {
  articles: Article[] = [];
  stayInPage = true;
  searchTitle: string = null;
  pageChangeSubscription: Subscription;
  articlesUpdateSubscription: Subscription;
  currentPage = 0;
  pageSize = 10;
  totalArticles: number;

  constructor(private articleService: ArticlesService,
              private router: Router,
              private activateRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.articlesUpdateSubscription = this.articleService.articlesChangeEvent.subscribe(
      (articles) => {
        this.articles = articles;
      }
    );
    this.pageChangeSubscription = this.articleService.articleSearchPageChangeEvent.subscribe(
      (page) => {
        this.currentPage = page.number;
        this.pageSize = page.size;
        this.totalArticles = page.totalElements;
      }
    );
    this.activateRoute.params.subscribe(
      (params: Params) => {
        this.searchTitle = params.title;
        if (!this.searchTitle) {
          this.articleService.fetchArticles(this.currentPage, this.pageSize);
        } else {
          this.articleService.searchArticleByTitle(this.searchTitle, 0, this.pageSize);
        }
      }
    );
  }

  ngOnDestroy(): void {
    this.pageChangeSubscription.unsubscribe();
  }




  /**
   * search article by pagination
   * @param event pagination event
   */
  paginate(event: any): void {
    this.currentPage = event.page;
    this.articleService.fetchArticles(this.currentPage, this.pageSize);
    window.scrollTo(0, 0);
  }
}
