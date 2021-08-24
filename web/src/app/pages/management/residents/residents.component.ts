import { Component, EventEmitter, Injectable, Output} from '@angular/core';

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
import { NewRequestsService } from '../../../@core/service/new-requests-service';



@Component({
  selector: 'ngx-residents',
  templateUrl: './residents.component.html',
  styleUrls: ['./residents.component.scss'],
})
@Injectable()
export class ResidentsComponent {

  private toaster: Toaster;


  translations: any;
  constructor(private toastrService: NbToastrService, private translateService: TranslateService,
    private residentsService: ResidentsService,
    private requestsService: RequestService,
    private newRequeststService: NewRequestsService) {
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

    this.settingsRequests.edit.editButtonContent= '<i class="nb-checkmark" title="' + this.translations.residentsPage.accept + '"></i>',
    this.settingsRequests.delete.deleteButtonContent= '<i class="nb-close" title="' + this.translations.residentsPage.decline + '"></i>',

    this.loadResidents();
    this.loadNewRequests();
  }


  private loadNewRequests(): void {
    this.requestsService.getAllNewUserRequests().subscribe(
      res => {
        this.sourceRequests.load(this.getTableViewRequests(res));
        this.countRequests = res.length;
        this.newRequeststService.changeNewUserRequestsCount(this.countRequests);
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


  getTableViewRequests(requests: Request[]) {
    return requests.map((item) => { // map json item into table view
      return {
        id: item.id,
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
    },

    edit: {
      editButtonContent: '',
    },
    delete: {
      deleteButtonContent: '',
    },
    mode: 'external',
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

  onUserAccept(event) : void {
    this.translateService.get('residentsPage.shureAccept', {user: event.data.userName}).subscribe(
      translated => {
        if (window.confirm(translated)) {
          this.requestsService.acceptRequest(event.data.id).subscribe(() => {
            this.loadNewRequests();
            this.loadResidents();
          });
        }
      });
  }

  onDeclineUser(event) : void {
    this.countRequests++;
    this.newRequeststService.changeNewUserRequestsCount(this.countRequests);
  }


}
