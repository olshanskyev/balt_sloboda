import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Resident, ResidentsServiceData } from '../data/resident-service-data';

@Injectable()
export class ResidentsService extends ResidentsServiceData {


  private uri: string = environment.baseEndpoint;

  constructor(private _http: HttpClient) {
    super();
  }

  getAllResidents(): Observable<Resident[]> {
    const _endpoint = this.uri +  '/management/residents';
    return this._http.get<Resident[]>(_endpoint);
  }

  getResidentByUserName(userName: string): Observable<Resident> {
    const _endpoint = this.uri +  '/management/residents/' + userName;
    return this._http.get<Resident>(_endpoint);
  }




}
