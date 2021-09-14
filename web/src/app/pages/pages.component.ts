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
  requestTypeListChangedSubscription :Subscription = null;
  requestsListChangedSubscription: Subscription = null;


  constructor(private accessChecker: NbAccessChecker, private translateService: TranslateService,

    private requestsService: RequestService,
    iconsLibrary: NbIconLibraries,
    ) {
    this.currentLang = this.translateService.currentLang;
    iconsLibrary.registerFontPack('ion', { iconClassPrefix: 'ion' });
  }


  private loadRequestsMenu(requestTypes: RequestType[]) {
    var requestMenuItem: NbMenuItem = this.menu.filter(item => item.data && item.data.id && item.data.id === 'Requests')[0];

    // remove childs if already initialized saving all requests menu
    var allRequestsMenuItem: NbMenuItem = {
      title: 'allRequests',
      link: '/pages/requests',
      icon: 'list-outline',
    };

    //this.requestsService.getAllUserRequestTypesSubscription().subscribe(requestTypes => {
      requestMenuItem.children = [];
      requestTypes.filter(item => !item.systemRequest).forEach(requestType => {
        if (Boolean(JSON.parse(requestType.displayOptions.showInMainRequestMenu))) {
          requestMenuItem.children.push({ //requestMenuItem.children.push({
            title: requestType.title,
            icon: {icon: requestType.displayOptions.icon, pack: requestType.displayOptions.iconPack},
            link: '/pages/requests/' + requestType.name,
          });
        }
      });
      requestMenuItem.children.push(allRequestsMenuItem);
      this.authAndTranslateMenuItem(requestMenuItem); // translate created request menu
    //});

  }

  ngOnInit(): void {

    this.requestTypeListChangedSubscription = this.requestsService.getAllUserRequestTypesSubscription().subscribe((requestTypes) => {
      if (requestTypes) // null at first time
        this.loadRequestsMenu(requestTypes);
    });

    this.requestsListChangedSubscription = this.requestsService.getAllUserActiveRequestsSubscription().subscribe(requests => {
      if (requests) { // null at first time
        ;// ToDo update badges
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
    if (this.requestTypeListChangedSubscription !== null) {
      this.requestTypeListChangedSubscription.unsubscribe();
    }

    if (this.requestsListChangedSubscription !== null) {
      this.requestsListChangedSubscription.unsubscribe();
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
          this.newUserRequestsSubscription = this.requestsService.getAllNewUserRequestsSubscription().subscribe(
            res => {
              if (res) {
                if (res.length > 0) {
                  menuItem.badge = {
                    text: res.length.toString(),
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
