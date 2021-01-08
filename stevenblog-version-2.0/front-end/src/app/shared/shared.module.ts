import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {AppRoutingModule} from '../app-routing.module';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    AppRoutingModule,
    BrowserAnimationsModule,
  ],
  exports: [
    CommonModule,
    AppRoutingModule,
    BrowserAnimationsModule,
  ]
})
export class SharedModule {
}
