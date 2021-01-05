import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {SignUpComponent} from './sign-up.component';
import {PanelModule} from 'primeng/panel';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {StepsModule} from 'primeng/steps';
import { AccountInfoComponent } from './account-info/account-info.component';
import {CardModule} from 'primeng/card';
import {InputTextModule} from 'primeng/inputtext';
import {ButtonModule} from 'primeng/button';
import {PasswordModule} from 'primeng/password';
import {TooltipModule} from 'primeng/tooltip';
import { SignUpConfirmationComponent } from './sign-up-confirmation/sign-up-confirmation.component';
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import { VerifyEmailComponent } from './verify-email/verify-email.component';
import {InputNumberModule} from 'primeng/inputnumber';
import { SignUpCompleteComponent } from './complete/sign-up-complete.component';

@NgModule({
  declarations: [
    SignUpComponent,
    AccountInfoComponent,
    SignUpConfirmationComponent,
    VerifyEmailComponent,
    SignUpCompleteComponent
  ],
  imports: [
    CommonModule,
    PanelModule,
    FormsModule,
    StepsModule,
    CardModule,
    InputTextModule,
    ButtonModule,
    PasswordModule,
    TooltipModule,
    ConfirmDialogModule,
    ReactiveFormsModule,
    InputNumberModule
  ]
})
export class SignUpModule { }
