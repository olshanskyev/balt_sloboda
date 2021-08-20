import { NgModule } from '@angular/core';
import { NbMenuModule } from '@nebular/theme';

import { ThemeModule } from '../@theme/theme.module';
import { PagesComponent } from './pages.component';
import { PagesRoutingModule } from './pages-routing.module';
import { MiscellaneousModule } from './miscellaneous/miscellaneous.module';
import { NewsPageModule } from './news/news-page.module';
import { CustomLoginModule } from './login/custom-login.module';
import { ResidentsPageModule } from './management/residents/residents-page.module';
import { AddressesPageModule } from './management/addresses/addresses-page.module';


@NgModule({
  imports: [
    PagesRoutingModule,
    ThemeModule,
    NbMenuModule,
    MiscellaneousModule,
    NewsPageModule,
    CustomLoginModule,
    ResidentsPageModule,
    AddressesPageModule,
  ],
  declarations: [
    PagesComponent,
  ],
})
export class PagesModule {
}
