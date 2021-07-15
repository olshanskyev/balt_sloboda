import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';

import { PagesComponent } from './pages.component';
import { NotFoundComponent } from './miscellaneous/not-found/not-found.component';
import { NewsComponent } from './news/news.component';
import { PageAccessChecker } from '../auth/PageAccessChecker';
import { UsersComponent } from './management/users/users.component';

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
      path: 'management/users',
      component: UsersComponent,
      canActivate: [PageAccessChecker], // Check rights
      data: {
        permission: 'view_page',
        resource: 'users',
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
