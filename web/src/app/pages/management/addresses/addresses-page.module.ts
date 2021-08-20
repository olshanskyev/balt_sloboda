import { NgModule} from '@angular/core';

import { ThemeModule } from '../../../@theme/theme.module';

import {
  NbCardModule, NbTabsetModule,
} from '@nebular/theme';

import { AddressesComponent } from './addresses.component';
import { TranslateModule } from '@ngx-translate/core';
import { Ng2SmartTableModule } from 'ng2-smart-table';

@NgModule({
  imports: [
    TranslateModule.forChild(),
    ThemeModule,
    NbCardModule,
    NbTabsetModule,
    Ng2SmartTableModule,
  ],
  declarations: [
    AddressesComponent,
  ],
})
export class AddressesPageModule { }
