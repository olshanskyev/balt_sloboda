import { NgModule} from '@angular/core';

import { ThemeModule } from '../../../@theme/theme.module';

import {
  NbCardModule,
} from '@nebular/theme';

import { RequestTypePageComponent } from './request-type-page.component';
import { TranslateModule } from '@ngx-translate/core';
import { RouterModule } from '@angular/router';
import { CustomComponentsModule } from '../../custom-components/custom-components.module';

@NgModule({
  imports: [
    TranslateModule.forChild(),
    ThemeModule,
    NbCardModule,
    RouterModule,
    CustomComponentsModule,
  ],
  declarations: [
    RequestTypePageComponent,
  ],
})
export class RequestTypePageModule { }
