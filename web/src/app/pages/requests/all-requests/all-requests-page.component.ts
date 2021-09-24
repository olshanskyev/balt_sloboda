import { Component, OnDestroy, OnInit } from '@angular/core';

import {
  NbDateService,
  NbToastrService,
} from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { Request } from '../../../@core/data/request-service-data';
import { RequestService } from '../../../@core/service/request-service';

import { Toaster } from '../../Toaster';


@Component({
  selector: 'ngx-all-requests',
  templateUrl: './all-requests-page.component.html',
  styleUrls: ['./all-requests-page.component.scss'],
})


export class AllRequestsPageComponent implements OnDestroy, OnInit {

  private toaster: Toaster;
  translations: any;
  myActiveRequestsSubscription: Subscription = null;
  assignedToMeActiveRequestsSubscription: Subscription = null;

  myRequests: Request[] = [];
  myActiveRequests: Request[] = [];
  assignedToMeRequests: Request[] = [];
  assignedToMeActiveRequests: Request[] = [];
  showOnlyMyActiveRequests: boolean = true;
  showOnlyAssignedToMeActiveRequests: boolean = true;

  constructor(private toastrService: NbToastrService, translateService: TranslateService,
    private requestService: RequestService,
    protected dateService: NbDateService<Date>) {

    this.toaster = new Toaster(toastrService);
    this.translations = translateService.translations[translateService.currentLang];
  }

  ngOnInit(): void {
    this.myActiveRequestsSubscription = this.requestService.getMyActiveRequestsSubscription()
          .subscribe(requests => {
            if (requests) { // null at first time
              this.myActiveRequests = requests.content.filter(item => !item.type.systemRequest);
              this.refreshMyRequests();

            }
        });
        this.assignedToMeActiveRequestsSubscription = this.requestService.getAssignedToMeActiveRequestsSubscription()
          .subscribe(requests => {
            if (requests) {
              this.assignedToMeActiveRequests = requests.content.filter(item => !item.type.systemRequest);
              this.refreshAssignedToMeRequests();
            }
        });
  }

  ngOnDestroy(): void {
    if (this.myActiveRequestsSubscription) {
      this.myActiveRequestsSubscription.unsubscribe();
    }
    if (this.assignedToMeActiveRequestsSubscription) {
      this.assignedToMeActiveRequestsSubscription.unsubscribe();
    }
  }

  refreshMyRequests() {
    if (!this.showOnlyMyActiveRequests) {
      this.requestService.getMyRequests().subscribe(res => {
        this.myRequests = res.content.filter(item => !item.type.systemRequest);
      });
    } else {
      this.myRequests = [];
    }
  }

  refreshAssignedToMeRequests() {
    if (!this.showOnlyAssignedToMeActiveRequests) {
      this.requestService.getAssignedToMeRequests().subscribe(res => {
        this.assignedToMeRequests = res.content.filter(item => !item.type.systemRequest);
      });
    } else {
      this.assignedToMeRequests = [];
    }
  }

}
