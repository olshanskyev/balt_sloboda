import { ChangeDetectorRef, Component, Inject } from '@angular/core';
import { Router } from '@angular/router';
import { NbAuthService, NbResetPasswordComponent, NB_AUTH_OPTIONS } from '@nebular/auth';
import { NbToastrService } from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { Address } from '../../@core/data/addresses-service-data';
import { Toaster } from '../Toaster';

@Component({
  selector: 'ngx-reset-pass',
  templateUrl: './custom-reset-pass.component.html',
})
export class CustomResetPassComponent extends NbResetPasswordComponent {

  streets: string[];
  selectedStreet: string;
  addressesOnStreet: Address[];
  private toaster: Toaster;
  translations: any;
  terms: boolean = false;

  constructor(authService: NbAuthService, @Inject(NB_AUTH_OPTIONS) options = {},
              cd: ChangeDetectorRef, router: Router, translateService: TranslateService,
              toastrService: NbToastrService) {
    super(authService, options, cd, router);
    this.redirectDelay = 3000;
    this.translations = translateService.translations[translateService.currentLang];
    this.toaster = new Toaster(toastrService);

  }


}
