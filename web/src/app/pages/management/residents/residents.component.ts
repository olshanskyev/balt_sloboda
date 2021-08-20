import { Component, EventEmitter, Output} from '@angular/core';

import {
  NbToastrService,
} from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { LocalDataSource } from 'ng2-smart-table';
import { Request } from '../../../@core/data/request-service-data';
import { Resident } from '../../../@core/data/resident-service-data';
import { RequestService } from '../../../@core/service/request-service';
import { ResidentsService } from '../../../@core/service/residents-service';
import { Toaster } from '../../Toaster';



@Component({
  selector: 'ngx-residents',
  templateUrl: './residents.component.html',
  styleUrls: ['./residents.component.scss'],
})

export class ResidentsComponent {

  private toaster: Toaster;

  @Output() newUserRequestsChanged: EventEmitter<number> = new EventEmitter();

  getNewUserRequestsValueChanged() {
    return this.newUserRequestsChanged;
  }


  translations: any;
  constructor(private toastrService: NbToastrService, translateService: TranslateService,
    residentsService: ResidentsService,
    requestsService: RequestService) {
    this.toaster = new Toaster(toastrService);

    this.translations = translateService.translations[translateService.currentLang];
    this.settings.columns.userName.title = this.translations.residentsPage.user;
    this.settings.columns.firstName.title = this.translations.residentsPage.firstName;
    this.settings.columns.lastName.title = this.translations.residentsPage.lastName;
    this.settings.columns.address.title = this.translations.residentsPage.address;

    this.settingsRequests.columns.userName.title = this.translations.residentsPage.user;
    this.settingsRequests.columns.firstName.title = this.translations.residentsPage.firstName;
    this.settingsRequests.columns.lastName.title = this.translations.residentsPage.lastName;
    this.settingsRequests.columns.address.title = this.translations.residentsPage.address;
    this.settingsRequests.columns.status.title = this.translations.residentsPage.status;


    residentsService.getAllResidents().subscribe(
      res => {
      this.source.load(this.getTableView(res));
      this.count = res.length;
    }, err => {
      this.toaster.showToast(this.toaster.types[4], this.translations.errors.cannotGetUsers,
           `${err.error}. ${this.translations.errors.errorCode}: ${err.status}`);
    });

    requestsService.getAllNewUserRequests().subscribe(
      res => {
        this.sourceRequests.load(this.getTableViewRequests(res));
        this.countRequests = res.length;
        this.newUserRequestsChanged.emit(this.countRequests);
      }
    );



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

  getTableViewRequests(requests: Request[]) {
    return requests.map((item) => { // map json item into table view
      return {
        userName: item.paramValues.userName,
        firstName: item.paramValues.firstName,
        lastName: item.paramValues.lastName,
        status: item.status,
        address: item.paramValues.street +  ' ' + item.paramValues.houseNumber + ', '
        + this.translations.residentsPage.pl + ' ' + item.paramValues.plotNumber,
      };
    });
  }


  settingsRequests = {
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
      status: {
        title: '',
        type: 'string',
        editable: false,
      },

    },
  };


}
