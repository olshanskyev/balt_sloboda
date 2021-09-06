/**
 * @license
 * Copyright Akveo. All Rights Reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */


import { Component, EventEmitter, Input, Output, Type } from '@angular/core';
import {
  NbBaseCalendarComponent,
  NbCalendarCell,
  NbCalendarViewMode,
  NbCalendarViewModeValues
} from '@nebular/theme';
import { MultiSelectCalendarDayCellComponent } from './multi-select-calendar-day-cell.component';
import { MultiSelectCalendarMonthCellComponent } from './multi-select-calendar-month-cell.component';
import { MultiSelectCalendarYearCellComponent } from './multi-select-calendar-year-cell.component';


  export class MultiSelectCalendarData<D> {
    array: Array<D>;
    manualSelection: boolean = true;
    constructor() {
      this.array = [];
    }
  }

 @Component({
   selector: 'ngx-multi-select-calendar',
   template: `
   <nb-base-calendar
   [boundingMonth]="boundingMonth"
   [startView]="startView"
   [date] = "multiSelectCalendarData"
   [min]="min"
   [max]="max"
   [filter]="filter"
   (dateChange)="onChange($any($event))"
   [dayCellComponent]="dayCellComponent"
   [monthCellComponent]="monthCellComponent"
   [yearCellComponent]="yearCellComponent"
   [size]="size"
   [visibleDate]="visibleDate"
   [showNavigation]="showNavigation"
   [showWeekNumber]="showWeekNumber"
   [weekNumberSymbol]="weekNumberSymbol"
  ></nb-base-calendar>
   `,
 })
 export class MultiSelectCalendarComponent<D> extends NbBaseCalendarComponent<D, MultiSelectCalendarData<D>> {

  /**
   * Defines starting view for the calendar.
  * */
  @Input() startView: NbCalendarViewMode = NbCalendarViewMode.DATE;
  static ngAcceptInputType_startView: NbCalendarViewModeValues;

  @Input("manualSelection")// manualSelection: boolean = true;
  set _manualSelection(selection: boolean){
    this.multiSelectCalendarData.manualSelection = selection;
  }

  preselectedDays: Array<D>;

  @Input("days") // selected days
  set _days(daysArray: Array<D>) {
    this.multiSelectCalendarData.array = daysArray;
  }

  multiSelectCalendarData: MultiSelectCalendarData<D> = new MultiSelectCalendarData();

  @Input('dayCellComponent')
  set _cellComponent(cellComponent: Type<NbCalendarCell<D, MultiSelectCalendarData<D>>>) {
    if (cellComponent) {
      this.dayCellComponent = cellComponent;
    }
  }
  dayCellComponent: Type<NbCalendarCell<D, MultiSelectCalendarData<D>>> = MultiSelectCalendarDayCellComponent;


  /**
   * Custom month cell component. Have to implement `NbCalendarCell` interface.
   * */
   @Input('monthCellComponent')
   set _monthCellComponent(cellComponent: Type<NbCalendarCell<D, MultiSelectCalendarData<D>>>) {
     if (cellComponent) {
       this.monthCellComponent = cellComponent;
     }
   }
   monthCellComponent: Type<NbCalendarCell<D, MultiSelectCalendarData<D>>> = MultiSelectCalendarMonthCellComponent;


   /**
   * Custom year cell component. Have to implement `NbCalendarCell` interface.
   * */
    @Input('yearCellComponent')
    set _yearCellComponent(cellComponent: Type<NbCalendarCell<D, MultiSelectCalendarData<D>>>) {
      if (cellComponent) {
        this.yearCellComponent = cellComponent;
      }
    }
    yearCellComponent: Type<NbCalendarCell<D, MultiSelectCalendarData<D>>> = MultiSelectCalendarYearCellComponent;


  /**
   * Emits range when start selected and emits again when end selected.
   * */
  @Output() arrayChange: EventEmitter<Array<D>> = new EventEmitter();

  onChange(date: D) {
    this.handleSelected(date);
  }

  private handleSelected(date: D) {

    const foundIndex = this.multiSelectCalendarData.array.findIndex(item => {

        return this.dateService.compareDates(item, date) === 0;
    });

    if (foundIndex > -1) {
      this.multiSelectCalendarData.array.splice(foundIndex, 1);
    } else {
      this.multiSelectCalendarData.array.push(date);
    }

    this.arrayChange.emit(this.multiSelectCalendarData.array);
  }

  updateView(){
    //ToDo to update cell view how?
    this.prevMonth();
    this.nextMonth();
  }

 }