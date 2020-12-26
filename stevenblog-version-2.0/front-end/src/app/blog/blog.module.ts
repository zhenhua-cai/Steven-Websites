import {NgModule} from '@angular/core';
import {BlogComponent} from './blog.component';
import {SharedModule} from '../shared/shared.module';
import {NavbarModule} from '../navbar/navbar.module';
import {FooterModule} from '../footer/footer.module';
@NgModule({
  declarations: [
    BlogComponent,
  ],
  imports: [
    SharedModule,
    NavbarModule,
    FooterModule,
  ]
})
export class BlogModule {
}
