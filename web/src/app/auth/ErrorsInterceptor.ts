import { HttpInterceptor, HttpHandler, HttpRequest, HttpEvent, HttpResponse, HttpErrorResponse }   from '@angular/common/http';
import { Injectable, Inject } from "@angular/core"
import { Observable, of } from "rxjs";
import { catchError } from "rxjs/operators";
import { Router } from '@angular/router';
import { NbToastrService } from '@nebular/theme';
import { Toaster } from '../pages/Toaster';

@Injectable()
export class ErrorsInterceptor implements HttpInterceptor {

    toaster: Toaster;

    constructor(private router: Router, private toastrService: NbToastrService) {
      this.toaster = new Toaster(toastrService);
    }

    intercept(
        req: HttpRequest<any>,
        next: HttpHandler
      ): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(
            catchError((err: any) => {
                if(err instanceof HttpErrorResponse) {
                  if (err.status == 401){
                    this.router.navigate(['auth/login']);
                    this.toaster.showToast(this.toaster.types[4], "Error", "Not authorized");
                  }
                }
                if (err.status <= 0){ //connection refused
                  this.toaster.showToast(this.toaster.types[4], "Error", "Server communication error. Connection refused");
                }
                return next.handle(req);
            }));

    }

}