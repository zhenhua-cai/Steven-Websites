import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {LandingPageHeaderComponent} from './landing-page-header/landing-page-header.component';
import {LandingPageHeaderAnimationComponent} from './landing-page-header/landing-page-header-animation/landing-page-header-animation.component';
import {LandingPageComponent} from './landing-page.component';
import {LandingPageHeaderTypedAnimationComponent} from './landing-page-header/landing-page-header-typed-animation/landing-page-header-typed-animation.component';
import {SharedModule} from '../shared/shared.module';
import {NavbarModule} from '../navbar/navbar.module';



@NgModule({
  declarations: [
    LandingPageHeaderComponent,
    LandingPageHeaderAnimationComponent,
    LandingPageComponent,
    LandingPageHeaderTypedAnimationComponent
  ],
  imports: [
    SharedModule,
    NavbarModule
  ]
})
export class LandingPageModule { }
