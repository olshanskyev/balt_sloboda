import { NgModule} from '@angular/core';

import { ThemeModule } from '../../../@theme/theme.module';

import {
  NbButtonModule,
  NbCardModule, NbCheckboxModule, NbIconModule, NbInputModule, NbListModule, NbSelectModule, NbTabsetModule, NbTooltipModule,
} from '@nebular/theme';

import { TranslateModule } from '@ngx-translate/core';
import { SingleRequestPageComponent } from './single-request-page.component';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CustomComponentsModule } from '../../custom-components/custom-components.module';
import { NbSecurityModule } from '@nebular/security';

@NgModule({
  imports: [
    TranslateModule.forChild(),
    ThemeModule,
    NbCardModule,
    NbTabsetModule,
    RouterModule,
    NbIconModule,
    NbListModule,
    NbSelectModule,
    NbTooltipModule,
    NbInputModule,
    NbButtonModule,
    FormsModule,
    CustomComponentsModule,
    NbSecurityModule,
    NbCheckboxModule,
  ],
  declarations: [
    SingleRequestPageComponent,
  ],
})
export class SingleRequestPageModule { }
