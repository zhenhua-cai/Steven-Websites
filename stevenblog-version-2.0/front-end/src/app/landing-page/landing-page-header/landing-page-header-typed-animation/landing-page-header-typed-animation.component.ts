import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import Typed from 'typed.js';

@Component({
  selector: 'app-landing-page-header-typed-animation',
  templateUrl: './landing-page-header-typed-animation.component.html',
  styleUrls: ['./landing-page-header-typed-animation.component.css']
})
export class LandingPageHeaderTypedAnimationComponent implements OnInit, AfterViewInit {
  @ViewChild('introduction', {static: true}) introduction: ElementRef;

  constructor() {
  }

  ngOnInit(): void {
  }

  ngAfterViewInit(): void {
    // call js function, starts typed.js animation
    const prefix = '<span class="display-4 font-weight-bold white-text pt-5 mb-2 text-left" >Hi there,</span> <br/>';
    const introductions = {
      strings: [prefix,
        prefix + 'My name is Steven Cai',
        prefix + 'I\'m a software developer.',
        prefix + 'I love coding,',
        prefix + 'I love reading,',
        prefix + 'Welcome to my blog!'],
      typeSpeed: 80,
      backSpeed: 50,
      backDelay: 500,
      startDelay: 100,
      loop: true
    };
    // @ts-ignore
    // tslint:disable-next-line:no-unused-expression
    new Typed(this.introduction.nativeElement, introductions);
  }

}
