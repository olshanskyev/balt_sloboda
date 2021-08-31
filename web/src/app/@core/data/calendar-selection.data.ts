

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
  last = -1,
}
/* format
  [
    ['Monday', [1, -1]] // first and last monday
    ['Sunday', [2]] // second sunday
  ]
  */
export type MonthDays = Map<string, Array<EveryDays>>;
export type WeekDays = Map<string, boolean>;
export class CalendarSelectionData {
  private selectionMode: SelectionMode;
  private monthDays: MonthDays;
  private weekDays: WeekDays;
  private selectedDays: Array<Date>;

  private constructor() {
  }

  public createManualSelection(selectedDays: Array<Date>): CalendarSelectionData {
    this.selectedDays = selectedDays;
    this.selectionMode = SelectionMode.Manually;
    return this;
  }

  public createMonthSelection(monthDays: MonthDays): CalendarSelectionData {
    this.monthDays = monthDays;
    this.selectionMode = SelectionMode.Monthly;
    return this;
  }

  public createWeekSelection(weekDays: WeekDays): CalendarSelectionData {
    this.weekDays = weekDays;
    this.selectionMode = SelectionMode.Weekly;
    return this;
  }
}


