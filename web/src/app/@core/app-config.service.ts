import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NbPasswordAuthStrategy } from '@nebular/auth';

@Injectable()
export class AppConfigService {
  private appConfig;

  constructor(private http: HttpClient, private passwordStrategy: NbPasswordAuthStrategy) {
  }

  loadAppConfig() {
    return this.http.get('./assets/config/backend.json')
      .toPromise()
      .then(data => {
        this.appConfig = data;
      });
  }

  getConfig() {
    return this.appConfig;
  }
}
