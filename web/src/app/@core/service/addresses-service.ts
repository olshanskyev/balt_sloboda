import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Address, AddressesServiceData } from '../data/addresses-service-data';

@Injectable()
export class AddressesService extends AddressesServiceData {


  private uri: string = environment.baseEndpoint;

  constructor(private _http: HttpClient) {
    super();
  }

  getAllStreets(): Observable<string[]> {
    const _endpoint = this.uri +  '/streets';
    return this._http.get<string[]>(_endpoint);
  }

  getAddressesOnStreet(street: string): Observable<Address[]> {
    const _endpoint = this.uri +  '/streets/' + street + '/addresses';
    return this._http.get<Address[]>(_endpoint);
  }

  getAllAddresses(): Observable<Address[]>{
    const _endpoint = this.uri +  '/addresses';
    return this._http.get<Address[]>(_endpoint);
  }




}
