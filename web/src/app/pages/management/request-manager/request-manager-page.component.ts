
import { Component} from '@angular/core';
import { Router } from '@angular/router';

import {
  NbDateService,
  NbToastrService,
} from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { LocalDataSource } from 'ng2-smart-table';
import { RequestType } from '../../../@core/data/request-service-data';

import { User } from '../../../@core/data/user-service-data';

import { RequestService } from '../../../@core/service/request-service';
import { UserService } from '../../../@core/service/users-service';

import { Toaster } from '../../Toaster';

export interface UserGroup {
  name: string;
  users: User[];
}

@Component({
  selector: 'ngx-request-manager',
  templateUrl: './request-manager-page.component.html',
  styleUrls: ['./request-manager-page.component.scss'],
})
export class RequestManagerPageComponent {

  private toaster: Toaster;
  translations: any;
  showSystemRequests: boolean = false;

  constructor(private toastrService: NbToastrService, private translateService: TranslateService,
    protected dateService: NbDateService<Date>,
    private userService: UserService,
    private requestsService: RequestService,
    private router: Router) {
    this.toaster = new Toaster(toastrService);
    this.translations = translateService.translations[translateService.currentLang];

    this.settings.columns.title.title = this.translations.requestManagerPage.title;
    this.settings.columns.description.title = this.translations.requestManagerPage.description;
    this.settings.columns.durable.title = this.translations.requestManagerPage.durable;

    this.settings.edit.editButtonContent= '<i class="nb-edit" title="' + this.translations.common.edit + '"></i>';
    this.settings.delete.deleteButtonContent= '<i class="nb-close" title="' + this.translations.common.delete + '"></i>';

    this.settings.actions.columnTitle = this.translations.common.actions;


    this.loadRequestTypes();
  }

  createRequestType(requestType: RequestType) {
    this.requestsService.createRequestType(requestType).subscribe( res => {
      this.toaster.showToast(this.toaster.types[1], this.translations.requestManagerPage.requestTypeCreated,
        '');
      this.loadRequestTypes();
      this.requestsService.notifyUserRequestTypesChanged(); // notify to update menu
    });
  }

  requestTypes: RequestType[];

  refreshTable() {
    var types: RequestType[] = (this.showSystemRequests)?
      this.requestTypes:
      this.requestTypes.filter(item => !item.systemRequest);

    this.source.load(this.getTableView(types));
    this.count = types.length;
  }

  private loadRequestTypes() {
    this.requestsService.getAllRequestTypes().subscribe( res => {
      this.requestTypes = res;
      this.refreshTable();
    });
  }

  source: LocalDataSource = new LocalDataSource();

  count: number = 0;

  settings = {
    actions: {
      add: false,
      columnTitle: '',
    },
    edit: {
      editButtonContent: '',
    },
    delete: {
      deleteButtonContent: '',
    },
    mode: 'external',
    columns: {
      title: {
        title: '',
        type: 'string',
        editable: false,
      },
      description: {
        title: '',
        type: 'string',
        editable: false,
      },
      durable: {
        title: '',
        type: 'string',
        editable: false,
      },

    },
  };

  getTableView(requests: RequestType[]) {
    return requests.map((item) => { // map json item into table view
      return {
        requestType: item,
        title: item.title,
        description: item.description,
        durable: (item.durable)? this.translations.common.yes: this.translations.common.no,
      };
    });
  }

  onEdit(event) {
    this.router.navigate(['pages/management/requestTypes', event.data.requestType.id]);
  }

  onDelete(event) {
    if (event.data.requestType.systemRequest) {
      this.toaster.showToast(this.toaster.types[3], this.translations.errors.systemActionCannotBeDeleted,'');
      return;
    }

    this.translateService.get('requestManagerPage.shureDeleteRequest', {request: event.data.title}).subscribe(
      translated => {
        if (window.confirm(translated)) {
          this.requestsService.deleteRequestType(event.data.requestType.id).subscribe(() => {
            this.toaster.showToast(this.toaster.types[1], this.translations.requestManagerPage.requestTypeDeleted,'');
            this.loadRequestTypes(); // update table
            this.requestsService.notifyUserRequestTypesChanged(); // notify to update menu
            this
          });

        }
      });
  }

}
