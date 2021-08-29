import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

import { PagesComponent } from './pages.component';
import { NotFoundComponent } from './miscellaneous/not-found/not-found.component';
import { NewsComponent } from './news/news.component';
import { PageAccessChecker } from '../auth/PageAccessChecker';
import { ResidentsComponent } from './management/residents/residents.component';
import { AddressesComponent } from './management/addresses/addresses.component';
import { RequestManagerComponent } from './management/request-manager/request-manager.component';

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
      component: ResidentsComponent,
      canActivate: [PageAccessChecker], // Check rights
      data: {
        permission: 'view_page',
        resource: 'management/residents',
      },
    },
    {
      path: 'management/addresses',
      component: AddressesComponent,
      canActivate: [PageAccessChecker], // Check rights
      data: {
        permission: 'view_page',
        resource: 'management/addresses',
      },
    },
    {
      path: 'management/requestManager',
      component: RequestManagerComponent,
      canActivate: [PageAccessChecker], // Check rights
      data: {
        permission: 'view_page',
        resource: 'management/requestManager',
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
