import { Component, Input, OnChanges, SimpleChanges} from '@angular/core';

import { TranslateService } from '@ngx-translate/core';
import { LocalDataSource } from 'ng2-smart-table';
import { Request, RequestParam, RequestParamType, RequestStatus } from '../../../@core/data/request-service-data';
import * as moment from 'moment';
import { CalendarSelectionService } from '../../../@core/service/calendar-selection.service';
import { CalendarSelectionDataBuilder } from '../../../@core/data/calendar-selection.data';
import { NbDateService, NbToastrService } from '@nebular/theme';
import { RequestService } from '../../../@core/service/request-service';
import { Toaster } from '../../Toaster';

@Component({
  selector: 'ngx-requests-list',
  templateUrl: './requests-list.component.html',
  styleUrls: ['./requests-list.component.scss'],
})
export class RequestsListComponent implements OnChanges {

  translations: any;

  RequestStatus = RequestStatus;
  RequestParamType = RequestParamType;
  @Input() requests: Request[];

  private toaster: Toaster;

  constructor(private translateService: TranslateService,
    private dateService: NbDateService<Date>,
    private requestsService: RequestService,
    private toastrService: NbToastrService) {
    this.toaster = new Toaster(toastrService);
    this.translations = translateService.translations[translateService.currentLang];


  }

  source: LocalDataSource = new LocalDataSource();

  count: number = 0;


  formatDateTime(date: Date): string {
    return (date != null) ? moment(date).format('DD.MM.YYYY HH:mm:ss') : '';
  }

  formatDate(date: Date): string {
    return (date != null) ? moment(date).format('DD.MM.YYYY') : '';
  }

  ngOnChanges(changes: SimpleChanges): void {

    if (changes['requests'] && changes['requests'].currentValue) {
      this.requests = changes['requests'].currentValue;
    }
  }

  rejectRequest(request: Request) {
    this.translateService.get('requests.shureRejectRequest', {name: request.generatedIdentifier}).subscribe(
      translated => {
        if (window.confirm(translated)) {
          this.requestsService.rejectRequest(request.id).subscribe( res => {
            this.toaster.showToast(this.toaster.types[1], this.translations.requests.requestRejected,
              '');
            this.requestsService.notifyUserActiveRequestsChanged();
          });
        }
      });



  }

  calculateNextExecutionDay(request: Request) {
    return this.formatDate(CalendarSelectionDataBuilder.getNextExecutionDay(request.calendarSelection, this.dateService));
  }


}
