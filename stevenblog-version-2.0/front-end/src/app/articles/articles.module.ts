import {NgModule} from '@angular/core';
import {ArticlesComponent} from './articles.component';
import {SharedModule} from '../shared/shared.module';
import {NavbarModule} from '../navbar/navbar.module';
import {ArticlesListComponent} from './articles-list/articles-list.component';
import {ArticlesListItemComponent} from './articles-list/articles-list-item/articles-list-item.component';
import {FooterModule} from '../footer/footer.module';
import {FormsModule} from '@angular/forms';
import {InputTextModule} from 'primeng/inputtext';
import {PaginatorModule} from 'primeng/paginator';
import { ArticleComponent } from './article/article.component';

@NgModule({
  declarations: [
    ArticlesComponent,
    ArticlesListComponent,
    ArticlesListItemComponent,
    ArticleComponent
  ],
  imports: [
    SharedModule,
    NavbarModule,
    FooterModule,
    FormsModule,
    InputTextModule,
    PaginatorModule
  ]
})
export class ArticlesModule {
}
