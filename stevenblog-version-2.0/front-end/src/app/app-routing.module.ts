import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {LandingPageComponent} from './landing-page/landing-page.component';
import {BlogComponent} from './blog/blog.component';
import {AboutMeComponent} from './about-me/about-me.component';
import {ContactComponent} from './contact/contact.component';
import {ArticleComponent} from './article/article.component';
import {ArticlesListComponent} from './articles-list/articles-list.component';
import {LoginComponent} from './login/login.component';
import {ArticlesResolver} from './articles-list/articles.resolver';
import {SearchArticlesResolver} from './articles-list/search-articles.resolver';
import {ArticleResolver} from './article/article.resolver';
import {AccountComponent} from './account/account.component';
import {ProfileComponent} from './account/profile/profile.component';
import {MyArticlesComponent} from './account/my-articles/my-articles.component';
import {MyMessagesComponent} from './account/my-messages/my-messages.component';
import {AuthGuard} from './shared/auth-guard.service';
import {DraftResolver} from './account/draft-resolver.service';
import {MyArticleResolver} from './account/my-article.resolver';


const routes: Routes = [
    {path: '', component: LandingPageComponent},
    {path: 'home', redirectTo: ''},
    {
      path: 'articles', component: BlogComponent, children: [
        {path: '', component: ArticlesListComponent, resolve: {articlesResponse: ArticlesResolver}},
        {path: 'search/:title', component: ArticlesListComponent, resolve: {articlesResponse: SearchArticlesResolver}},
        {path: ':id', component: ArticleComponent, resolve: {article: ArticleResolver}}
      ]
    },
    {
      path: 'about', component: BlogComponent, children: [
        {path: '', component: AboutMeComponent}
      ]
    },
    {
      path: 'contact', component: BlogComponent, children: [
        {path: '', component: ContactComponent}
      ]
    },
    {
      path: 'login', component: BlogComponent, children: [
        {path: '', component: LoginComponent}
      ]
    },
    {
      path: 'logout', component: BlogComponent, children: [
        {path: '', component: LoginComponent}
      ]
    },
    {
      path: 'account', canActivate: [AuthGuard], component: BlogComponent, children: [
        {
          path: '', component: AccountComponent, children: [
            {path: 'profile', component: ProfileComponent},
            {path: 'articles', component: MyArticlesComponent},
            {path: 'drafts', component: MyArticlesComponent},
            {path: 'messages', component: MyMessagesComponent}
          ]
        },
        {path: 'articles/:id', component: ArticleComponent, resolve: {article: MyArticleResolver}},
        {
          path: 'drafts/:id', component: ArticleComponent, resolve: {article: DraftResolver}
        }
      ]
    }
  ]
;

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
