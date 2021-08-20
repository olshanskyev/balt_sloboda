import { Component, OnInit } from '@angular/core';
import { NbAccessChecker } from '@nebular/security';
import { NbMenuItem } from '@nebular/theme';
import { TranslateService } from '@ngx-translate/core';

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
export class PagesComponent implements OnInit {

  menu = MENU_ITEMS;
  currentLang: string;

  constructor(private accessChecker: NbAccessChecker, private translateService: TranslateService) {

    this.currentLang = this.translateService.currentLang;
  }




  ngOnInit(): void {
    this.menu.forEach(item => {
      this.authMenuItem(item);
    });
  }


  // hiding and activating menu items
  authMenuItem(menuItem: NbMenuItem) {

    const key = menuItem.title;
    const value = this.translateService.translations[this.currentLang].menu[key];
    /*
    if (menuItem.link === '/pages/management/residents') do request
    l[0].badge.text = '2';
    l[0].badge.status = 'primary';
    add funktion to call to change badge
    */
    if (value) {
      menuItem.title = value; // translate menu item
    }

    if (menuItem.data && menuItem.data['permission'] && menuItem.data['resource']) {
      this.accessChecker.isGranted(menuItem.data['permission'], menuItem.data['resource']).subscribe(granted => {
        menuItem.hidden = !granted;
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
