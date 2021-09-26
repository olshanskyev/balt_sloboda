import { Component, OnDestroy, OnInit } from '@angular/core';

import {
  NbDateService,
  NbToastrService,
} from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { Observable, Subscription } from 'rxjs';
import { Page } from '../../../@core/data/page';
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

  currentAssignedToMePage: number = 0;
  currentMyRequestsPage: number = 0;
  pageSize: number = 6;
  totalAssignedToMePages: number;
  totalMyRequestsPages: number;
  totalAssignedToMeElements: number;
  totalMyElements: number;

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
              this.loadMyRequests();
            }
        });
        this.assignedToMeActiveRequestsCountSubscription = this.requestService.getAssignedToMeActiveRequestsCountSubscription()
          .subscribe(requestsCount => {
            if (requestsCount) {
              this.assignedToMeRequestsCount = 0;
              requestsCount.forEach(item => this.assignedToMeRequestsCount += item.count);
              this.loadAssignedToMeRequests();
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

  loadMyRequests() {
    let requestsSub: Observable<Page<Request>>;
    if (this.showOnlyMyActiveRequests) {
      requestsSub = this.requestService.getMyRequests(this.currentMyRequestsPage, this.pageSize, [RequestStatus.NEW, RequestStatus.ACCEPTED, RequestStatus.IN_PROGRESS]);
    } else {
      requestsSub = this.requestService.getMyRequests(this.currentMyRequestsPage, this.pageSize);
    }
    requestsSub.subscribe(res => {
      this.myRequests = res.content;
      this.totalMyRequestsPages = res.totalPages;
      this.totalMyElements = res.totalElements;
      if (this.currentMyRequestsPage >= this.totalMyRequestsPages && this.totalMyRequestsPages > 0) {
        this.currentMyRequestsPage = this.totalMyRequestsPages - 1;
        this.loadMyRequests();
      }
    });
  }

  loadAssignedToMeRequests() {
    let requestsSub: Observable<Page<Request>>;
    if (this.showOnlyAssignedToMeActiveRequests) {
      requestsSub = this.requestService.getAssignedToMeRequests(this.currentAssignedToMePage, this.pageSize, [RequestStatus.NEW, RequestStatus.ACCEPTED, RequestStatus.IN_PROGRESS]);
    } else {
      requestsSub = this.requestService.getAssignedToMeRequests(this.currentAssignedToMePage, this.pageSize);
    }
    requestsSub.subscribe(res => {
      this.assignedToMeRequests = res.content;
      this.totalAssignedToMePages = res.totalPages;
      this.totalAssignedToMeElements = res.totalElements;
      if (this.currentAssignedToMePage >= this.totalAssignedToMePages && this.totalAssignedToMePages > 0) {
        this.currentAssignedToMePage = this.totalAssignedToMePages - 1;
        this.loadAssignedToMeRequests();
      }
    });
  }

  assignedToMeRequestsPageChanged(pageNumber: number) {
    this.currentAssignedToMePage = pageNumber;
    this.loadAssignedToMeRequests();
  }

  myRequestsPageChanged(pageNumber: number) {
    this.currentMyRequestsPage = pageNumber;
    this.loadMyRequests();
  }

}
