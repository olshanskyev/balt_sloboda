import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from './users-service';
import { AddressesService } from './addresses-service';
import { ResidentsService } from './residents-service';
import { RequestService } from './request-service';
import { CalendarSelectionService } from './calendar-selection.service';


const SERVICES = [
  UserService,
  AddressesService,
  ResidentsService,
  RequestService,
  CalendarSelectionService,
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
