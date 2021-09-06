import { Component} from '@angular/core';

import {
  NbToastrService,
} from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';

import { Toaster } from '../../Toaster';


@Component({
  selector: 'ngx-create-request',
  templateUrl: './create-request-page.component.html',
  styleUrls: ['./create-request-page.component.scss'],
})


export class CreateRequestPageComponent {

  private toaster: Toaster;


  translations: any;
  constructor(private toastrService: NbToastrService, translateService: TranslateService) {
    this.toaster = new Toaster(toastrService);
    this.translations = translateService.translations[translateService.currentLang];
  }





}
