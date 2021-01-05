import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-congrats',
  templateUrl: './sign-up-complete.component.html',
  styleUrls: ['./sign-up-complete.component.css']
})
export class SignUpCompleteComponent implements OnInit {
  timeCountDown: number;

  constructor(private router: Router) {
  }

  ngOnInit(): void {
    this.startsResendEmailTimeCountDown();
  }

  startsResendEmailTimeCountDown(): void {
    this.timeCountDown = 10;
    const resendEmailTimeInterval = setInterval(
      () => {
        this.timeCountDown--;
        if (this.timeCountDown === 0) {
          clearInterval(resendEmailTimeInterval);
          this.router.navigate(['/']);
        }
      }, 1000);
  }

}
