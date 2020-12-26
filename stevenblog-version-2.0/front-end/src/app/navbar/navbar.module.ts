import {NgModule} from '@angular/core';
import {NavbarComponent} from './navbar.component';
import {SharedModule} from '../shared/shared.module';
import {InputTextModule} from 'primeng/inputtext';
import {FormsModule} from '@angular/forms';
import {NavbarSearchComponent} from './narbar-search/navbar-search.component';
import {MDBBootstrapModule, WavesModule, ButtonsModule, BadgeModule} from 'angular-bootstrap-md';

@NgModule({
  declarations: [
    NavbarComponent,
    NavbarSearchComponent
  ],
  imports: [
    SharedModule,
    InputTextModule,
    FormsModule,
    MDBBootstrapModule.forRoot(),
    WavesModule,
    ButtonsModule,
    BadgeModule
  ],
  exports: [
    NavbarComponent,
  ]
})
export class NavbarModule {
}
