import {Component, Input, OnInit} from '@angular/core';
import {Article} from '../../shared/Article';
import {Router} from '@angular/router';

@Component({
  selector: 'app-articles-list-item',
  templateUrl: './articles-list-item.component.html',
  styleUrls: ['./articles-list-item.component.css']
})
export class ArticlesListItemComponent implements OnInit {
  @Input()
  article: Article;
  gainFocus = false;

  constructor(private router: Router) {
  }

  ngOnInit(): void {
  }

  navigate(): void {
    this.router.navigate([`/articles/${this.article.id}`]);
  }
}
