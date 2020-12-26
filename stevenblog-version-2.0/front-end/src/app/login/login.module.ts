import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {LoginComponent} from './login.component';
import {NavbarModule} from '../navbar/navbar.module';
import {FooterModule} from '../footer/footer.module';
import {InputTextModule} from 'primeng/inputtext';
import {FormsModule} from '@angular/forms';
import {InputSwitchModule} from 'primeng/inputswitch';
import {ButtonModule} from 'primeng/button';
import {RouterModule} from '@angular/router';
import {CheckboxModule} from 'primeng/checkbox';
import {ToastModule} from 'primeng/toast';
import {BlockUIModule} from 'primeng/blockui';
import {ProgressSpinnerModule} from 'primeng/progressspinner';

@NgModule({
  declarations: [
    LoginComponent
  ],
  imports: [
    CommonModule,
    NavbarModule,
    FooterModule,
    InputTextModule,
    FormsModule,
    InputSwitchModule,
    ButtonModule,
    RouterModule,
    CheckboxModule,
    ToastModule,
    BlockUIModule,
    ProgressSpinnerModule
  ],
  exports: [
    LoginComponent
  ]
})
export class LoginModule { }
