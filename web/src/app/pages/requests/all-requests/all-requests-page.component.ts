import { Component, OnDestroy, OnInit } from '@angular/core';

import {
  NbDateService,
  NbToastrService,
} from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { Request, RequestStatus } from '../../../@core/data/request-service-data';
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
  myActiveRequestsCountSubscription: Subscription = null;
  assignedToMeActiveRequestsCountSubscription: Subscription = null;

  myRequests: Request[] = [];
  myRequestsCount: number = 0;
  assignedToMeRequests: Request[] = [];
  assignedToMeRequestsCount: number = 0;
  showOnlyMyActiveRequests: boolean = true;
  showOnlyAssignedToMeActiveRequests: boolean = true;

  constructor(private toastrService: NbToastrService, translateService: TranslateService,
    private requestService: RequestService,
    protected dateService: NbDateService<Date>) {

    this.toaster = new Toaster(toastrService);
    this.translations = translateService.translations[translateService.currentLang];
  }

  ngOnInit(): void {
    this.myActiveRequestsCountSubscription = this.requestService.getMyActiveRequestsCountSubscription()
          .subscribe(requestsCount => {
            if (requestsCount) { // null at first time
              this.myRequestsCount = 0;
              requestsCount.forEach(item => this.myRequestsCount += item.count);
              this.refreshMyRequests();
            }
        });
        this.assignedToMeActiveRequestsCountSubscription = this.requestService.getAssignedToMeActiveRequestsCountSubscription()
          .subscribe(requestsCount => {
            if (requestsCount) {
              this.assignedToMeRequestsCount = 0;
              requestsCount.forEach(item => this.assignedToMeRequestsCount += item.count);
              this.refreshAssignedToMeRequests();
            }
        });
  }

  ngOnDestroy(): void {
    if (this.myActiveRequestsCountSubscription) {
      this.myActiveRequestsCountSubscription.unsubscribe();
    }
    if (this.assignedToMeActiveRequestsCountSubscription) {
      this.assignedToMeActiveRequestsCountSubscription.unsubscribe();
    }
  }

  refreshMyRequests() {
    if (this.showOnlyMyActiveRequests) {
      this.requestService.getMyRequests(0, 10, [RequestStatus.NEW, RequestStatus.ACCEPTED, RequestStatus.IN_PROGRESS]).subscribe(res => {
        this.myRequests = res.content.filter(item => !item.type.systemRequest);
      });
    } else {
      this.requestService.getMyRequests(0, 10).subscribe(res => {
        this.myRequests = res.content.filter(item => !item.type.systemRequest);
      });
    }
  }

  refreshAssignedToMeRequests() {
    if (this.showOnlyAssignedToMeActiveRequests) {
      this.requestService.getAssignedToMeRequests(0, 10, [RequestStatus.NEW, RequestStatus.ACCEPTED, RequestStatus.IN_PROGRESS]).subscribe(res => {
        this.assignedToMeRequests = res.content.filter(item => !item.type.systemRequest);
      });
    } else {
      this.requestService.getAssignedToMeRequests(0, 10).subscribe(res => {
        console.log(res);
        this.assignedToMeRequests = res.content.filter(item => !item.type.systemRequest);
      });
    }
  }

}
