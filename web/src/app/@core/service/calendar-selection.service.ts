
import { WeekDay } from '@angular/common';
import { Injectable } from '@angular/core';
import { NbDateService } from '@nebular/theme';
import { EveryDays, MonthDays, WeekDays } from '../data/calendar-selection.data';


@Injectable()
export class CalendarSelectionService {

  private weekDays: WeekDays = new Map([
    ['Monday', false],
    ['Tuesday', false],
    ['Wednesday', false],
    ['Thursday', false],
    ['Friday', false],
    ['Saturday', false],
    ['Sunday', false],
  ]);

  private monthDays: MonthDays = new Map([]);

  public getEveryDaysList(): Array<string> {
    return ['first', 'second', 'third', 'fourth', 'last'];
  }

  public toggleDayOfWeek(weekDay: WeekDay, checked: boolean): WeekDays{
    this.weekDays.set('' + weekDay, checked);
    return this.weekDays;
  }

  public getCheckedWeekDays(): WeekDays {
    return this.weekDays;
  }


  public toggleDayOfMonth(everyDay: EveryDays, weekDay: WeekDay, checked: boolean): MonthDays {

    var found: Array<number> = this.monthDays.get(WeekDay[weekDay]);
    if (checked) { //set
      if (!found) {
        found = new Array<number>();
      }
      if (!found.includes(everyDay)){
        found.push(everyDay);
        this.monthDays.set(WeekDay[weekDay], found);
      }
    } else { // ToDo not tested
      // checked == false
      if (found) {
        var foundIndex: number = found.findIndex(item => item === everyDay);
        if (foundIndex != -1)
          found.splice(foundIndex, 1);
      }
    }

    return this.monthDays;
  }


  public weekFilter = (date: Date) => {
    return (this.weekDays.get(WeekDay[date.getDay()]));
  }

  public monthFilter = (date: Date) => {

    var everies: Array<number> = this.monthDays.get(WeekDay[date.getDay()]);
    var found: boolean = false;
    if (everies) {
      for (var i = 0; i < everies.length && !found; i++) {
        var every: number = everies[i];
        var numberInMonth: number;
        var rem: number = date.getDate() % 7;
        numberInMonth = (rem === 0)? date.getDate() / 7: ((date.getDate() - rem) / 7) + 1

        if (every === -1) {
          if (numberInMonth === 5) { // 5th day is always last
            found = true;
          }
          if (numberInMonth === 4) {
            found =  ((date.getDate() + 7) > this.dateService.getNumberOfDaysInMonth(date)); // last 4th day in month
          }
        } else {
          found = (every === numberInMonth);
        }
      }
    }

    return found;

  }

  constructor(private dateService: NbDateService<Date>){
  }




}
