import { KeyValue, WeekDay } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, ViewChild} from '@angular/core';

import {
  NbDateService,
  NbDialogService,
  NbToastrService,
} from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { CalendarSelectionDataBuilder, EveryDays, MonthDays, SelectionMode, WeekDays } from '../../../@core/data/calendar-selection.data';
import { RequestParam, RequestParamType, RequestType } from '../../../@core/data/request-service-data';
import { User } from '../../../@core/data/user-service-data';
import { CalendarSelectionService } from '../../../@core/service/calendar-selection.service';
import { UserService } from '../../../@core/service/users-service';
import { MultiSelectCalendarComponent } from '../multi-select-calendar/multi-select-calendar.component';

import { Toaster } from '../../Toaster';
import { IconPickerWindowComponent } from '../icon-picker/icon-picker-window.component';


export interface UserGroup {
  name: string;
  users: User[];
}

@Component({
  selector: 'ngx-request-type-stepper',
  templateUrl: './request-type-stepper.component.html',
  styleUrls: ['./request-type-stepper.component.scss'],
})
export class RequestTypeStepperComponent implements OnChanges {


  @Output() requestTypeCreated: EventEmitter<RequestType> = new EventEmitter();

  @Output() requestTypeUpdated: EventEmitter<RequestType> = new EventEmitter();

  @Input() basedOn: RequestType;

  @Input() mode: string = 'create';

  private toaster: Toaster;
  @ViewChild('calendar', { static: false }) calendar: MultiSelectCalendarComponent<Date>;

  EveryDays = EveryDays;
  everyDays = EveryDays.first;

  SelectionMode = SelectionMode;
  selectionMode = SelectionMode.Manually;

  minDate: Date = new Date();
  maxDate: Date = new Date();
  translations: any;
  selectedDays: Array<Date> = [];
  selectedEveryDay: string = 'first';
  selectedEveryWeekDay:string = 'Monday';
  weekDaySelected: boolean = false;
  selectedAssignToId: number = null;

  convertedDayOfMonth: Array<{dayOfWeek: string, everyDay: string}> = [];

  adminsGroup: UserGroup = {
    name: 'Admin',
    users: [],
  }
  othersGroup: UserGroup = {
    name: 'Others',
    users: [],
  };

  newRequestType: RequestType = new RequestType();
  userGroups: UserGroup[] = [];
  newParameter: RequestParam;
  RequestParamType = RequestParamType;
  newParameterEnumValues: string[] = [];

  selectedIcon: string;
  showInMenu: boolean = true;

  initNewParam() {
    this.newParameter = new RequestParam();
    this.newParameter.type = RequestParamType.STRING;
    this.newParameter.optional = false;
    this.newParameterEnumValues = [];
  }

  init() {
    this.newRequestType = new RequestType();
    this.newRequestType.parameters = [];
    this.newRequestType.durable = false;
    this.selectedDays = [];
    this.convertedDayOfMonth = [];
    this.newParameterEnumValues = [];
    this.initNewParam();
    this.selectedIcon = 'plus-outline';
    this.calendarSelectionService.resetWeeDays();
    this.calendarSelectionService.resetMonthDays();
  }

  initBasedOn() {
    this.newRequestType = this.basedOn;

    if (this.basedOn.durable) {
      this.selectionMode = this.basedOn.calendarSelection.selectionMode;
      switch (this.selectionMode) {
        case SelectionMode.Manually: {
          this.selectedDays= CalendarSelectionDataBuilder.convertDaysFromStringArray(this.basedOn.calendarSelection.selectedDays, this.dateService);
          break;
        }
        case SelectionMode.Weekly: {
          Object.entries(this.basedOn.calendarSelection.weekDays).forEach(item => {
            // item[0] = day
            // item[1] = checked
            this.calendarSelectionService.toggleDayOfWeek(WeekDay[item[0]], item[1])
            if (item[1]) {
              this.weekDaySelected = true;
            }
          });
          break;
        }
        case SelectionMode.Monthly: { // ToDo
          Object.entries(this.basedOn.calendarSelection.monthDays).forEach(item => {

            // item[0] = day
            // item[1] Array<EveryDays>
            item[1].forEach(day => {
              this.convertSelectedMonthDays(this.calendarSelectionService.toggleDayOfMonth(day, WeekDay[item[0]], true));
            });

          });
          break;
        }
      }
    }
    this.selectedAssignToId = this.newRequestType.assignTo.id;
    this.selectedIcon = this.newRequestType.displayOptions['icon'];
    this.showInMenu = Boolean(JSON.parse(this.newRequestType.displayOptions['showInMainRequestMenu']));
  }

  constructor(private toastrService: NbToastrService, translateService: TranslateService,
    protected dateService: NbDateService<Date>,
    private calendarSelectionService: CalendarSelectionService,
    private userService: UserService,
    private dialogService: NbDialogService,
    ) {
    this.toaster = new Toaster(toastrService);
    this.translations = translateService.translations[translateService.currentLang];
    this.newRequestType.durable = false;
    this.maxDate.setMonth(this.maxDate.getMonth() + 12);
    this.minDate.setDate(this.minDate.getDate() -1);

    this.loadUsers();
    this.init();
  }

  onChangeArray(event) {
    this.selectedDays = event;
  }

  durableRadioChanged(event) {
    // reset selectedDays
    this.selectedDays = [];
  }

  originalOrder = (a: KeyValue<number,string>, b: KeyValue<number,string>): number => {
    return 0;
  }

  toggleWeekDay(checked: boolean, weekDay: WeekDay) {
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

  loadUsers() {
    this.userService.getAllUsers().subscribe(res => {
      res.forEach(item => {
        if (item.roles.includes('ROLE_ADMIN')) {
          this.adminsGroup.users.push(item);
          if (!this.selectedAssignToId) {
            //this.newRequestType.assignTo = item;
            this.selectedAssignToId = item.id
          }
        } else {
          this.othersGroup.users.push(item)
        }
      });
      this.userGroups.push(this.adminsGroup);
      this.userGroups.push(this.othersGroup);
    });
  }

  addValueToEnum(item: string) {
    if (!this.newParameterEnumValues.includes(item) && item.length > 0)
      this.newParameterEnumValues.push(item);
  }

  removeFromNewParameterEnumValues(item: string) {
    this.newParameterEnumValues.forEach((element,index)=>{
      if(element === item) this.newParameterEnumValues.splice(index,1);
    });
  }


  addNewParam() {
    if (this.newParameter.type === RequestParamType.ENUM) {
      this.newParameter.enumValues = this.newParameterEnumValues;
    }
    if (!this.newRequestType.parameters.find(item => item.name === this.newParameter.name)) {
      this.newParameter.enumValues = this.newParameterEnumValues;
      this.newRequestType.parameters.push(this.newParameter);
      this.initNewParam();
    }
  }

  removeFromNewParameters(item: RequestParam) {
    this.newRequestType.parameters.forEach((element,index)=>{
      if (element.name === item.name) this.newRequestType.parameters.splice(index,1);
    });
  }


  chooseIcon() {
    this.dialogService.open(IconPickerWindowComponent, {
      context: {
        selectedIcon: this.selectedIcon,
      },
    })
    .onClose.subscribe(result => {
      this.selectedIcon = result;
    });
  }


  createRequestType() {
    if (this.newRequestType.durable) {
      switch(this.selectionMode) {
        case SelectionMode.Manually: {
          this.newRequestType.calendarSelection = CalendarSelectionDataBuilder.createManualSelection(this.selectedDays, this.dateService);
          break;
        }
        case SelectionMode.Monthly: {
          this.newRequestType.calendarSelection = CalendarSelectionDataBuilder.createMonthSelection(this.calendarSelectionService.getMonthDays());
          break;
        }
        case SelectionMode.Weekly: {
          this.newRequestType.calendarSelection = CalendarSelectionDataBuilder.createWeekSelection(this.calendarSelectionService.getWeekDays());
          break;
        }
      }
    } else {
      if (this.newRequestType.calendarSelection) {
        this.newRequestType.calendarSelection = null;
      }
    }

    this.newRequestType.displayOptions = {
      icon: this.selectedIcon,
      showInMainRequestMenu: this.showInMenu,
    };

    var assigneToUser: User = new User();
    assigneToUser.id = this.selectedAssignToId;
    this.newRequestType.assignTo = assigneToUser;

    if (this.mode === 'create') {
      this.requestTypeCreated.emit(this.newRequestType);
      this.init();
    } else {
      this.requestTypeUpdated.emit(this.newRequestType);
    }


  }


  ngOnChanges(changes: SimpleChanges): void {

    if (changes['basedOn'] && changes['basedOn'].currentValue) {
      this.basedOn = changes['basedOn'].currentValue;
      this.initBasedOn();
    }

    if (changes['mode'] && changes['mode'].currentValue &&
    (changes['mode'].currentValue === 'create' || changes['mode'].currentValue === 'update')) {
      this.mode =  changes['mode'].currentValue;
    }

  }



}
