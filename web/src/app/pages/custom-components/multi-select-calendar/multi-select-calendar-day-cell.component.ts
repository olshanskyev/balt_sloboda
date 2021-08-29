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


  @Component({
    selector: 'ngx-multi-calendar-day-cell',
    template: `
      <div class="cell-content">{{ day }}</div>
    `,
    changeDetection: ChangeDetectionStrategy.OnPush,

  })
  export class MultiSelectCalendarDayCellComponent<D> implements NbCalendarCell<D, Array<D>> {


    @Input() date: D;

    @Input() selectedValue: Array<D>;

    @Input() visibleDate: D;

    @Input() min: D;

    @Input() max: D;

    @Input() filter: (D) => boolean;

    @Input() size: NbCalendarSize = NbCalendarSize.MEDIUM;
    static ngAcceptInputType_size: NbCalendarSizeValues;

    @Output() select: EventEmitter<D> = new EventEmitter(true);

    constructor(protected dateService: NbDateService<D>) {
    }

    @HostBinding('class.in-range')
    get inArray(): boolean {
        if (this.date) {
            return this.isInArray(this.date, this.selectedValue);
        }

        return false;
    }

    @HostBinding('class.range-cell')
    rangeCellClass = true;

    @HostBinding('class.day-cell')
    dayCellClass = true;

    @HostBinding('class.today')
    get today(): boolean {
        return this.date && this.dateService.isSameDay(this.date, this.dateService.today());
    }

    @HostBinding('class.bounding-month')
    get boundingMonth(): boolean {
        return !this.dateService.isSameMonthSafe(this.date, this.visibleDate);
    }

    @HostBinding('class.selected')
    get selected(): boolean {
        if (this.inArray) {
            return true;
        } else {
            return false;
        }
    }

    @HostBinding('class.empty')
    get empty(): boolean {
        return !this.date;
    }

    @HostBinding('class.disabled')
    get disabled(): boolean {
        return this.smallerThanMin() || this.greaterThanMax() || this.dontFitFilter();
    }

    @HostBinding('class.size-large')
    get isLarge(): boolean {
        return this.size === NbCalendarSize.LARGE;
    }

    get day(): number {
        return this.date && this.dateService.getDate(this.date);
    }

    @HostListener('click')
    onClick() {
        if (this.disabled || this.empty) {
            return;
        }

        this.select.emit(this.date);
    }

    protected smallerThanMin(): boolean {
        return this.date && this.min && this.dateService.compareDates(this.date, this.min) < 0;
    }

    protected greaterThanMax(): boolean {
        return this.date && this.max && this.dateService.compareDates(this.date, this.max) > 0;
    }

    protected dontFitFilter(): boolean {
        return this.date && this.filter && !this.filter(this.date);
    }

    protected isInArray(date: D, array: Array<D>): boolean {
        const foundIndex = array.findIndex(item => {
            return this.dateService.compareDates(item, date) === 0;
        });

        return foundIndex > -1;
    }
  }