import { Component} from '@angular/core';

import {
  NbToastrService,
} from '@nebular/theme';
import { Toaster } from '../Toaster';


@Component({
  selector: 'ngx-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.scss'],
})


export class NewsComponent {

  private toaster: Toaster;

  constructor(private toastrService: NbToastrService) {
    this.toaster = new Toaster(toastrService);
  }

}
