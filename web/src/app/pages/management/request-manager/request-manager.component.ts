import { KeyValue, WeekDay } from '@angular/common';
import { Component, ViewChild} from '@angular/core';

import {
  NbDateService,
  NbToastrService,
} from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { EveryDays, MonthDays, SelectionMode, WeekDays } from '../../../@core/data/calendar-selection.data';
import { RequestParam, RequestType } from '../../../@core/data/request-service-data';
import { CalendarSelectionService } from '../../../@core/service/calendar-selection.service';
import { MultiSelectCalendarComponent } from '../../custom-components/multi-select-calendar/multi-select-calendar.component';

import { Toaster } from '../../Toaster';


@Component({
  selector: 'ngx-request-manager',
  templateUrl: './request-manager.component.html',
  styleUrls: ['./request-manager.component.scss'],
})
export class RequestManagerComponent{

  private toaster: Toaster;
  newRequestType: RequestType = new RequestType();

  EveryDays = EveryDays;
  everyDays = EveryDays.first;

  SelectionMode = SelectionMode;
  selectionMode = SelectionMode.Manually;

  @ViewChild('calendar', { static: false }) calendar: MultiSelectCalendarComponent<Date>;

  minDate: Date = new Date();
  maxDate: Date = new Date();
  translations: any;
  selectedDays: Array<Date> = [];
  selectedEveryDay: string = 'first';
  selectedEveryWeekDay:string = 'Monday';


  constructor(private toastrService: NbToastrService, translateService: TranslateService,
    protected dateService: NbDateService<Date>,
    private calendarSelectionService: CalendarSelectionService) {
    this.toaster = new Toaster(toastrService);
    this.translations = translateService.translations[translateService.currentLang];
    this.newRequestType.durable = false;
    this.maxDate.setMonth(this.maxDate.getMonth() + 12);
    this.minDate.setDate(this.minDate.getDate() -1);
  }

  onChangeArray(event) {
    this.selectedDays = event;
  }

  originalOrder = (a: KeyValue<number,string>, b: KeyValue<number,string>): number => {
    return 0;
  }

  weekDaySelected: boolean = false;
  toggleWeekDay(checked: boolean, weekDay: WeekDay){

    var weekDays: WeekDays = this.calendarSelectionService.toggleDayOfWeek(weekDay, checked);
    this.weekDaySelected = false;
    weekDays.forEach((value, key) => {
      if (value) {
        this.weekDaySelected = true;
        return;
      }
    });
    this.calendar.updateView();
  }

  convertedDayOfMonth: Array<{dayOfWeek: string, everyDay: string}> = [];

  convertSelectedMonthDays(input: MonthDays) {
    this.convertedDayOfMonth = [];
    input.forEach((arrayEveryDay, dayOfWeek) => {
      arrayEveryDay.forEach(everyDay => {
        this.convertedDayOfMonth.push(
            {
              dayOfWeek: dayOfWeek,
              everyDay: EveryDays[everyDay]
            }
          );
      });
    });

  }

  addEvery() {
    this.convertSelectedMonthDays(this.calendarSelectionService.toggleDayOfMonth(EveryDays[this.selectedEveryDay], WeekDay[this.selectedEveryWeekDay], true));
    this.calendar.updateView();
  }

  removeEvery(dayOfWeek: string, everyDay: string) {
    this.convertSelectedMonthDays(this.calendarSelectionService.toggleDayOfMonth(EveryDays[everyDay], WeekDay[dayOfWeek], false));
    this.calendar.updateView();
  }


  newParameter: RequestParam = new RequestParam();
}
