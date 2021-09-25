import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';

import { TranslateService } from '@ngx-translate/core';
import { LocalDataSource } from 'ng2-smart-table';
import { Request, RequestLogItem, RequestParamType, RequestStatus } from '../../../@core/data/request-service-data';
import * as moment from 'moment';
import { CalendarSelectionDataBuilder } from '../../../@core/data/calendar-selection.data';
import { NbDateService, NbDialogService, NbToastrService } from '@nebular/theme';
import { RequestService } from '../../../@core/service/request-service';
import { Toaster } from '../../Toaster';
import { ResolveWindowComponent } from '../resolve-window/resolve-window.component';
import { NbAuthOAuth2JWTToken, NbAuthService } from '@nebular/auth';
import { NbAccessChecker } from '@nebular/security';
import { ResidentsService } from '../../../@core/service/residents-service';
import { Resident } from '../../../@core/data/resident-service-data';

@Component({
  selector: 'ngx-requests-list',
  templateUrl: './requests-list.component.html',
  styleUrls: ['./requests-list.component.scss'],
})
export class RequestsListComponent implements OnChanges {

  translations: any;
  ownerResident: Resident;
  RequestStatus = RequestStatus;
  RequestParamType = RequestParamType;
  logs: RequestLogItem[] = null;

  currentUser: string;
  @Input() requests: Request[];
  @Input() translateParamNames: boolean = false;
  @Input() translateTitle: boolean = false;

  @Output() requestRejected: EventEmitter<Request> = new EventEmitter();
  @Output() requestAccepted: EventEmitter<Request> = new EventEmitter();

  private toaster: Toaster;
  source: LocalDataSource = new LocalDataSource();

  constructor(private translateService: TranslateService,
    private dateService: NbDateService<Date>,
    private requestsService: RequestService,
    private toastrService: NbToastrService,
    private dialogService: NbDialogService,
    private authService: NbAuthService,
    private accessChecker: NbAccessChecker,
    private residentsService: ResidentsService) {
    this.toaster = new Toaster(toastrService);
    this.translations = translateService.translations[translateService.currentLang];

    this.authService.onTokenChange()
      .subscribe((token: NbAuthOAuth2JWTToken) => {
        if (token.isValid()) {
          this.currentUser = token.getAccessTokenPayload().sub;
        }
      });

  }

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

        this.dialogService.open(ResolveWindowComponent, {
          context: {
            title: translated,
            comment: this.translations.requests.rejectDefaultComment
          },
        })
        .onClose.subscribe(result => {
          if (result.ok) {
            this.requestsService.rejectRequest(request.id, result.comment)
              .subscribe( res => {
              this.toaster.showToast(this.toaster.types[1], this.translations.requests.requestRejected,
                '');
              this.requestsService.notifyMyActiveRequestsCountChanged();
              this.requestsService.notifyAssignedToMeActiveRequestsCountChanged();
              this.requestRejected.emit(res);
            });
          }
        });
      });
  }

  acceptRequest(request: Request) {
    this.requestsService.acceptRequest(request.id).subscribe(res => {
      this.toaster.showToast(this.toaster.types[1], this.translations.requests.requestAccepted,
        '');
      this.requestsService.notifyMyActiveRequestsCountChanged();
      this.requestsService.notifyAssignedToMeActiveRequestsCountChanged();
      this.requestAccepted.emit(res);
    });
  }

  calculateNextExecutionDay(request: Request) {
    return this.formatDate(CalendarSelectionDataBuilder.getNextExecutionDay(request.calendarSelection, this.dateService));
  }

  logsAreLoading: boolean = false;

  accordionCollapsedChanged(notExpanded: boolean, request: Request) {
    this.logs = null;
    this.ownerResident = null;
    if (!notExpanded) {
      // load owner as resident (not every user can do it)
      this.accessChecker.isGranted('read', 'management/residents').subscribe(granted => { // only for admins
        if (granted) {
          this.residentsService.getResidentByUserName(request.owner.userName).subscribe(res => {
            this.ownerResident = res;
          });
        }
      });
      this.logsAreLoading = true;
      this.requestsService.getRequestLogs(request.id).subscribe(gotLogs => {
        this.logs = gotLogs;
        this.logsAreLoading = false;
      });
    }
  }


}
