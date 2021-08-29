/**
 * @license
 * Copyright Akveo. All Rights Reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */
import { Component, Inject, LOCALE_ID, OnInit } from '@angular/core';
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
    private translateService: TranslateService,
    @Inject(LOCALE_ID) public locale: string
    ) {
      this.translations();
      this.translateService.setDefaultLang(locale);
      this.translateService.use(locale);

  }

  ngOnInit(): void {
    this.analytics.trackPageViews();
    this.seoService.trackCanonicalChanges();
  }

  private translations() {
    this.translateService.setTranslation('ru', RussianLanguage.getTranslations());
  }





}
