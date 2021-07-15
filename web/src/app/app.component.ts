/**
 * @license
 * Copyright Akveo. All Rights Reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NbPasswordAuthStrategy } from '@nebular/auth';
import { NbMenuService } from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { AnalyticsService } from './@core/utils/analytics.service';
import { SeoService } from './@core/utils/seo.service';
import { RussianLanguage } from './lang/russian.language';


@Component({
  selector: 'ngx-app',
  template: '<router-outlet></router-outlet>',
})
export class AppComponent implements OnInit {

  constructor(private analytics: AnalyticsService, private seoService: SeoService,
    passwordStrategy: NbPasswordAuthStrategy,
    private menuService: NbMenuService, private router: Router,
    private translateService: TranslateService
    ) {

      this.translations();
      this.translateService.setDefaultLang('ru');
      this.translateService.use('ru');

    /*this.menuService.onItemClick()
      .subscribe((event) => {
        this.onContecxtItemSelection(event.item.title);
      });*/
  }

  /*onContecxtItemSelection(title:string) {

  }*/

  ngOnInit(): void {
    this.analytics.trackPageViews();
    this.seoService.trackCanonicalChanges();
  }

  private translations() {
    this.translateService.setTranslation('ru', RussianLanguage.getTranslations());
  }





}
