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
import { RequestManagerPageModule } from './management/request-manager/request-manager-page.module';
import { RequestTypePageModule } from './management/request-types/request-type-page.module';
import { SingleRequestPageModule } from './requests/single-request/single-request-page.module';
import { AllRequestsPageModule } from './requests/all-requests/all-requests-page.module';


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
    RequestManagerPageModule,
    RequestTypePageModule,
    SingleRequestPageModule,
    AllRequestsPageModule,
  ],
  declarations: [
    PagesComponent,
  ],
})
export class PagesModule {
}
