import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Request, RequestServiceData } from '../data/request-service-data';

@Injectable()
export class RequestService extends RequestServiceData {


  newUserRequestName: string = 'NewUserRequest';

  private uri: string = environment.baseEndpoint;

  constructor(private _http: HttpClient) {
    super();
  }

  getAllNewUserRequests(): Observable<Request[]> {
    const _endpoint = this.uri +  '/management/requests?requestType=' + this.newUserRequestName;
    return this._http.get<Request[]>(_endpoint);
  }

}
