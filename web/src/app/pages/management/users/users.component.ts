import { Component} from '@angular/core';

import {
  NbToastrService,
} from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { LocalDataSource } from 'ng2-smart-table';
import { UserService } from '../../../@core/service/users-service';
import { Toaster } from '../../Toaster';


@Component({
  selector: 'ngx-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss'],
})


export class UsersComponent{

  private toaster:Toaster;



  constructor(private toastrService: NbToastrService, translateService: TranslateService, usersService: UserService) {
    this.toaster = new Toaster(toastrService);

    const translations = translateService.translations[translateService.currentLang];
    this.settings.columns.user.title = translations.usersPage.user;
    this.settings.columns.firstName.title = translations.usersPage.firstName;
    this.settings.columns.lastName.title = translations.usersPage.lastName;

    usersService.getAllUsers().subscribe(res => {
      this.source.load(res);
      this.count = res.length;
    }, err=> {
      this.toaster.showToast(this.toaster.types[4], translations.errors.cannotGetUsers,
           `${err.error}. ${translations.errors.errorCode}: ${err.status}`);
    });
  }


  source: LocalDataSource = new LocalDataSource();
  count: number = 0;
  settings = {
    actions: {
      add: false,
      edit: false,
      delete: false,
    },

    columns: {
      user: {
        title: '',
        type: 'string',
        editable: false,
      },
      firstName: {
        title: '',
        type: 'string',
        editable: false,
      },
      lastName: {
        title: '',
        type: 'string',
        editable: false,
      },

    },
  };

}
