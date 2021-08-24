import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from './users-service';
import { AddressesService } from './addresses-service';
import { ResidentsService } from './residents-service';
import { RequestService } from './request-service';
import { NewRequestsService } from './new-requests-service';


const SERVICES = [
  UserService,
  AddressesService,
  ResidentsService,
  RequestService,
  NewRequestsService,
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
