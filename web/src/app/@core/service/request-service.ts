import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Request, RequestServiceData, RequestStatus, RequestType } from '../data/request-service-data';

@Injectable()
export class RequestService extends RequestServiceData {

  newUserRequestName: string = 'NewUserRequest';

  private uri: string = environment.baseEndpoint;

  constructor(private _http: HttpClient) {
    super();
  }

  getAllNewUserRequests(): Observable<Request[]> {
    const _endpoint = this.uri +  '/management/requests?requestType=' + this.newUserRequestName + '&status=' + RequestStatus[RequestStatus.NEW];
    return this._http.get<Request[]>(_endpoint);
  }

  acceptRequest(requestId: number): Observable<Request> {
    const _endpoint = this.uri +  '/management/requests/' + requestId + '/accept';
    return this._http.put<Request>(_endpoint, null);
  }

  createRequestType(requestType: RequestType): Observable<RequestType> {
    const _endpoint = this.uri +  '/management/requestTypes';
    return this._http.post<RequestType>(_endpoint, requestType);
  }

  getAllRequestTypes(): Observable<RequestType[]> {
    const _endpoint = this.uri +  '/management/requestTypes';
    return this._http.get<RequestType[]>(_endpoint);
  }

  getAllUserRequestTypes(): Observable<RequestType[]> {
    const _endpoint = this.uri +  '/requestTypes';
    return this._http.get<RequestType[]>(_endpoint);
  }

  deleteRequestType(id: number): Observable<void> {
    const _endpoint = this.uri +  '/management/requestTypes/' + id;
    return this._http.delete<void>(_endpoint);
  }

  getRequestTypeById(id: number): Observable<RequestType> {
    const _endpoint = this.uri +  '/management/requestTypes/' + id;
    return this._http.get<RequestType>(_endpoint);
  }

  getRequestTypeByName(name: string): Observable<RequestType> {
    const _endpoint = this.uri +  '/requestTypes/' + name;
    return this._http.get<RequestType>(_endpoint);
  }

  updateRequestType(id: number, requestType: RequestType): Observable<RequestType> {
    const _endpoint = this.uri +  '/management/requestTypes/' + id;
    return this._http.put<RequestType>(_endpoint, requestType);
  }



}
