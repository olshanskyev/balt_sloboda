import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from './users-service';
import { AddressesService } from './addresses-service';
import { ResidentsService } from './residents-service';
import { RequestService } from './request-service';
import { NewUserRequestsInterconnectionService } from './new-user-request-interconnection-service';
import { CalendarSelectionService } from './calendar-selection.service';
import { InterconnectionService } from './interconnection-service copy';


const SERVICES = [
  UserService,
  AddressesService,
  ResidentsService,
  RequestService,
  InterconnectionService,
  NewUserRequestsInterconnectionService,
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
