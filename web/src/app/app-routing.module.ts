import { ExtraOptions, RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import {
  NbAuthComponent,
  NbLogoutComponent,
} from '@nebular/auth';


import {AuthGuard} from './auth/AuthGuard';
import { CustomLoginComponent } from './pages/login/custom-login.component';
import { CustomRegisterComponent } from './pages/login/custom-register.component';
import { CustomResetPassComponent } from './pages/login/custom-reset-pass.component';
import { CustomRequestPassComponent } from './pages/login/custom-request-pass.component';

export const routes: Routes = [
  {
    path: 'pages',
    loadChildren: () => import('./pages/pages.module')
      .then(m => m.PagesModule),
    canActivate: [AuthGuard], // here we tell Angular to check the access with our AuthGuard

  },
  {
    path: 'auth',
    component: NbAuthComponent,
    children: [
      {
        path: '',
        component: CustomLoginComponent,
      },
      {
        path: 'login',
        component: CustomLoginComponent,
      },
      {
        path: 'register',
        component: CustomRegisterComponent,
      },
      {
        path: 'logout',
        component: NbLogoutComponent,
      },
      {
        path: 'request-password',
        component: CustomRequestPassComponent,
      },
      {
        path: 'reset-password',
        component: CustomResetPassComponent,
      },
    ],
  },
  { path: '', redirectTo: 'pages/news', pathMatch: 'full' },
  { path: '**', redirectTo: 'pages/news' },
];

const config: ExtraOptions = {
  useHash: false,
};

@NgModule({
  imports: [RouterModule.forRoot(routes, config)],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
