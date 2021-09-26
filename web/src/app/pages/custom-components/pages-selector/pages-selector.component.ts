import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from "@angular/core";
import { TranslateService } from "@ngx-translate/core";

@Component({
    selector: 'ngx-pages-selector',
    templateUrl: 'pages-selector.component.html',
    styleUrls: ['pages-selector.component.scss'],
  })

  export class PagesSelectorComponent implements OnChanges{
    translations: any;
    @Input() currentPage: number = 0;
    @Input() totalPages: number = 0;
    @Output() pageChanged: EventEmitter<number> = new EventEmitter<number>();

    constructor(private translateService: TranslateService) {
      this.translations = translateService.translations[translateService.currentLang];
    }

    ngOnChanges(changes: SimpleChanges): void {
      if (changes['currentPage'] && (changes['currentPage'].currentValue !== null)) {
        this.currentPage = changes['currentPage'].currentValue;
      }

      if (changes['totalPages'] && (changes['totalPages'].currentValue !== null)) {
        this.totalPages = changes['totalPages'].currentValue;
      }
    }

    onNextPage() {
      this.currentPage++;
      this.pageChanged.emit(this.currentPage);
    }

    onPrevPage() {
      this.currentPage--;
      this.pageChanged.emit(this.currentPage);
    }

}
