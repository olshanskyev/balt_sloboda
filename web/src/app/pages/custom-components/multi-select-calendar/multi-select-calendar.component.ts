/**
 * @license
 * Copyright Akveo. All Rights Reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

import { supportsPassiveEventListeners } from '@angular/cdk/platform';
import { WeekDay } from '@angular/common';
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


 @Component({
   selector: 'ngx-multi-select-calendar',
   template: `
   <nb-base-calendar
   [boundingMonth]="boundingMonth"
   [startView]="startView"
   [date] = "array"
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
 export class MultiSelectCalendarComponent<D> extends NbBaseCalendarComponent<D, Array<D>> {


  /**
   * Defines starting view for the calendar.
  * */
  @Input() startView: NbCalendarViewMode = NbCalendarViewMode.DATE;
  static ngAcceptInputType_startView: NbCalendarViewModeValues;

  array: Array<D> = [];


  @Input('dayCellComponent')
  set _cellComponent(cellComponent: Type<NbCalendarCell<D, Array<D>>>) {
    if (cellComponent) {
      this.dayCellComponent = cellComponent;
    }
  }
  dayCellComponent: Type<NbCalendarCell<D, Array<D>>> = MultiSelectCalendarDayCellComponent;


  /**
   * Custom month cell component. Have to implement `NbCalendarCell` interface.
   * */
   @Input('monthCellComponent')
   set _monthCellComponent(cellComponent: Type<NbCalendarCell<D, Array<D>>>) {
     if (cellComponent) {
       this.monthCellComponent = cellComponent;
     }
   }
   @Input() monthCellComponent: Type<NbCalendarCell<D, Array<D>>> = MultiSelectCalendarMonthCellComponent;


   /**
   * Custom year cell component. Have to implement `NbCalendarCell` interface.
   * */
    @Input('yearCellComponent')
    set _yearCellComponent(cellComponent: Type<NbCalendarCell<D, Array<D>>>) {
      if (cellComponent) {
        this.yearCellComponent = cellComponent;
      }
    }
    yearCellComponent: Type<NbCalendarCell<D, Array<D>>> = MultiSelectCalendarYearCellComponent;



  /**
   * Emits range when start selected and emits again when end selected.
   * */
  @Output() arrayChange: EventEmitter<Array<D>> = new EventEmitter();

  onChange(date: D) {
    this.handleSelected(date);
  }

  private handleSelected(date: D) {

    const foundIndex = this.array.findIndex(item => {

        return this.dateService.compareDates(item, date) === 0;
    });

    if (foundIndex > -1) {
      this.array.splice(foundIndex, 1);
    } else {
      this.array.push(date);
    }

    this.arrayChange.emit(this.array);
  }


  reloadView(){
    //ToDo to update cell view how?
    this.prevMonth();
    this.nextMonth();
  }
  /*public toggleWeekDay(checked: boolean, weekDay: string) {


    // remove from array
    for (let i = this.array.length - 1; i >= 0; i--) {
      if (this.dateService.getDayOfWeek(this.array[i]) === WeekDay[weekDay]) {
        this.array.splice(i, 1);
      }
    }

    if (checked) { // add new elements into array
      var item: D = this.dateService.addDay(this.min, 1);
      var step: number = 1;
      while(this.dateService.compareDates(item, this.max) < 1) {
        if (this.dateService.getDayOfWeek(item) === WeekDay[weekDay]) { // find first f.e. Monday
          this.array.push(item);
          step = 7;
        }
        item = this.dateService.addDay(item, step);
      }
    }


    //ToDo to update cell view how?
    this.prevMonth();
    this.nextMonth();

    this.arrayChange.emit(this.array);

  }*/





 }