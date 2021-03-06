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
import {ArticleEditorComponent} from './article-editor/article-editor.component';
import {ArticleEditorGuard} from './article-editor/article-editor.guard';
import {NewArticleGuard} from './article-editor/new-article.guard';
import {PublishComponent} from './article-editor/publish/publish.component';
import {ArticlePublishTitleComponent} from './article-editor/publish/ArticlePublishTitle/ArticlePublishTitle.component';
import {ArticlePublishConfirmationComponent} from './article-editor/publish/article-publish-confirmation/article-publish-confirmation.component';
import {PublishGuard} from './article-editor/publish.guard';
import {ErrorComponent} from './error/error.component';
import {AccessDeniedComponent} from './error/access-denied/access-denied.component';
import {SignUpComponent} from './sign-up/sign-up.component';
import {AccountInfoComponent} from './sign-up/account-info/account-info.component';
import {SignUpConfirmationComponent} from './sign-up/sign-up-confirmation/sign-up-confirmation.component';
import {AccountInfoGuard} from './sign-up/account-info/account-info.guard';
import {SignUpConfirmationGuard} from './sign-up/sign-up-confirmation/sign-up-confirmation.guard';
import {VerifyEmailComponent} from './sign-up/verify-email/verify-email.component';
import {SignUpCompleteComponent} from './sign-up/complete/sign-up-complete.component';
import {ResetPasswordComponent} from './reset-password/reset-password.component';
import {ResetPasswordFormsComponent} from './reset-password/reset/reset-password-forms.component';


const routes: Routes = [
    {path: '', component: LandingPageComponent},
    {path: 'home', redirectTo: ''},
    {
      path: 'articles', component: BlogComponent, children: [
        {path: '', component: ArticlesListComponent, resolve: {articlesResponse: ArticlesResolver}},
        {path: 'search/:ArticlePublishTitle', component: ArticlesListComponent, resolve: {articlesResponse: SearchArticlesResolver}},
        {path: ':id', component: ArticleComponent, resolve: {articleResponse: ArticleResolver}}
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
      path: 'sign-up', component: BlogComponent, children: [
        {
          path: '', component: SignUpComponent, children: [
            {path: 'account-info', component: AccountInfoComponent, canDeactivate: [AccountInfoGuard]},
            {path: 'confirmation', component: SignUpConfirmationComponent, canDeactivate: [SignUpConfirmationGuard]},
            {path: 'verifyEmail', component: VerifyEmailComponent},
            {path: 'complete', component: SignUpCompleteComponent}
          ]
        }
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
        {path: 'articles/:id', component: ArticleComponent, resolve: {articleResponse: MyArticleResolver}},
        {
          path: 'drafts/:id', component: ArticleComponent, resolve: {articleResponse: DraftResolver}
        },
      ]
    },
    {
      path: 'account/new',
      canActivate: [AuthGuard, NewArticleGuard, ArticleEditorGuard],
      canDeactivate: [ArticleEditorGuard, NewArticleGuard],
      component: ArticleEditorComponent
    },
    {
      path: 'account/edit/:id',
      canActivate: [AuthGuard, ArticleEditorGuard],
      canDeactivate: [ArticleEditorGuard],
      component: ArticleEditorComponent
    },
    {
      path: 'account/publish', canActivate: [AuthGuard, PublishGuard], canActivateChild: [AuthGuard, PublishGuard],
      canDeactivate: [PublishGuard], component: PublishComponent, children: [
        {path: 'title', component: ArticlePublishTitleComponent},
        {path: 'confirmation', component: ArticlePublishConfirmationComponent}
      ]
    },
    {
      path: 'error', component: BlogComponent, children: [
        {path: '', component: ErrorComponent},
        {path: 'access-denied', component: AccessDeniedComponent}
      ]
    },
    {
      path: 'reset-password', component: BlogComponent, children: [
        {
          path: '', component: ResetPasswordComponent, children: [
            {path: 'verify', component: VerifyEmailComponent},
            {path: 'reset', component: ResetPasswordFormsComponent}
          ]
        },
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
