import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {InputTextModule} from 'primeng/inputtext';
import {PaginatorModule} from 'primeng/paginator';
import {ArticlesListComponent} from './articles-list.component';
import {ArticlesListItemComponent} from './articles-list-item/articles-list-item.component';
import {SharedModule} from '../shared/shared.module';


@NgModule({
  declarations: [
    ArticlesListComponent,
    ArticlesListItemComponent,
  ],
  imports: [
    SharedModule,
    FormsModule,
    InputTextModule,
    PaginatorModule
  ]
})
export class ArticlesListModule {
}
