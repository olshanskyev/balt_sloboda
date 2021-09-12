import { NgModule} from '@angular/core';

import { ThemeModule } from '../../../@theme/theme.module';

import {
  NbButtonModule,
  NbCardModule, NbIconModule, NbInputModule, NbListModule, NbSelectModule, NbTabsetModule, NbTooltipModule,
} from '@nebular/theme';

import { TranslateModule } from '@ngx-translate/core';
import { SingleRequestPageComponent } from './single-request-page.component';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CustomComponentsModule } from '../../custom-components/custom-components.module';

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
  ],
  declarations: [
    SingleRequestPageComponent,
  ],
})
export class SingleRequestPageModule { }
