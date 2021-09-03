import { KeyValue, WeekDay } from '@angular/common';
import { Component, ViewChild} from '@angular/core';

import {
  NbDateService,
  NbToastrService,
} from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';

import { User } from '../../../@core/data/user-service-data';
import { UserService } from '../../../@core/service/users-service';

import { Toaster } from '../../Toaster';

export interface UserGroup {
  name: string;
  users: User[];
}

@Component({
  selector: 'ngx-request-manager',
  templateUrl: './request-manager.component.html',
  styleUrls: ['./request-manager.component.scss'],
})
export class RequestManagerComponent{

  private toaster: Toaster;
  translations: any;



  constructor(private toastrService: NbToastrService, translateService: TranslateService,
    protected dateService: NbDateService<Date>,
    private userService: UserService) {
    this.toaster = new Toaster(toastrService);
    this.translations = translateService.translations[translateService.currentLang];

  }

}
