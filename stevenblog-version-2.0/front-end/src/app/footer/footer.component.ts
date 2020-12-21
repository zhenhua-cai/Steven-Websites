import {Component, OnInit} from '@angular/core';
import {NavbarService} from '../navbar/navbar.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css'],
  animations: []
})
export class FooterComponent implements OnInit {
  currentYear: Date;

  constructor() {
    this.currentYear = new Date();
  }

  ngOnInit(): void {

  }
}
