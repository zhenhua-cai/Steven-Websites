import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ResetPasswordComponent} from './reset-password.component';
import { ResetPasswordFormsComponent } from './reset/reset-password-forms.component';
import {InputTextModule} from 'primeng/inputtext';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ButtonModule} from 'primeng/button';
import {RouterModule} from '@angular/router';
import {CardModule} from 'primeng/card';
import {PasswordModule} from 'primeng/password';
import {TooltipModule} from 'primeng/tooltip';



@NgModule({
  declarations: [
    ResetPasswordComponent,
    ResetPasswordFormsComponent
  ],
  imports: [
    CommonModule,
    InputTextModule,
    FormsModule,
    ButtonModule,
    RouterModule,
    CardModule,
    PasswordModule,
    ReactiveFormsModule,
    TooltipModule
  ]
})
export class ResetPasswordModule { }
