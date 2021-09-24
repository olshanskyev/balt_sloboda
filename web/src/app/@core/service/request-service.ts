import { BehaviorSubject, Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Request, RequestLogItem, RequestServiceData, RequestStatus, RequestType } from '../data/request-service-data';
import { NbAccessChecker } from '@nebular/security';
import { Page } from '../data/page';

@Injectable()
export class RequestService extends RequestServiceData {

  newUserRequestName: string = 'NewUserRequest';

  private uri: string = environment.baseEndpoint;

  // ================= subscriptions =============

  // new user requests
  private _newUserRequestsSource = new BehaviorSubject<Page<Request>>(null);
  private _newUserRequests: Observable<Page<Request>> = this._newUserRequestsSource.asObservable();
  // request types
  private _requestTypesSource: BehaviorSubject<RequestType[]> = new BehaviorSubject<RequestType[]>(null);
  private _requestTypes: Observable<RequestType[]> = this._requestTypesSource.asObservable();
  // my active requests
  private _myActiveRequestsSource: BehaviorSubject<Page<Request>> = new BehaviorSubject<Page<Request>>(null);;
  private _myActiveRequests: Observable<Page<Request>> = this._myActiveRequestsSource.asObservable();
  // assigned to me active requests
  private _assignedToMeActiveRequestsSource: BehaviorSubject<Page<Request>> = new BehaviorSubject<Page<Request>>(null);;
  private _assignedToMeActiveRequests: Observable<Page<Request>> = this._assignedToMeActiveRequestsSource.asObservable();

  public notifyNewUserRequestsChanged() {
    this.getAllNewUserRequests([RequestStatus.NEW]).subscribe (requests => {
      this._newUserRequestsSource.next(requests);
    });

  }

  public getActiveNewUserRequestsSubscription(): Observable<Page<Request>> {
      return this._newUserRequests;
  }

  public notifyRequestTypesChanged() {
    this._getAllUserRequestTypes().subscribe( requestTypes => {
      this._requestTypesSource.next(requestTypes);
    });
  }

  public getRequestTypesSubscription(): Observable<RequestType[]> {
      return this._requestTypes;
  }

  public notifyMyActiveRequestsChanged() {
      this.getMyRequests([RequestStatus.NEW, RequestStatus.ACCEPTED, RequestStatus.IN_PROGRESS]).subscribe(requests => {
        this._myActiveRequestsSource.next(requests);
      });
  }

  public getMyActiveRequestsSubscription(): Observable<Page<Request>> {
    return this._myActiveRequests;
  }

  public notifyAssignedToMeActiveRequestsChanged() {
    this.getAssignedToMeRequests([RequestStatus.NEW, RequestStatus.ACCEPTED, RequestStatus.IN_PROGRESS]).subscribe(requests => {
      this._assignedToMeActiveRequestsSource.next(requests);
    });
  }

  public getAssignedToMeActiveRequestsSubscription(): Observable<Page<Request>> {
    return this._assignedToMeActiveRequests;
  }




  // ======= end ======= subscriptions =================

  constructor(private _http: HttpClient,
    accessChecker: NbAccessChecker) {
    super();
    // load new user requestst
    accessChecker.isGranted('read', 'newUserRequests').subscribe(granted => { // only for admins
      if (granted) {
        this.notifyNewUserRequestsChanged();
      }
    });
    // load request types
    this.notifyRequestTypesChanged();
    // load my active requests
    this.notifyMyActiveRequestsChanged();
    // load assigned to me requests
    this.notifyAssignedToMeActiveRequestsChanged();

  }

  // requests

  public getMyRequests(requestStatuses?: RequestStatus[]): Observable<Page<Request>> {
    var _endpoint = this.uri +  '/requests?page=0&size=10';
    _endpoint = _endpoint + ((!requestStatuses) ? '' : '&status=' + requestStatuses.map(item => RequestStatus[item]).join());
    return this._http.get<Page<Request>>(_endpoint);
  }

  public getAssignedToMeRequests(requestStatuses?: RequestStatus[]): Observable<Page<Request>> {
    let _endpoint = this.uri +  '/requests?page=0&size=10&assignedToMe=true';
    _endpoint = _endpoint + ((!requestStatuses) ? '' : '&status=' + requestStatuses.map(item => RequestStatus[item]).join());
    return this._http.get<Page<Request>>(_endpoint);
  }

  public getAllNewUserRequests(requestStatuses?: RequestStatus[]): Observable<Page<Request>> {
    let _endpoint = this.uri +  '/management/requests?page=0&size=10&requestType=' + this.newUserRequestName;
    _endpoint = _endpoint + ((!requestStatuses) ? '' : '&status=' + requestStatuses.map(item => RequestStatus[item]).join());
    return this._http.get<Page<Request>>(_endpoint);
  }

  acceptRequest(requestId: number): Observable<Request> {
    const _endpoint = this.uri +  '/requests/' + requestId + '/accept';
    return this._http.put<Request>(_endpoint, null);
  }

  rejectRequest(requestId: number, comment?: string): Observable<Request> {
    let _endpoint = this.uri +  '/requests/' + requestId + '/reject';
    if (comment && comment.length > 0) {
      _endpoint = _endpoint + '?comment=' + comment;
    }
    return this._http.put<Request>(_endpoint, null);
  }

  createRequest(request: Request): Observable<Request> {
    const _endpoint = this.uri +  '/requests';
    return this._http.post<Request>(_endpoint, request);
  }

  // request types

  createRequestType(requestType: RequestType): Observable<RequestType> {
    const _endpoint = this.uri +  '/management/requestTypes';
    return this._http.post<RequestType>(_endpoint, requestType);
  }

  getAllRequestTypes(): Observable<RequestType[]> {
    const _endpoint = this.uri +  '/management/requestTypes';
    return this._http.get<RequestType[]>(_endpoint);
  }

  private _getAllUserRequestTypes(): Observable<RequestType[]> {
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

  getRequestLogs(requestId: number): Observable<RequestLogItem[]> {
    const _endpoint = this.uri +  '/requestLogs/?requestId=' + requestId;
    return this._http.get<RequestLogItem[]>(_endpoint);
  }

}
