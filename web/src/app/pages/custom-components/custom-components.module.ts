import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {
  NbAutocompleteModule,
  NbBaseCalendarModule,
  NbButtonModule,
  NbCardModule,
  NbCheckboxModule,
  NbDialogModule,
  NbFormFieldModule,
  NbIconModule,
  NbInputModule,
  NbListModule,
  NbRadioModule,
  NbSelectModule,
  NbStepperModule
} from '@nebular/theme';
import { TranslateModule } from '@ngx-translate/core';


import { ThemeModule } from '../../@theme/theme.module';
import { IconPickerWindowComponent } from './icon-picker/icon-picker-window.component';
import { MultiSelectCalendarComponent } from './multi-select-calendar/multi-select-calendar.component';
import { RequestTypeStepperComponent } from './steppers/request-type-stepper.component';


@NgModule({
  imports: [
    TranslateModule.forChild(),
    ThemeModule,
    NbCardModule,
    NbBaseCalendarModule,
    NbCheckboxModule,
    NbListModule,
    NbSelectModule,
    NbIconModule,
    NbAutocompleteModule,
    ReactiveFormsModule,
    NbFormFieldModule,
    NbStepperModule,
    FormsModule,
    NbInputModule,
    NbRadioModule,
    NbButtonModule,
    NbDialogModule.forChild(),

  ],
  declarations: [
    MultiSelectCalendarComponent,
    RequestTypeStepperComponent,
    IconPickerWindowComponent,
  ],
  exports: [
    MultiSelectCalendarComponent,
    RequestTypeStepperComponent,
  ],
  entryComponents: [
    IconPickerWindowComponent,
  ]
})
export class CustomComponentsModule { }
