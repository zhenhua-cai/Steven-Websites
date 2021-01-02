import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ErrorComponent} from './error.component';
import { AccessDeniedComponent } from './access-denied/access-denied.component';


@NgModule({
  declarations: [
    ErrorComponent,
    AccessDeniedComponent
  ],
  imports: [
    CommonModule
  ]
})
export class ErrorModule {
}
