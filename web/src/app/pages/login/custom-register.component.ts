import { ChangeDetectorRef, Component, Inject } from '@angular/core';
import { Router } from '@angular/router';
import { NbAuthService, NbRegisterComponent, NB_AUTH_OPTIONS } from '@nebular/auth';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'ngx-register',
  templateUrl: './custom-register.component.html',
})
export class CustomRegisterComponent extends NbRegisterComponent {

  constructor(authService: NbAuthService, @Inject(NB_AUTH_OPTIONS) options = {}, cd: ChangeDetectorRef, router: Router, translate: TranslateService) {
    super(authService, options, cd, router);
  }



}
