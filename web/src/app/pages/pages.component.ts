import { Component, Injector, OnDestroy, OnInit } from '@angular/core';
import { NbAccessChecker } from '@nebular/security';
import { NbMenuItem } from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { NewRequestsService } from '../@core/service/new-requests-service';

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
  newRequestsService: NewRequestsService;

  newUserRequestsSubscription :Subscription = null;
  constructor(private accessChecker: NbAccessChecker, private translateService: TranslateService,
    private injector: Injector
    ) {
      accessChecker.isGranted('read', 'requests').subscribe(granted => { // only for admins
        if (granted) {
          this.newRequestsService = this.injector.get(NewRequestsService);
        }
      });

    this.currentLang = this.translateService.currentLang;
  }


  ngOnInit(): void {
    this.menu.forEach(item => {
      this.authMenuItem(item);
    });

  }

  ngOnDestroy(): void {
    if (this.newUserRequestsSubscription !== null) {
      this.newUserRequestsSubscription.unsubscribe();
    }

  }

  // hiding and activating menu items
  authMenuItem(menuItem: NbMenuItem) {

    const key = menuItem.title;
    const value = this.translateService.translations[this.currentLang].menu[key];
    if (value) {
      menuItem.title = value; // translate menu item
    }

    if (menuItem.data && menuItem.data['permission'] && menuItem.data['resource']) {
      this.accessChecker.isGranted(menuItem.data['permission'], menuItem.data['resource']).subscribe(granted => {
        menuItem.hidden = !granted;
        if (menuItem.link === '/pages/management/residents' && granted) { // update badge values
          this.newUserRequestsSubscription = this.newRequestsService.getNewUserRequests().subscribe(
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
    if (!menuItem.hidden && menuItem.children != null) {
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
