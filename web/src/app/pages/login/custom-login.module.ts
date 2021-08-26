import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import {
  NbActionsModule, NbAlertModule, NbButtonModule, NbCardModule,
  NbCheckboxModule, NbContextMenuModule, NbInputModule, NbLayoutModule, NbSelectModule,
} from '@nebular/theme';
import { TranslateModule } from '@ngx-translate/core';
import { CustomLoginComponent } from './custom-login.component';
import { CustomRegisterComponent } from './custom-register.component';
import { CustomRequestPassComponent } from './custom-request-pass.component';
import { CustomResetPassComponent } from './custom-reset-pass.component';


@NgModule({
  imports: [
    TranslateModule.forChild(),
    CommonModule,
    FormsModule,
    NbLayoutModule,
    NbActionsModule,
    NbContextMenuModule,
    HttpClientModule,
    NbCardModule,
    NbAlertModule,
    NbCheckboxModule,
    NbInputModule,
    NbButtonModule,
    RouterModule,
    NbSelectModule,
  ],
  declarations: [
    CustomLoginComponent,
    CustomRegisterComponent,
    CustomResetPassComponent,
    CustomRequestPassComponent,
  ],
})
export class CustomLoginModule { }
