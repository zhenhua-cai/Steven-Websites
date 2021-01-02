import {Injectable} from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpEventType
} from '@angular/common/http';
import {Observable, Subject, throwError} from 'rxjs';
import {AppService} from '../app.service';
import {catchError, map, switchMap, tap} from 'rxjs/operators';


@Injectable()
export class AddAuthTokenInterceptor implements HttpInterceptor {

  constructor(private appService: AppService) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = this.appService.getAccessToken();
    if (!!token) {
      request = request.clone({
        headers: request.headers.set('Authorization', token)
      });
      return next.handle(request);
    }
    return next.handle(request);
  }

}
