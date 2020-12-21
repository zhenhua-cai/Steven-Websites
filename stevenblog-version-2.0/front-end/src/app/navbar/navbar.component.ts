import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  animations: []
})
export class NavbarComponent implements OnInit {
  @Input()
  isLandingPage: boolean;
  isShowingSearchBar = false;

  constructor() {
  }

  ngOnInit(): void {

  }

  showSearchbar(): void {
    this.isShowingSearchBar = true;
  }

  handleEmptyAndBlurSearchArea(emptyAndBlur: boolean): void {
    this.isShowingSearchBar = !emptyAndBlur;
  }
}
