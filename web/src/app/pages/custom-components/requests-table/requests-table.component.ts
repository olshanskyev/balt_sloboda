import { Component, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {
  NbToastrService,
} from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { LocalDataSource } from 'ng2-smart-table';
import { Request } from '../../../@core/data/request-service-data';
import { RequestService } from '../../../@core/service/request-service';
import { Toaster } from '../../Toaster';
import * as moment from 'moment';

@Component({
  selector: 'ngx-requests-table',
  templateUrl: './requests-table.component.html',
  styleUrls: ['./requests-table.component.scss'],
})
export class RequestsTableComponent implements OnChanges {

  private toaster: Toaster;
  translations: any;

  @Input() requests: Request[];

  constructor(private toastrService: NbToastrService, translateService: TranslateService,
    private requestsService: RequestService) {
    this.toaster = new Toaster(toastrService);
    this.translations = translateService.translations[translateService.currentLang];

     this.settings.columns.status.title = this.translations.requests.status;
     this.settings.columns.paramValues.title = this.translations.requests.paramValues;
     this.settings.columns.comment.title = this.translations.requests.comment;
     this.settings.columns.assignedTo.title = this.translations.requests.assignedTo;
     this.settings.columns.lastModified.title = this.translations.requests.lastModified;
     this.settings.columns.createDateTime.title = this.translations.requests.createDateTime;

    this.settings.edit.editButtonContent= '<i class="nb-edit" title="' + this.translations.common.edit + '"></i>';
    this.settings.delete.deleteButtonContent= '<i class="nb-close" title="' + this.translations.common.delete + '"></i>';

    this.settings.actions.columnTitle = this.translations.common.actions;

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
      status: {
        title: '',
        type: 'string',
        editable: false,
      },
      paramValues: {
        title: '',
        type: 'string',
        editable: false,
      },
      comment: {
        title: '',
        type: 'string',
        editable: false,
      },
      assignedTo: {
        title: '',
        type: 'string',
        editable: false,
      },
      createDateTime: {
        title: '',
        type: 'string',
        editable: false,
      },
      lastModified: {
        title: '',
        type: 'string',
        editable: false,
      }
    },
  };

  formatDateTime(date: Date): string {
    return (date != null) ? moment(date).format('DD.MM.YYYY HH:mm:ss') : '';
  }

  getTableItemView(request: Request) {
    return {
      id: request.id,
      comment: request.comment,
      paramValues: request.paramValues,
      status: request.status,
      createDateTime: this.formatDateTime(request.createDateTime),
      lastModified: request.lastModifiedBy.userName + ' ' + this.formatDateTime(request.lastModifiedDate),
      assignedTo: request.assignedTo.userName,
    };
  }

  getTableView(versions: Request[]) {
    return versions.map((item) => { // map json item into table view
      return this.getTableItemView(item);
    });
  }

  refreshTable() {
    this.source.load(this.getTableView(this.requests));
    this.count = this.requests.length;
  }


  ngOnChanges(changes: SimpleChanges): void {

    let toUpdate = false;
    if (changes['requests'] && changes['requests'].currentValue) {
      this.requests = changes['requests'].currentValue;
      this.refreshTable();
    }
  }

  onEdit(event) {

  }

  onDelete(event) {

  }

}
