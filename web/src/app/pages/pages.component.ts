import { Component, Injector, OnDestroy, OnInit } from '@angular/core';
import { NbAccessChecker } from '@nebular/security';
import { NbIconLibraries, NbMenuItem } from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { RequestType } from '../@core/data/request-service-data';
import { RequestService } from '../@core/service/request-service';

import { MENU_ITEMS } from './pages-menu';

@Component({
  selector: 'ngx-pages',
  styleUrls: ['pages.component.scss'],
  template: `
    <ngx-one-column-layout>
      <nb-menu [items]="menu"></nb-menu>
      <router-outlet></router-outlet>
    </ngx-one-column-layout>
  `,
})
export class PagesComponent implements OnInit, OnDestroy {

  menu = MENU_ITEMS;
  currentLang: string;

  requestsDisplayOptions: any[];

  newUserRequestsSubscription :Subscription = null;
  requestTypeSubscription :Subscription = null;
  myActiveRequestsCountSubscription: Subscription = null;
  assignedToMeActiveRequestsCountSubscription: Subscription = null;

  constructor(private accessChecker: NbAccessChecker, private translateService: TranslateService,

    private requestsService: RequestService,
    iconsLibrary: NbIconLibraries,
    ) {
    this.currentLang = this.translateService.currentLang;
    iconsLibrary.registerFontPack('ion', { iconClassPrefix: 'ion' });
  }


  private updateBadges() { // badge value = myActiveRequests + assignedToMeActiveRequests
    var requestMenuItem: NbMenuItem = this.menu.filter(item => item.data && item.data.id && item.data.id === 'Requests')[0];
    requestMenuItem.children.forEach(itemRequestMenu => {
      if (itemRequestMenu.data?.name) {
        var activeRequestsCount: number = this.myActiveRequestsCount.get(itemRequestMenu.data.name);
        var assignedToMeCount: number = this.assignedToMeActiveRequestsCount.get(itemRequestMenu.data.name);

        var count: number = ((!activeRequestsCount) ? 0: activeRequestsCount) + ((!assignedToMeCount) ? 0 : assignedToMeCount);

        if (count > 0) {
          itemRequestMenu.badge = {
            text: count.toString(),
            status: 'primary',
          }
        } else {
          itemRequestMenu.badge = {
            text: null
          }
        }
      }
    });
  }

  private loadRequestsMenu(requestTypes: RequestType[]) {
    var requestMenuItem: NbMenuItem = this.menu.filter(item => item.data && item.data.id && item.data.id === 'Requests')[0];

    // remove childs if already initialized saving all requests menu
    var allRequestsMenuItem: NbMenuItem = {
      title: 'allRequests',
      link: '/pages/requests',
      icon: 'list-outline',
    };

    requestMenuItem.children = [];
    requestTypes.filter(item => !item.systemRequest).forEach(requestType => {
      if (Boolean(JSON.parse(requestType.displayOptions.showInMainRequestMenu))) {
        requestMenuItem.children.push({ //requestMenuItem.children.push({
          title: requestType.title,
          icon: {icon: requestType.displayOptions.icon, pack: requestType.displayOptions.iconPack},
          link: '/pages/requests/' + requestType.name,
          data: {name: requestType.name}
        });
      }
    });
    requestMenuItem.children.push(allRequestsMenuItem);
    this.authAndTranslateMenuItem(requestMenuItem); // translate created request menu

  }

  myActiveRequestsCount: Map<string, number> = new Map(); // <requestType.name, count>
  assignedToMeActiveRequestsCount: Map<string, number> = new Map(); // <requestType.name, count>

  ngOnInit(): void {

    this.requestTypeSubscription = this.requestsService.getRequestTypesSubscription().subscribe((requestTypes) => {
      if (requestTypes) { // null at first time
        // 1. we got request items
        this.loadRequestsMenu(requestTypes);
        // 2. make subscription to get active requests to update badges
        this.myActiveRequestsCountSubscription = this.requestsService.getMyActiveRequestsCountSubscription().subscribe(requests => {
          if (requests) { // null at first time
            this.myActiveRequestsCount = new Map();
            requests.forEach(item => {
              this.myActiveRequestsCount.set(item.requestTypeName, item.count);
            });
            this.updateBadges();
          }
        });
        // 3. make subscription to get assigned to me requests to update badges
        this.assignedToMeActiveRequestsCountSubscription = this.requestsService.getAssignedToMeActiveRequestsCountSubscription().subscribe(requests => {
          if (requests) { // null at first time
            this.assignedToMeActiveRequestsCount = new Map();
            requests.forEach(item => {
              this.assignedToMeActiveRequestsCount.set(item.requestTypeName, item.count);
            });
            this.updateBadges();
          }
        });
      }
    });



    this.menu.forEach(item => { //translate and check permissions
      this.authAndTranslateMenuItem(item);
    });

  }

  ngOnDestroy(): void {
    if (this.newUserRequestsSubscription !== null) {
      this.newUserRequestsSubscription.unsubscribe();
    }
    if (this.requestTypeSubscription !== null) {
      this.requestTypeSubscription.unsubscribe();
    }

    if (this.myActiveRequestsCountSubscription !== null) {
      this.myActiveRequestsCountSubscription.unsubscribe();
    }

    if (this.assignedToMeActiveRequestsCountSubscription !== null) {
      this.assignedToMeActiveRequestsCountSubscription.unsubscribe();
    }
  }

  // hiding and activating menu items
  authAndTranslateMenuItem(menuItem: NbMenuItem) {

    const key = menuItem.title;
    const value = this.translateService.translations[this.currentLang].menu[key];
    if (value) {
      menuItem.title = value; // translate menu item
    }
    if (menuItem.data && menuItem.data['permission'] && menuItem.data['resource']) {
      this.accessChecker.isGranted(menuItem.data['permission'], menuItem.data['resource']).subscribe(granted => {
        menuItem.hidden = !granted;
        if (key === 'Users' && granted) { // update badge values
          this.newUserRequestsSubscription = this.requestsService.getActiveNewUserRequestsSubscription().subscribe(
            res => {
              if (res) {
                if (res.content.length > 0) {
                  menuItem.badge = {
                    text: res.content.length.toString(),
                    status: 'primary',
                  }
                } else {
                  menuItem.badge = {
                    text: null,
                  }
                }
              }

            });
        }
      });
    } else {
      menuItem.hidden = true;
    }


    if (!menuItem.hidden && menuItem.children != null) { // menu childs
      menuItem.children.forEach(item => {
        const innerKey = item.title;
        const innerValue = this.translateService.translations[this.currentLang].menu[innerKey];
        if (innerValue) {
          item.title = innerValue; // translate menu item
        }

        if (item.data && item.data['permission'] && item.data['resource']) {
          this.accessChecker.isGranted(item.data['permission'], item.data['resource']).subscribe(granted => {
            item.hidden = !granted;
          });
        } else {
          // if child item do not config any `data.permission` and `data.resource` just inherit parent item's config
          item.hidden = menuItem.hidden;
        }
      });
    }
  }
}
