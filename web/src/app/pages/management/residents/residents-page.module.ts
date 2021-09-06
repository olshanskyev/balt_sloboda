import { NgModule} from '@angular/core';

import { ThemeModule } from '../../../@theme/theme.module';

import {
  NbButtonModule,
  NbCardModule, NbIconModule, NbTabsetModule,
} from '@nebular/theme';

import { ResidentsPageComponent } from './residents-page.component';
import { TranslateModule } from '@ngx-translate/core';
import { Ng2SmartTableModule } from 'ng2-smart-table';

@NgModule({
  imports: [
    TranslateModule.forChild(),
    ThemeModule,
    NbCardModule,
    NbTabsetModule,
    Ng2SmartTableModule,
    NbButtonModule,
    NbIconModule,
  ],
  providers: [
  ],
  declarations: [
    ResidentsPageComponent,
  ],
})
export class ResidentsPageModule { }
