import { NgModule} from '@angular/core';

import { ThemeModule } from '../../@theme/theme.module';

import {
  NbCardModule,
} from '@nebular/theme';

import { NewsComponent } from './news.component';

@NgModule({
  imports: [
    ThemeModule,
    NbCardModule,
  ],
  declarations: [
    NewsComponent,
  ],
})
export class NewsPageModule { }
