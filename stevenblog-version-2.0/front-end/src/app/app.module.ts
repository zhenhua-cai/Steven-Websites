import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ArticlesModule} from './articles/articles.module';
import {NavbarModule} from './navbar/navbar.module';
import {LandingPageModule} from './landing-page/landing-page.module';
import {AboutMeModule} from './about-me/about-me.module';
import {ContactModule} from './contact/contact.module';
import {SharedModule} from './shared/shared.module';
import {HttpClientModule} from '@angular/common/http';
import { NavbarSearchComponent } from './navbar/narbar-search/navbar-search.component';

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ArticlesModule,
    NavbarModule,
    LandingPageModule,
    AboutMeModule,
    ContactModule,
    SharedModule,
    HttpClientModule,
  ],
  providers: [],
  exports: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
