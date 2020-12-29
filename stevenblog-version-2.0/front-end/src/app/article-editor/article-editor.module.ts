import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ArticleEditorComponent} from './article-editor.component';
import {MenubarModule} from 'primeng/menubar';
import {FormsModule} from '@angular/forms';
import {CKEditorModule} from '@ckeditor/ckeditor5-angular';
import {BlockUIModule} from 'primeng/blockui';
import {ProgressSpinnerModule} from 'primeng/progressspinner';
import {SidebarModule} from 'primeng/sidebar';
import {ButtonModule} from 'primeng/button';
import {MenuModule} from 'primeng/menu';
import {PanelModule} from 'primeng/panel';
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import {ToastModule} from 'primeng/toast';
import {DialogModule} from 'primeng/dialog';
import {InputTextModule} from 'primeng/inputtext';
import {NavbarModule} from '../navbar/navbar.module';
import {StepsModule} from 'primeng/steps';
import {PublishComponent} from './publish/publish.component';
import {ArticlePublishTitleComponent} from './publish/ArticlePublishTitle/ArticlePublishTitle.component';
import {ArticlePublishConfirmationComponent} from './publish/article-publish-confirmation/article-publish-confirmation.component';
import {InputTextareaModule} from 'primeng/inputtextarea';
import {CardModule} from 'primeng/card';
@NgModule({
  declarations: [
    ArticleEditorComponent,
    PublishComponent,
    ArticlePublishTitleComponent,
    ArticlePublishConfirmationComponent,
  ],
  imports: [
    CommonModule,
    MenubarModule,
    FormsModule,
    CKEditorModule,
    BlockUIModule,
    ProgressSpinnerModule,
    SidebarModule,
    ButtonModule,
    MenuModule,
    PanelModule,
    ConfirmDialogModule,
    ToastModule,
    DialogModule,
    InputTextModule,
    NavbarModule,
    StepsModule,
    InputTextareaModule,
    CardModule
  ]
})
export class ArticleEditorModule {
}
