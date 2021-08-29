import { KeyValue, WeekDay } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild} from '@angular/core';

import {
  NbToastrService,
} from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { RequestType } from '../../../@core/data/request-service-data';
import { MultiSelectCalendarComponent } from '../../custom-components/multi-select-calendar/multi-select-calendar.component';

import { Toaster } from '../../Toaster';

export  enum PERIODICITY {
  Manually = 'Manually',
  Weekly = 'Weekly',
  Monthly = 'Monthly',
}

@Component({
  selector: 'ngx-request-manager',
  templateUrl: './request-manager.component.html',
  styleUrls: ['./request-manager.component.scss'],
})
export class RequestManagerComponent{

  private toaster: Toaster;
  newRequestType: RequestType = new RequestType();

  PERIODICITY = PERIODICITY;
  weekDay = WeekDay;

  periodicity = PERIODICITY.Manually;

  @ViewChild('calendar', { static: false }) calendar: MultiSelectCalendarComponent<Date>;

  minDate: Date = new Date();
  maxDate: Date = new Date();
  translations: any;
  array: Array<Date> = [];
  constructor(private toastrService: NbToastrService, translateService: TranslateService) {
    this.toaster = new Toaster(toastrService);
    this.translations = translateService.translations[translateService.currentLang];
    this.newRequestType.durable = false;
    this.maxDate.setMonth(this.maxDate.getMonth() + 12);
    this.minDate.setDate(this.minDate.getDate() -1);

  }

  onChangeArray(event) {
    console.log(event);
  }


  checkedWeekDays: Map<string, boolean> = new Map([
  ]);

  keys() : Array<string> {
    var keys = Object.keys(this.weekDay);
    return keys.slice(keys.length / 2);
  }

  toggleWeekDay(checked: boolean, weekDay: string){
    this.checkedWeekDays.set(weekDay, checked);
    this.calendar.reloadView();
  }

  filter = (date: Date) => {
      return (this.checkedWeekDays.get(WeekDay[date.getDay()]));
  }

}
