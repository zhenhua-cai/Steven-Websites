import {Component, OnDestroy, OnInit} from '@angular/core';
import {Article} from '../shared/Article';
import {ArticlesService} from './articles.service';

import {ActivatedRoute, Router} from '@angular/router';
import {ArticlesPageResponse} from '../shared/data-transaction.service';

@Component({
  selector: 'app-articles-list',
  templateUrl: './articles-list.component.html',
  styleUrls: ['./articles-list.component.css'],
  animations: []
})
export class ArticlesListComponent implements OnInit, OnDestroy {
  articles: Article[] = [];
  currentPage = 0;
  pageSize = 10;
  totalArticles: number;

  constructor(private articleService: ArticlesService,
              private router: Router,
              private activateRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activateRoute.data.subscribe(
      (data) => {
        this.resolveArticlesResponse(data.articlesResponse);
      }, ignore => {
      }
    );
  }

  ngOnDestroy(): void {
  }

  /**
   * search article by pagination
   * @param event pagination event
   */
  paginate(event: any): void {
    this.currentPage = event.page;
    this.articleService.fetchArticles(this.currentPage, this.pageSize).subscribe(
      (articlesResponse) => {
        this.resolveArticlesResponse(articlesResponse);
      }, ignore => {

      });
    window.scrollTo(0, 0);
  }

  private resolveArticlesResponse(articlesResponse: ArticlesPageResponse): void {
    this.articles = articlesResponse.articles;
    this.totalArticles = articlesResponse.responsePage.totalElements;
    this.pageSize = articlesResponse.responsePage.size;
    this.currentPage = articlesResponse.responsePage.number;
  }
}
