import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from './users-service';
import { AddressesService } from './addresses-service';


const SERVICES = [
  UserService,
  AddressesService,
];

@NgModule({
  imports: [
    CommonModule,
  ],
  providers: [
    ...SERVICES,
  ],
})
export class ServiceModule {
  static forRoot(): ModuleWithProviders<ServiceModule> {
    return {
      ngModule: ServiceModule,
      providers: [
        ...SERVICES,
      ],
    };
  }
}
