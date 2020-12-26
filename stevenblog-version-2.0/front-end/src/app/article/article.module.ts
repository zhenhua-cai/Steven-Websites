import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ArticleComponent} from './article.component';
import {SharedModule} from '../shared/shared.module';
import {ButtonModule} from 'primeng/button';



@NgModule({
  declarations: [
    ArticleComponent
  ],
  imports: [
    SharedModule,
    ButtonModule
  ]
})
export class ArticleModule { }
