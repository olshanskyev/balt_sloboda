import { NgModule} from '@angular/core';

import { ThemeModule } from '../../../@theme/theme.module';

import {
  NbCardModule,
  NbCheckboxModule,
  NbTabsetModule,
} from '@nebular/theme';

import { TranslateModule } from '@ngx-translate/core';
import { RequestManagerPageComponent } from './request-manager-page.component';
import { CustomComponentsModule } from '../../custom-components/custom-components.module';
import { Ng2SmartTableModule } from 'ng2-smart-table';

@NgModule({
  imports: [
    TranslateModule.forChild(),
    ThemeModule,
    NbCardModule,
    NbTabsetModule,
    Ng2SmartTableModule,
    CustomComponentsModule,
    NbCheckboxModule,


  ],
  declarations: [
    RequestManagerPageComponent,
  ],
})
export class RequestManagerPageModule { }
