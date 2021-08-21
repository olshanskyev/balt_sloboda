import { HttpInterceptor, HttpHandler, HttpRequest, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { AfterContentInit, Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { NbToastrService } from '@nebular/theme';
import { Toaster } from '../pages/Toaster';
import { TranslateService } from '@ngx-translate/core';

@Injectable()
export class ErrorsInterceptor implements HttpInterceptor{

    toaster: Toaster;
    currentLang: string;
    constructor(private router: Router, private toastrService: NbToastrService,
      private translateService: TranslateService) {
      this.toaster = new Toaster(toastrService);
    }

    intercept(
        req: HttpRequest<any>,
        next: HttpHandler,
      ): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(
            catchError((err: any) => {
                let errorMsg = '';
                let errorParameters;
                if (err instanceof HttpErrorResponse) {
                  errorMsg = err.error.error;
                  errorParameters = err.error.parameters
                  if (err.status === 401) {

                    this.toaster.showToast(this.toaster.types[4],
                      this.translateService.translations[this.translateService.currentLang].errors.error,
                      this.translateService.translations[this.translateService.currentLang].errors.Unauthorized);
                      this.router.navigate(['auth/login']);
                    return throwError(err);
                  }

                }
                if (err.status <= 0) { // connection refused
                  errorMsg = 'serverCommunicationError';
                }

                // translate
                this.translateService.get('errors.' + errorMsg, errorParameters).subscribe(res => {
                  this.toaster.showToast(this.toaster.types[4],
                    this.translateService.translations[this.translateService.currentLang].errors.error,
                     res);
                });
                return throwError(err);
            }));

    }

}
