import { Component} from '@angular/core';

import {
  NbToastrService,
} from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';

import { LocalDataSource } from 'ng2-smart-table';
import { AddressesService } from '../../../@core/service/addresses-service';
import { Toaster } from '../../Toaster';


@Component({
  selector: 'ngx-addresses',
  templateUrl: './addresses-page.component.html',
  styleUrls: ['./addresses-page.component.scss'],
})


export class AddressesPageComponent {

  private toaster: Toaster;


  translations: any;
  constructor(private toastrService: NbToastrService, translateService: TranslateService,
    addressService: AddressesService,
    ) {
    this.toaster = new Toaster(toastrService);

    this.translations = translateService.translations[translateService.currentLang];
    this.settings.columns.street.title = this.translations.addressesPage.street;
    this.settings.columns.houseNumber.title = this.translations.addressesPage.houseNumber;
    this.settings.columns.plotNumber.title = this.translations.addressesPage.plotNumber;

    addressService.getAllAddresses().subscribe(
      res => {
      this.source.load(res);
      this.count = res.length;
    }, err => {
      this.toaster.showToast(this.toaster.types[4], this.translations.errors.cannotGetAddresses,
           '');
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
      street: {
        title: '',
        type: 'string',
        editable: false,
      },
      houseNumber: {
        title: '',
        type: 'string',
        editable: false,
      },
      plotNumber: {
        title: '',
        type: 'string',
        editable: false,
      },


    },
  };

  /*getTableView(addresses: Address[]) {
    return addresses.map((item) => { // map json item into table view
      return {
        userName: 'userName',
        street: item.street,
        houseNumber: item.houseNumber,
        plotNumber: item.plotNumber,

      };
    });
  }*/


}
