import { Component, Injector, OnDestroy, OnInit } from '@angular/core';
import { NbAccessChecker } from '@nebular/security';
import { NbIconLibraries, NbMenuItem } from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { InterconnectionService } from '../@core/service/interconnection-service copy';
import { NewUserRequestsInterconnectionService } from '../@core/service/new-user-request-interconnection-service';
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
  newUserRequestInterconnectionService: NewUserRequestsInterconnectionService;

  requestsDisplayOptions: any[];

  newUserRequestsSubscription :Subscription = null;
  requestListChangedSubscription :Subscription = null;

  constructor(private accessChecker: NbAccessChecker, private translateService: TranslateService,
    private injector: Injector,
    private requestsService: RequestService,
    private interconnectionService: InterconnectionService,
    iconsLibrary: NbIconLibraries,
    ) {
      accessChecker.isGranted('read', 'newUserRequests').subscribe(granted => { // only for admins
        if (granted) {
          this.newUserRequestInterconnectionService = this.injector.get(NewUserRequestsInterconnectionService);
        }
      });

    this.currentLang = this.translateService.currentLang;
    iconsLibrary.registerFontPack('ion', { iconClassPrefix: 'ion' });
  }


  private loadRequestsMenu() {
    var requestMenuItem: NbMenuItem = this.menu.filter(item => item.data && item.data.id && item.data.id === 'Requests')[0];

    // remove childs if already initialized saving all requests menu
    var allRequestsMenuItem: NbMenuItem = {
      title: 'allRequests',
      link: '/pages/requests',
      icon: 'list-outline',
    };
    requestMenuItem.children = [];

    this.requestsService.getAllUserRequestTypes().subscribe(res => {
      res.filter(item => !item.systemRequest).forEach(requestType => {
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
    });

  }

  ngOnInit(): void {

    // always called on initialization without emit
    this.requestListChangedSubscription = this.interconnectionService.getRequestsListChanged().subscribe(() => {
      this.loadRequestsMenu();
    });

    this.menu.forEach(item => { //translate and check permissions
      this.authAndTranslateMenuItem(item);
    });

  }

  ngOnDestroy(): void {
    if (this.newUserRequestsSubscription !== null) {
      this.newUserRequestsSubscription.unsubscribe();
    }
    if (this.requestListChangedSubscription !== null) {
      this.requestListChangedSubscription.unsubscribe();
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
          this.newUserRequestsSubscription = this.newUserRequestInterconnectionService.getNewUserRequests().subscribe(
            res => {
              if (res > 0) {
                menuItem.badge = {
                  text: res.toString(),
                  status: 'primary',
                }
              } else {
                menuItem.badge = {
                  text: null,
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
