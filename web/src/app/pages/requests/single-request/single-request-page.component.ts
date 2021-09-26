import { WeekDay } from '@angular/common';
import { isNgTemplate } from '@angular/compiler';
import { Component, OnDestroy, ViewChild} from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import {
  NbDateService,
  NbToastrService,
} from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { Observable, Subscription } from 'rxjs';
import { CalendarSelectionDataBuilder, SelectionMode } from '../../../@core/data/calendar-selection.data';
import { Page } from '../../../@core/data/page';
import { Request, RequestParam, RequestParamType, RequestStatus, RequestType } from '../../../@core/data/request-service-data';
import { CalendarSelectionService } from '../../../@core/service/calendar-selection.service';
import { RequestService } from '../../../@core/service/request-service';
import { JsonUtils } from '../../../@core/utils/json.utils';
import { MultiSelectCalendarComponent } from '../../custom-components/multi-select-calendar/multi-select-calendar.component';

import { Toaster } from '../../Toaster';


@Component({
  selector: 'ngx-single-request',
  templateUrl: './single-request-page.component.html',
  styleUrls: ['./single-request-page.component.scss'],
})


export class SingleRequestPageComponent implements OnDestroy {

  private toaster: Toaster;
  gotRequestType: RequestType;
  requestTypeName: string;
  SelectionMode = SelectionMode;
  RequestParamType = RequestParamType;
  newRequest: Request;
  availableDays: Array<Date> = [];
  selectedDays: Array<Date> = [];
  translations: any;
  setParamValues: Map<string, string> = new Map();
  minDate: Date = new Date();
  maxDate: Date = new Date();
  comment: string;
  myActiveRequestsCountSubscription: Subscription = null;
  assignedToMeActiveRequestsCountSubscription: Subscription = null;
  myRequests: Request[] = [];
  myRequestsCount: number = 0;
  assignedToMeRequests: Request[] = [];
  assignedToMeRequestsCount: number = 0;
  showOnlyMyActiveRequests: boolean = true;
  showOnlyAssignedToMeActiveRequests: boolean = true;


  currentAssignedToMePage: number = 0;
  currentMyRequestsPage: number = 0;
  pageSize: number = 6;
  totalAssignedToMePages: number;
  totalMyRequestsPages: number;
  totalAssignedToMeElements: number;
  totalMyElements: number;

  @ViewChild('calendar', { static: false }) calendar: MultiSelectCalendarComponent<Date>;


  setInitialValues() {
    this.comment = '';
    this.selectedDays = [];
    this.gotRequestType.parameters.forEach(param => this.setParamValues.set(param.name, param.defaultValue));
    // calendar initialization
    if (this.gotRequestType.durable) {
      this.maxDate.setMonth(this.maxDate.getMonth() + 12);
      this.minDate.setDate(this.minDate.getDate() -1);
      switch (this.gotRequestType.calendarSelection.selectionMode) {
        case SelectionMode.Manually: {
          this.availableDays = CalendarSelectionDataBuilder.convertDaysFromStringArray(this.gotRequestType.calendarSelection.selectedDays, this.dateService);
          break;
        }
        case SelectionMode.Weekly: {
          Object.entries(this.gotRequestType.calendarSelection.weekDays).forEach(item => {
            // item[0] = day
            // item[1] = checked
            this.calendarSelectionService.toggleDayOfWeek(WeekDay[item[0]], item[1]);
          });
          break;
        }
        case SelectionMode.Monthly: {
          Object.entries(this.gotRequestType.calendarSelection.monthDays).forEach(item => {
            // item[0] = day
            // item[1] Array<EveryDays>
            item[1].forEach(day => {
              this.calendarSelectionService.toggleDayOfMonth(day, WeekDay[item[0]], true);
            });

          });
          break;
        }
      }
      if (this.calendar)
        this.calendar.updateView();
    }
  }


  constructor(private toastrService: NbToastrService, translateService: TranslateService,
    private route: ActivatedRoute,
    private requestService: RequestService,
    private calendarSelectionService: CalendarSelectionService,
    protected dateService: NbDateService<Date>) {

    this.toaster = new Toaster(toastrService);
    this.translations = translateService.translations[translateService.currentLang];

    this.route.params.subscribe((params: Params) => { //called once for every request type
      this.setParamValues = new Map();
      this.requestTypeName = params['requestTypeName'];
      this.requestService.getRequestTypeByName(this.requestTypeName).subscribe( res => {
        this.gotRequestType = res;
        // setting default values
        this.setInitialValues();
        this.myActiveRequestsCountSubscription = this.requestService.getMyActiveRequestsCountSubscription()
          .subscribe(requestsCount => {
            if (requestsCount) { // null at first time
              this.myRequestsCount = 0;
              let rc = requestsCount.find(item => item.requestTypeName === this.requestTypeName);
              if (rc) {
                this.myRequestsCount = rc.count;
              }
              this.loadMyRequests();
            }
        });
        this.assignedToMeActiveRequestsCountSubscription = this.requestService.getAssignedToMeActiveRequestsCountSubscription()
          .subscribe(requestsCount => {
            if (requestsCount) { // null at first time
              this.assignedToMeRequestsCount = 0;
              let rc = requestsCount.find(item => item.requestTypeName === this.requestTypeName);
              if (rc) {
                this.assignedToMeRequestsCount = rc.count;
              }

              this.loadAssignedToMeRequests();
            }
        });

      });
    });
  }

  ngOnDestroy(): void {
    if (this.myActiveRequestsCountSubscription) {
      this.myActiveRequestsCountSubscription.unsubscribe();
    }
    if (this.assignedToMeActiveRequestsCountSubscription) {
      this.assignedToMeActiveRequestsCountSubscription.unsubscribe();
    }
  }


  public manualSelectionFilter = (date: Date) => {
    return (this.availableDays.findIndex(item => this.dateService.compareDates(item, date) === 0) !== -1)
  }

  paramValueChanged(paramName: string, value: string) {
    this.setParamValues.set(paramName, value);
  }

  createRequest() {
    var newRequest: Request = {
      comment: this.comment,
      paramValues: JsonUtils.convertMapToJsonObject<string>(this.setParamValues),
      type: this.gotRequestType,
      calendarSelection: (this.gotRequestType.durable)? CalendarSelectionDataBuilder.createManualSelection(this.selectedDays, this.dateService): null,

    }

    this.requestService.createRequest(newRequest).subscribe( res => {
      this.toaster.showToast(this.toaster.types[1], this.translations.singleRequestPage.requestCreated,'');
      this.requestService.notifyMyActiveRequestsCountChanged();
      this.requestService.notifyAssignedToMeActiveRequestsCountChanged();

    });
    this.setInitialValues();
  }

  areParamsValid(): boolean {
    var valid: boolean = true;
    this.gotRequestType.parameters.forEach(param => {
      if (this.isParamInvalid(param)){
        valid = false;
        return;
      }
    });
    return valid;
  }

  isParamInvalid(param: RequestParam): boolean {
    var value: string = this.setParamValues.get(param.name);
    return (!param.optional && (!value || value.length === 0));
  }

  onChangeSelectedDays(event) {
    this.selectedDays = event;
  }


  loadMyRequests() {
    let requestsSub: Observable<Page<Request>>;
    if (this.showOnlyMyActiveRequests) {
      requestsSub = this.requestService.getMyRequests(this.currentMyRequestsPage, this.pageSize,
        [RequestStatus.NEW, RequestStatus.ACCEPTED, RequestStatus.IN_PROGRESS],
        this.requestTypeName);
    } else {
      requestsSub = this.requestService.getMyRequests(this.currentMyRequestsPage, this.pageSize,
        null, this.requestTypeName);
    }
    requestsSub.subscribe(res => {
      this.myRequests = res.content;
      this.totalMyRequestsPages = res.totalPages;
      this.totalMyElements = res.totalElements;
      if (this.currentMyRequestsPage >= this.totalMyRequestsPages && this.totalMyRequestsPages > 0) {
        // if current page number after checkbox event > than total page number
        this.currentMyRequestsPage = this.totalMyRequestsPages - 1;
        this.loadMyRequests();
      }
    });
  }

  loadAssignedToMeRequests() {
    let requestsSub: Observable<Page<Request>>;
    if (this.showOnlyAssignedToMeActiveRequests) {
      requestsSub = this.requestService.getAssignedToMeRequests(this.currentAssignedToMePage, this.pageSize,
        [RequestStatus.NEW, RequestStatus.ACCEPTED, RequestStatus.IN_PROGRESS],
        this.requestTypeName);
    } else {
      requestsSub = this.requestService.getAssignedToMeRequests(this.currentAssignedToMePage, this.pageSize,
        null, this.requestTypeName);
    }
    requestsSub.subscribe(res => {
      this.assignedToMeRequests = res.content;
      this.totalAssignedToMePages = res.totalPages;
      this.totalAssignedToMeElements = res.totalElements;
      if (this.currentAssignedToMePage >= this.totalAssignedToMePages && this.totalAssignedToMePages > 0) {
        // if current page number after checkbox event > than total page number
        this.currentAssignedToMePage = this.totalAssignedToMePages - 1;
        this.loadAssignedToMeRequests();
      }
    });
  }

  assignedToMeRequestsPageChanged(pageNumber: number) {
    this.currentAssignedToMePage = pageNumber;
    this.loadAssignedToMeRequests();
  }

  myRequestsPageChanged(pageNumber: number) {
    this.currentMyRequestsPage = pageNumber;
    this.loadMyRequests();
  }


}
