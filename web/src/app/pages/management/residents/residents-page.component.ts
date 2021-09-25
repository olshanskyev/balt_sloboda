import { Component, EventEmitter, Injectable, OnDestroy, Output} from '@angular/core';

import {
  NbToastrService,
} from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { LocalDataSource } from 'ng2-smart-table';
import { Subscription } from 'rxjs';
import { Request } from '../../../@core/data/request-service-data';
import { Resident } from '../../../@core/data/resident-service-data';
import { RequestService } from '../../../@core/service/request-service';
import { ResidentsService } from '../../../@core/service/residents-service';
import { Toaster } from '../../Toaster';

@Component({
  selector: 'ngx-residents',
  templateUrl: './residents-page.component.html',
  styleUrls: ['./residents-page.component.scss'],
})
@Injectable()
export class ResidentsPageComponent implements OnDestroy{

  private toaster: Toaster;
  activeRequests: Request[] = [];
  requests: Request[] = [];
  showOnlyActiveRequests: boolean = true;

  translations: any;
  constructor(private toastrService: NbToastrService, private translateService: TranslateService,
    private residentsService: ResidentsService,
    private requestsService: RequestService) {
    this.toaster = new Toaster(toastrService);

    this.translations = translateService.translations[translateService.currentLang];
    this.settings.columns.userName.title = this.translations.residentsPage.user;
    this.settings.columns.firstName.title = this.translations.residentsPage.firstName;
    this.settings.columns.lastName.title = this.translations.residentsPage.lastName;
    this.settings.columns.address.title = this.translations.residentsPage.address;

    this.loadResidents();
    this.loadNewRequests();
  }

  ngOnDestroy(): void {
    if (this.allNewUserRequestSubscription !== null)
      this.allNewUserRequestSubscription.unsubscribe();
  }

  private allNewUserRequestSubscription: Subscription;

  private loadNewRequests(): void {
    this.allNewUserRequestSubscription = this.requestsService.getActiveNewUserRequestsSubscription().subscribe(
      res => {
        if (res) {
          this.activeRequests = res.content;
          this.countRequests = res.content.length;
          this.refreshRequests();
        }
      },  err => {
        this.toaster.showToast(this.toaster.types[4], this.translations.errors.cannotGetRequests,
             '');
      }
    );
  }

  private loadResidents(): void {
    this.residentsService.getAllResidents().subscribe(
      res => {
      this.source.load(this.getTableView(res));
      this.count = res.length;
    }, err => {
      this.toaster.showToast(this.toaster.types[4], this.translations.errors.cannotGetUsers,
           '');
    });
  }


  source: LocalDataSource = new LocalDataSource();
  sourceRequests: LocalDataSource = new LocalDataSource();

  count: number = 0;
  countRequests: number = 0;
  settings = {
    actions: {
      add: false,
      edit: false,
      delete: false,
    },

    columns: {
      userName: {
        title: '',
        type: 'string',
        editable: false,
      },
      firstName: {
        title: '',
        type: 'string',
        editable: false,
      },
      lastName: {
        title: '',
        type: 'string',
        editable: false,
      },
      address: {
        title: '',
        type: 'string',
        editable: false,
      },


    },
  };

  getTableView(residents: Resident[]) {
    return residents.map((item) => { // map json item into table view
      return {
        userName: item.user.userName,
        firstName: item.firstName,
        lastName: item.lastName,
        address: item.address.street +  ' ' + item.address.houseNumber + ', '
        + this.translations.residentsPage.pl + ' ' + item.address.plotNumber,

      };
    });
  }

  userAccepted(event) : void {
    this.requestsService.notifyNewUserRequestsChanged();
    this.loadResidents();
  }

  userRejected(event): void {
    this.requestsService.notifyNewUserRequestsChanged();
    this.loadResidents();
  }

  refreshRequests() { // get all new user requests
    if (!this.showOnlyActiveRequests) {
      this.requestsService.getAllNewUserRequests(0, 10).subscribe(res => {
        this.requests = res.content;
      });
    } else {
      this.requests = [];
    }
  }

}
