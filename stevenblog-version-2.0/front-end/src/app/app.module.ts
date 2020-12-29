import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {ContactModule} from './contact/contact.module';
import {BlogModule} from './blog/blog.module';
import {LoginModule} from './login/login.module';
import {NavbarModule} from './navbar/navbar.module';
import {FooterModule} from './footer/footer.module';
import {AboutMeModule} from './about-me/about-me.module';
import {SharedModule} from './shared/shared.module';
import {LandingPageModule} from './landing-page/landing-page.module';
import {ArticlesListModule} from './articles-list/articles-list.module';
import {ArticleModule} from './article/article.module';
import {AddAuthCookieInterceptor} from './shared/add-auth-cookie.interceptor';
import {ConfirmationService, MessageService} from 'primeng/api';
import {AccountModule} from './account/account.module';
import {ArticleEditorModule} from './article-editor/article-editor.module';
import {ToastModule} from 'primeng/toast';
import {BlockUIModule} from 'primeng/blockui';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    BlogModule,
    ContactModule,
    LoginModule,
    NavbarModule,
    FooterModule,
    AboutMeModule,
    SharedModule,
    LandingPageModule,
    ArticlesListModule,
    ArticleModule,
    AccountModule,
    ArticleEditorModule,
    ToastModule,
    BlockUIModule
  ],
  providers: [
    [
      {provide: HTTP_INTERCEPTORS, useClass: AddAuthCookieInterceptor, multi: true},
      MessageService,
      ConfirmationService,
    ]
  ],
  exports: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
