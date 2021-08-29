import { NgModule } from '@angular/core';
import { NbBaseCalendarModule, NbCalendarKitModule, NbCalendarModule, NbCalendarRangeModule, NbCardModule } from '@nebular/theme';
import { NbSharedModule } from '@nebular/theme/components/shared/shared.module';


import { ThemeModule } from '../../@theme/theme.module';
import { MultiSelectCalendarComponent } from './multi-select-calendar/multi-select-calendar.component';


@NgModule({
  imports: [
    ThemeModule,
    NbBaseCalendarModule,
  ],
  declarations: [
    MultiSelectCalendarComponent,
  ],
  exports: [
    MultiSelectCalendarComponent,
  ]
})
export class CustomComponentsModule { }
