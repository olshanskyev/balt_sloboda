import { NgModule} from '@angular/core';

import { ThemeModule } from '../../../@theme/theme.module';

import {
  NbButtonModule,
  NbCardModule, NbCheckboxModule, NbIconModule, NbTabsetModule,
} from '@nebular/theme';

import { ResidentsPageComponent } from './residents-page.component';
import { TranslateModule } from '@ngx-translate/core';
import { Ng2SmartTableModule } from 'ng2-smart-table';
import { CustomComponentsModule } from '../../custom-components/custom-components.module';

@NgModule({
  imports: [
    TranslateModule.forChild(),
    ThemeModule,
    NbCardModule,
    NbTabsetModule,
    Ng2SmartTableModule,
    NbButtonModule,
    NbIconModule,
    CustomComponentsModule,
    NbCheckboxModule,
  ],
  providers: [
  ],
  declarations: [
    ResidentsPageComponent,
  ],
})
export class ResidentsPageModule { }
