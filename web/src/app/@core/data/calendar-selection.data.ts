import { NbDateService, NbTreeGridDataService } from "@nebular/theme";
import { JsonUtils } from "../utils/json.utils";


export enum SelectionMode {
  Manually = 'Manually',
  Weekly = 'Weekly',
  Monthly = 'Monthly',
}

export enum EveryDays {
  first = 1,
  second = 2,
  third = 3,
  fourth= 4,
  last = 5,
}

export class CalendarSelectionData {
  id?: number;
  selectionMode: SelectionMode;
  monthDays?: MonthDays;
  weekDays?: WeekDays;
  selectedDays?: Array<string>;
}
/* format
  [
    ['Monday', [1, 5]] // first and last monday
    ['Sunday', [2]] // second sunday
  ]
  */
export type MonthDays = Map<string, Array<EveryDays>>;
export type WeekDays = Map<string, boolean>;

export class CalendarSelectionDataBuilder {


  public static convertDateToString(date: Date, dateService: NbDateService<Date>): string {
    return dateService.getYear(date) + '-' + (dateService.getMonth(date) + 1) + '-' + dateService.getDate(date);
  }


  public static createManualSelection(selectedDays: Array<Date>, dateService: NbDateService<Date>): CalendarSelectionData {
    var dates: Array<string> = []
    selectedDays.forEach(item => {
      dates.push(CalendarSelectionDataBuilder.convertDateToString(item, dateService));
    });
    return {
      selectionMode: SelectionMode.Manually,
      selectedDays: dates,
    }
  }



  public static createMonthSelection(monthDays: MonthDays): CalendarSelectionData {
    return {
      selectionMode: SelectionMode.Monthly,
      monthDays: JsonUtils.convertMapToJsonObject<Array<EveryDays>>(monthDays),
    }
  }

  public static createWeekSelection(weekDays: WeekDays): CalendarSelectionData {
    return {
      selectionMode: SelectionMode.Weekly,
      weekDays: JsonUtils.convertMapToJsonObject<boolean>(weekDays),
    }
  }

  public static convertDaysFromStringArray(selectedDays: Array<string>, dateService: NbDateService<Date>): Array<Date> {
    var toReturn: Array<Date> = []
    selectedDays.forEach(item => {
      var date: Date = dateService.parse(item, 'YYYY-MM-DD');
      date.setHours(0); // to escape + hours because of timezone
      toReturn.push(date);
    });
    return toReturn;
  }

  public static getNextExecutionDay(calendarSelection: CalendarSelectionData,  dateService: NbDateService<Date>): Date {
    if (calendarSelection.selectionMode === SelectionMode.Manually) {
      // ToDo at the moment it is alway manuall selection in Request Type
      var dates: Date[] = this.convertDaysFromStringArray(calendarSelection.selectedDays, dateService);
      var sortedDates: Date[] = dates.sort((date1, date2) => dateService.compareDates(date1, date2));
      return sortedDates.find(item => dateService.compareDates(item, dateService.today()) >= 0)
    }

    return null;
  }
}






