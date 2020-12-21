import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-landing-page-header',
  templateUrl: './landing-page-header.component.html',
  styleUrls: ['./landing-page-header.component.css'],
  animations: []
})
export class LandingPageHeaderComponent implements OnInit {
  @Input()
  stayInPage = true;

  constructor() {
  }

  ngOnInit(): void {

  }
}
