import {NgModule} from '@angular/core';
import {ContactComponent} from './contact.component';
import {SharedModule} from '../shared/shared.module';
import {NavbarModule} from '../navbar/navbar.module';


@NgModule({
  declarations: [
    ContactComponent
  ],
  imports: [
    SharedModule,
    NavbarModule
  ]
})
export class ContactModule {
}
