import { NgModule } from '@angular/core';
import {AboutMeComponent} from './about-me.component';
import {SharedModule} from '../shared/shared.module';
import {NavbarModule} from '../navbar/navbar.module';



@NgModule({
  declarations: [
    AboutMeComponent
  ],
  imports: [
    SharedModule,
    NavbarModule
  ]
})
export class AboutMeModule { }
