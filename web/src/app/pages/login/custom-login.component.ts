import { ChangeDetectorRef, Component, Inject } from '@angular/core';
import { Router } from '@angular/router';
import { NbAuthService, NbLoginComponent, NB_AUTH_OPTIONS } from '@nebular/auth';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'ngx-login',
  templateUrl: './custom-login.component.html',
})
export class CustomLoginComponent extends NbLoginComponent {

  constructor(authService: NbAuthService, @Inject(NB_AUTH_OPTIONS) options = {},
    cd: ChangeDetectorRef, router: Router, translate: TranslateService) {
    super(authService, options, cd, router);
  }
}
