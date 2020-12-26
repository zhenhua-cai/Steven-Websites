import {NgModule} from '@angular/core';
import {ProfileComponent} from './profile/profile.component';
import {AccountComponent} from './account.component';
import {SharedModule} from '../shared/shared.module';
import {TabMenuModule} from 'primeng/tabmenu';
import {MyArticlesComponent} from './my-articles/my-articles.component';
import {MyMessagesComponent} from './my-messages/my-messages.component';
import {TableModule} from 'primeng/table';
import {InputTextModule} from 'primeng/inputtext';
import {ButtonModule} from 'primeng/button';
import {ContextMenuModule} from 'primeng/contextmenu';
import {ToastModule} from 'primeng/toast';
import {FormsModule} from '@angular/forms';


@NgModule({
  declarations: [
    AccountComponent,
    ProfileComponent,
    MyArticlesComponent,
    MyMessagesComponent
  ],
  imports: [
    SharedModule,
    TabMenuModule,
    TableModule,
    InputTextModule,
    ButtonModule,
    ContextMenuModule,
    ToastModule,
    FormsModule
  ]
})
export class AccountModule {
}
