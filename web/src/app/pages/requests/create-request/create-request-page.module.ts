import { NgModule} from '@angular/core';

import { ThemeModule } from '../../../@theme/theme.module';

import {
  NbCardModule, NbTabsetModule,
} from '@nebular/theme';

import { TranslateModule } from '@ngx-translate/core';
import { CreateRequestPageComponent } from './create-request-page.component';

@NgModule({
  imports: [
    TranslateModule.forChild(),
    ThemeModule,
    NbCardModule,
    NbTabsetModule,
  ],
  declarations: [
    CreateRequestPageComponent,
  ],
})
export class CreateRequestPageModule { }
