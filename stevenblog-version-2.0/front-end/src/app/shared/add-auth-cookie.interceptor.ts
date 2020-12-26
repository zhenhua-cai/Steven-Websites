import {Injectable} from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class AddAuthCookieInterceptor implements HttpInterceptor {

  constructor() {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const user = JSON.parse(localStorage.getItem('user'));
    if (!!user) {
      request = request.clone(
        {
          headers: request.headers.set('Authorization', user.token)
        }
      );
    }

    return next.handle(request);
  }
}
