import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {LandingPageComponent} from './landing-page/landing-page.component';
import {ArticlesComponent} from './articles/articles.component';
import {AboutMeComponent} from './about-me/about-me.component';
import {ContactComponent} from './contact/contact.component';
import {ArticleComponent} from './articles/article/article.component';
import {ArticlesListComponent} from './articles/articles-list/articles-list.component';

const routes: Routes = [
  {path: '', component: LandingPageComponent},
  {path: 'home', redirectTo: ''},
  {
    path: 'articles', component: ArticlesComponent, children: [
      {path: '', component: ArticlesListComponent},
      {path: 'search/:title', component: ArticlesListComponent},
      {path: ':id', component: ArticleComponent}
    ]
  },
  {
    path: 'about', component: AboutMeComponent
  },
  {
    path: 'contact', component: ContactComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
