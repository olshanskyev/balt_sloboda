/**
 * @license
 * Copyright Akveo. All Rights Reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

 import {
    ChangeDetectionStrategy,
    Component,
    EventEmitter,
    HostBinding,
    HostListener,
    Input,
    Output,
  } from '@angular/core';

  import { NbCalendarCell, NbCalendarSize, NbCalendarSizeValues, NbDateService } from '@nebular/theme';
import { MultiSelectCalendarData } from './multi-select-calendar.component';

  @Component({
    selector: 'nb-calendar-range-month-cell',
    template: `
      <div class="cell-content">
        {{ month }}
      </div>
    `,
    changeDetection: ChangeDetectionStrategy.OnPush,
  })
  export class MultiSelectCalendarMonthCellComponent<D> implements NbCalendarCell<D, MultiSelectCalendarData<D>> {

    get month(): string {
      return this.dateService.getMonthName(this.date);
    }

    @Input() date: D;
    @Input() visibleDate: D;

    @Input() selectedValue: MultiSelectCalendarData<D>;
    @Input() min: D;
    @Input() max: D;

    @Input() size: NbCalendarSize = NbCalendarSize.MEDIUM;
    static ngAcceptInputType_size: NbCalendarSizeValues;

    @Output() select: EventEmitter<D> = new EventEmitter(true);

    @HostBinding('class.month-cell')
    monthCellClass = true;

    @HostBinding('class.range-cell')
    rangeCellClass = true;

    @HostBinding('class.selected')
    get selected(): boolean {
      if (this.inArray) {
        return true;
      }
    }

    @HostBinding('class.in-range')
    get inArray(): boolean {

      return this.isInArray(this.date, this.selectedValue.array);

    }


    @HostBinding('class.today')
    get today(): boolean {
      return this.dateService.isSameMonthSafe(this.date, this.dateService.today());
    }

    @HostBinding('class.disabled')
    get disabled(): boolean {
      return this.smallerThanMin() || this.greaterThanMax();
    }

    @HostBinding('class.size-large')
    get isLarge(): boolean {
      return this.size === NbCalendarSize.LARGE;
    }

    @HostListener('click')
    onClick() {
      if (this.disabled) {
        return;
      }

      this.select.emit(this.date);
    }

    constructor(protected dateService: NbDateService<D>) {
    }

    protected smallerThanMin(): boolean {
      return this.date && this.min && this.dateService.compareDates(this.monthEnd(), this.min) < 0;
    }

    protected greaterThanMax(): boolean {
      return this.date && this.max && this.dateService.compareDates(this.monthStart(), this.max) > 0;
    }

    protected monthStart(): D {
      return this.dateService.getMonthStart(this.date);
    }

    protected monthEnd(): D {
      return this.dateService.getMonthEnd(this.date);
    }

    protected isInArray(date: D, array: Array<D>): boolean {
        const foundIndex = array.findIndex(item => {
            return this.dateService.isSameMonth(item, date);
          });

        return foundIndex > -1;
    }
  }