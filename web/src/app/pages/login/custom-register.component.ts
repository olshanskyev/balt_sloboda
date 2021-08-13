import { ChangeDetectorRef, Component, Inject } from '@angular/core';
import { Router } from '@angular/router';
import { NbAuthService, NbRegisterComponent, NB_AUTH_OPTIONS } from '@nebular/auth';
import { NbToastrService } from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { Address } from '../../@core/data/addresses-service-data';
import { AddressesService } from '../../@core/service/addresses-service';
import { Toaster } from '../Toaster';

@Component({
  selector: 'ngx-register',
  templateUrl: './custom-register.component.html',
})
export class CustomRegisterComponent extends NbRegisterComponent {

  streets: string[];
  selectedStreet: string;
  addressesOnStreet: Address[];
  private toaster: Toaster;
  translations: any;
  constructor(authService: NbAuthService, @Inject(NB_AUTH_OPTIONS) options = {},
              cd: ChangeDetectorRef, router: Router, translateService: TranslateService,
              private addressesService: AddressesService,
              toastrService: NbToastrService) {
    super(authService, options, cd, router);
    options['forms'].validation.fullName.minLength = 2;
    options['forms'].validation.fullName.maxLength = 32;
    options['forms'].validation.fullName.required = true;

    this.translations = translateService.translations[translateService.currentLang];
    this.toaster = new Toaster(toastrService);

    addressesService.getAllStreets().subscribe(
      res => {
        this.streets = res;
      },
      err => {
        this.toaster.showToast(this.toaster.types[4], this.translations.errors.cannotGetStreet,
          `${err.message}. ${this.translations.errors.errorCode}: ${err.status}`);
      }
    );
  }

  onStreetSelected(street: string) {
    this.user.address = null;
    this.addressesService.getAddressesOnStreet(street).subscribe(
      res => {
        this.addressesOnStreet = res;
      }, err => {
        this.toaster.showToast(this.toaster.types[4], this.translations.errors.cannotGetAddresses,
          `${err.message}. ${this.translations.errors.errorCode}: ${err.status}`);
      }
    );
  }



}
