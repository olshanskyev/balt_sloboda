/**
 * @license
 * Copyright Akveo. All Rights Reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

 import {
    Component,
    ChangeDetectionStrategy,
    Input,
    Output,
    EventEmitter,
    HostBinding,
    HostListener,
  } from '@angular/core';





  import { NbCalendarCell, NbCalendarSize, NbCalendarSizeValues, NbDateService } from '@nebular/theme';
import { MultiSelectCalendarData } from './multi-select-calendar.component';

  @Component({
    selector: 'nb-calendar-range-year-cell',
    template: `
      <div class="cell-content">
        {{ year }}
      </div>
    `,
    changeDetection: ChangeDetectionStrategy.OnPush,
  })
  export class MultiSelectCalendarYearCellComponent<D> implements NbCalendarCell<D, MultiSelectCalendarData<D>> {

    @Input() date: D;

    @Input() selectedValue: MultiSelectCalendarData<D>;

    @Input() min: D;

    @Input() max: D;


    @Input() size: NbCalendarSize = NbCalendarSize.MEDIUM;
    static ngAcceptInputType_size: NbCalendarSizeValues;

    @Output() select: EventEmitter<D> = new EventEmitter(true);

    constructor(protected dateService: NbDateService<D>) {
    }

    @HostBinding('class.in-range')
    get inArray(): boolean {
      return this.isInArray(this.date, this.selectedValue.array);
    }

    @HostBinding('class.selected')
    get selected(): boolean {
      return this.inArray;
    }

    @HostBinding('class.today')
    get today(): boolean {
      return this.dateService.isSameYear(this.date, this.dateService.today());
    }

    @HostBinding('class.disabled')
    get disabled(): boolean {
      return this.smallerThanMin() || this.greaterThanMax();
    }

    @HostBinding('class.size-large')
    get isLarge(): boolean {
      return this.size === NbCalendarSize.LARGE;
    }

    @HostBinding('class.year-cell')
    yearCellClass = true;

    @HostBinding('class.range-cell')
    rangeCellClass = true;

    get year(): number {
      return this.dateService.getYear(this.date);
    }

    @HostListener('click')
    onClick() {
      if (this.disabled) {
        return;
      }

      this.select.emit(this.date);
    }

    protected smallerThanMin(): boolean {
      return this.date && this.min && this.dateService.compareDates(this.yearEnd(), this.min) < 0;
    }

    protected greaterThanMax(): boolean {
      return this.date && this.max && this.dateService.compareDates(this.yearStart(), this.max) > 0;
    }

    protected yearStart(): D {
      return this.dateService.getYearStart(this.date);
    }

    protected yearEnd(): D {
      return this.dateService.getYearEnd(this.date);
    }

    protected isInArray(date: D, array: Array<D>): boolean {
      const foundIndex = array.findIndex(item => {
        return this.dateService.isSameYear(item, date);
      });

      return foundIndex > -1;
    }
  }