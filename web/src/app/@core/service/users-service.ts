import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { User, UserServiceData } from '../data/user-service-data';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable()
export class UserService extends UserServiceData {


  private uri: string = environment.baseEndpoint;

  constructor(private _http: HttpClient) {
    super();
  }

  getAllUsers(): Observable<User[]> {
    const _endpoint = this.uri +  '/management/users';
    return this._http.get<User[]>(_endpoint);
  }

  getUserByName(userName: string): Observable<User> {
    const _endpoint = this.uri +  '/management/users/' + userName;
    return this._http.get<User>(_endpoint);
  }

  getUserInfo(): Observable<User> {
    const _endpoint = this.uri +  '/userInfo';
    return this._http.get<User>(_endpoint);
  }




}
