import { NgModule} from '@angular/core';

import { ThemeModule } from '../../../@theme/theme.module';

import {
  NbCardModule,
  NbTabsetModule,
} from '@nebular/theme';

import { TranslateModule } from '@ngx-translate/core';
import { RequestManagerComponent } from './request-manager.component';
import { CustomComponentsModule } from '../../custom-components/custom-components.module';

@NgModule({
  imports: [
    TranslateModule.forChild(),
    ThemeModule,
    NbCardModule,
    NbTabsetModule,

    CustomComponentsModule,


  ],
  declarations: [
    RequestManagerComponent,
  ],
})
export class RequestManagerModule { }
