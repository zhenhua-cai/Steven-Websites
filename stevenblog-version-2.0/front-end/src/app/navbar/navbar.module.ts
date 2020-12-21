import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NavbarComponent} from './navbar.component';
import {SharedModule} from '../shared/shared.module';
import {InputTextModule} from 'primeng/inputtext';
import {FormsModule} from '@angular/forms';
import {NavbarSearchComponent} from './narbar-search/navbar-search.component';


@NgModule({
  declarations: [
    NavbarComponent,
    NavbarSearchComponent
  ],
  imports: [
    SharedModule,
    InputTextModule,
    FormsModule
  ],
  exports: [
    NavbarComponent,
  ]
})
export class NavbarModule {
}
