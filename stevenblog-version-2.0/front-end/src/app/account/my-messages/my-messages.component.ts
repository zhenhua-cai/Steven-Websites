import { Component, OnInit } from '@angular/core';
import {AccountService} from '../account.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-my-messages',
  templateUrl: './my-messages.component.html',
  styleUrls: ['./my-messages.component.css']
})
export class MyMessagesComponent implements OnInit {

  constructor(private accountService: AccountService, private router: Router) { }

  ngOnInit(): void {
    this.accountService.accountRouteChange(this.router.url);
  }

}
