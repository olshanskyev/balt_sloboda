import { NgModule} from '@angular/core';

import { ThemeModule } from '../../../@theme/theme.module';

import {
  NbButtonModule,
  NbCardModule, NbCheckboxModule, NbInputModule, NbListModule, NbRadioModule, NbStepperModule, NbTabsetModule,
} from '@nebular/theme';

import { TranslateModule } from '@ngx-translate/core';
import { RequestManagerComponent } from './request-manager.component';
import { FormsModule } from '@angular/forms';
import { CustomComponentsModule } from '../../custom-components/custom-components.module';

@NgModule({
  imports: [
    TranslateModule.forChild(),
    ThemeModule,
    NbCardModule,
    NbTabsetModule,
    NbStepperModule,
    FormsModule,
    NbInputModule,
    NbRadioModule,
    NbButtonModule,
    CustomComponentsModule,
    NbCheckboxModule,
    NbListModule,
  ],
  declarations: [
    RequestManagerComponent,
  ],
})
export class RequestManagerModule { }
