import { WeekDay } from '@angular/common';
import { Component, OnDestroy, ViewChild} from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import {
  NbDateService,
  NbToastrService,
} from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { CalendarSelectionDataBuilder, SelectionMode } from '../../../@core/data/calendar-selection.data';
import { Request, RequestParam, RequestParamType, RequestType } from '../../../@core/data/request-service-data';
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
  myActiveRequestsSubscription: Subscription = null;
  assignedToMeActiveRequestsSubscription: Subscription = null;
  myRequests: Request[] = [];
  myActiveRequests: Request[] = [];
  assignedToMeRequests: Request[] = [];
  assignedToMeActiveRequests: Request[] = [];
  showOnlyMyActiveRequests: boolean = true;
  showOnlyAssignedToMeActiveRequests: boolean = true;

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
        this.myActiveRequestsSubscription = this.requestService.getMyActiveRequestsSubscription()
          .subscribe(requests => {
            if (requests) { // null at first time
              this.myActiveRequests = requests.content.filter(item => item.type.name === this.requestTypeName);
              this.refreshMyRequests();

            }
        });
        this.assignedToMeActiveRequestsSubscription = this.requestService.getAssignedToMeActiveRequestsSubscription()
          .subscribe(requests => {
            if (requests) {
              this.assignedToMeActiveRequests = requests.content.filter(item => item.type.name === this.requestTypeName);
              this.refreshAssignedToMeRequests();
            }
        });

      });
    });
  }

  ngOnDestroy(): void {
    if (this.myActiveRequestsSubscription) {
      this.myActiveRequestsSubscription.unsubscribe();
    }
    if (this.assignedToMeActiveRequestsSubscription) {
      this.assignedToMeActiveRequestsSubscription.unsubscribe();
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
      this.requestService.notifyMyActiveRequestsChanged();
      this.requestService.notifyAssignedToMeActiveRequestsChanged();

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

  refreshMyRequests() {
    if (!this.showOnlyMyActiveRequests) {
      this.requestService.getMyRequests().subscribe(res => {
        this.myRequests = res.content.filter(item => item.type.name === this.requestTypeName);
      });
    } else {
      this.myRequests = [];
    }

  }

  refreshAssignedToMeRequests() {
    if (!this.showOnlyAssignedToMeActiveRequests) {
      this.requestService.getAssignedToMeRequests().subscribe(res => {
        this.assignedToMeRequests = res.content.filter(item => item.type.name === this.requestTypeName);
      });
    } else {
      this.assignedToMeRequests = [];
    }
  }


}
