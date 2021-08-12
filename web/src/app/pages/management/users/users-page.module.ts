import { NgModule} from '@angular/core';

import { ThemeModule } from '../../../@theme/theme.module';

import {
  NbCardModule, NbTabsetModule,
} from '@nebular/theme';

import { UsersComponent } from './users.component';
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
    UsersComponent,
  ],
})
export class UsersPageModule { }
