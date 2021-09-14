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
import { Ng2SmartTableModule } from 'ng2-smart-table';


import { ThemeModule } from '../../@theme/theme.module';
import { IconPickerWindowComponent } from './icon-picker/icon-picker-window.component';
import { MultiSelectCalendarComponent } from './multi-select-calendar/multi-select-calendar.component';
import { RequestsTableComponent } from './requests-table/requests-table.component';
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
    Ng2SmartTableModule,

  ],
  declarations: [
    MultiSelectCalendarComponent,
    RequestTypeStepperComponent,
    IconPickerWindowComponent,
    RequestsTableComponent,
  ],
  exports: [
    MultiSelectCalendarComponent,
    RequestTypeStepperComponent,
    RequestsTableComponent,
  ],
  entryComponents: [
    IconPickerWindowComponent,
  ]
})
export class CustomComponentsModule { }
