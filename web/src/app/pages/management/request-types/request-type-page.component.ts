import { Component} from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import {
  NbToastrService,
} from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { RequestType } from '../../../@core/data/request-service-data';
import { InterconnectionService } from '../../../@core/service/interconnection-service copy';
import { RequestService } from '../../../@core/service/request-service';

import { Toaster } from '../../Toaster';


@Component({
  selector: 'ngx-request-type',
  templateUrl: './request-type-page.component.html',
  styleUrls: ['./request-type-page.component.scss'],
})


export class RequestTypePageComponent {

  private toaster: Toaster;

  translations: any;
  requestTypeId: number;
  gotRequestType: RequestType;

  constructor(private toastrService: NbToastrService, translateService: TranslateService,
    private route: ActivatedRoute,
    private requestService: RequestService,
    private interconnectionService: InterconnectionService) {
    this.toaster = new Toaster(toastrService);

    this.translations = translateService.translations[translateService.currentLang];

    this.route.params.subscribe((params: Params) => {
      this.requestTypeId = parseInt(params['requestTypeId'], 10);
      this.requestService.getRequestTypeById(this.requestTypeId).subscribe( res => {
        this.gotRequestType = res;
      });
    });
  }


  updateRequestType(requestType: RequestType) {
    this.requestService.updateRequestType(this.requestTypeId, requestType).subscribe(res => {
      this.toaster.showToast(this.toaster.types[1], this.translations.common.changesSaved,'');
      this.interconnectionService.notifyRequestsListChanged(); // notify to update menu
    })
  }



}
