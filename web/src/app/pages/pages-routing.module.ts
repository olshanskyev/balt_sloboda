import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

import { PagesComponent } from './pages.component';
import { NotFoundComponent } from './miscellaneous/not-found/not-found.component';
import { NewsComponent } from './news/news.component';
import { PageAccessChecker } from '../auth/PageAccessChecker';
import { ResidentsPageComponent } from './management/residents/residents-page.component';
import { AddressesPageComponent } from './management/addresses/addresses-page.component';
import { RequestManagerPageComponent } from './management/request-manager/request-manager-page.component';
import { RequestTypePageComponent } from './management/request-types/request-type-page.component';
import { SingleRequestPageComponent } from './requests/single-request/single-request-page.component';
import { AllRequestsPageComponent } from './requests/all-requests/all-requests-page.component';

const routes: Routes = [{
  path: '',
  component: PagesComponent,
  children: [
    {
      path: 'news',
      component: NewsComponent,
      canActivate: [PageAccessChecker], // Check rights
      data: {
        permission: 'view_page',
        resource: 'news',
      },
    },
    {
      path: 'management/residents',
      component: ResidentsPageComponent,
      canActivate: [PageAccessChecker], // Check rights
      data: {
        permission: 'view_page',
        resource: 'management/residents',
      },
    },
    {
      path: 'management/addresses',
      component: AddressesPageComponent,
      canActivate: [PageAccessChecker], // Check rights
      data: {
        permission: 'view_page',
        resource: 'management/addresses',
      },
    },
    {
      path: 'management/requestManager',
      component: RequestManagerPageComponent,
      canActivate: [PageAccessChecker], // Check rights
      data: {
        permission: 'view_page',
        resource: 'management/requestManager',
      },
    },
    {
      path: 'management/requestTypes/:requestTypeId',
      component: RequestTypePageComponent,
      canActivate: [PageAccessChecker], // Check rights
      data: {
        permission: 'view_page',
        resource: 'management/requestTypes',
      },
    },

    {
      path: 'requests',
      component: AllRequestsPageComponent,
      canActivate: [PageAccessChecker], // Check rights
      data: {
        permission: 'view_page',
        resource: 'allRequests',
      },
    },
    {
      path: 'requests/:requestTypeName',
      component: SingleRequestPageComponent,
      canActivate: [PageAccessChecker], // Check rights
      data: {
        permission: 'view_page',
        resource: 'singleRequest',
      },
    },
    {
      path: '**',
      component: NotFoundComponent,
    },
  ],
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PagesRoutingModule {
}
