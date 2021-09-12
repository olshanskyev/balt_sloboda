import { NbDateService } from "@nebular/theme";


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

  public static createManualSelection(selectedDays: Array<Date>, dateService: NbDateService<Date>): CalendarSelectionData {
    var dates: Array<string> = []
    selectedDays.forEach(item => {
      dates.push(dateService.getYear(item) + '-' + (dateService.getMonth(item) + 1) + '-' + dateService.getDate(item));
    });
    return {
      selectionMode: SelectionMode.Manually,
      selectedDays: dates,
    }
  }

  private static convertMapToJsonObject<T>(map: Map<string, T>): Map<string, T> {
    let jsonObject = new Map();
      map.forEach((value, key) => {
        jsonObject[key] = value
      });
    return jsonObject;
  }

  public static createMonthSelection(monthDays: MonthDays): CalendarSelectionData {
    return {
      selectionMode: SelectionMode.Monthly,
      monthDays: this.convertMapToJsonObject<Array<EveryDays>>(monthDays),
    }
  }

  public static createWeekSelection(weekDays: WeekDays): CalendarSelectionData {
    return {
      selectionMode: SelectionMode.Weekly,
      weekDays: this.convertMapToJsonObject<boolean>(weekDays),
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
}




