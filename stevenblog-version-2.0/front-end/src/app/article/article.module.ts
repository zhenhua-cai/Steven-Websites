import { NgModule } from '@angular/core';
import {ArticleComponent} from './article.component';
import {SharedModule} from '../shared/shared.module';
import {ButtonModule} from 'primeng/button';
import {ToastModule} from 'primeng/toast';
import {ConfirmDialogModule} from 'primeng/confirmdialog';

@NgModule({
  declarations: [
    ArticleComponent
  ],
  imports: [
    SharedModule,
    ButtonModule,
    ToastModule,
    ConfirmDialogModule,
  ]
})
export class ArticleModule { }
