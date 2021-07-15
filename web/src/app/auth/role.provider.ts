import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { map } from 'rxjs/operators/map';
import { NbAuthService, NbAuthOAuth2JWTToken } from '@nebular/auth';
import { NbRoleProvider } from '@nebular/security';

@Injectable()
export class RoleProvider implements NbRoleProvider {

  constructor(private authService: NbAuthService) {
  }

  getRole(): Observable<string[]> {
    //const roles: string[] = ['ROLE_ADMIN'];
    return this.authService.onTokenChange()
      .pipe(
        map((token: NbAuthOAuth2JWTToken) => {

          if (token.isValid() && token instanceof NbAuthOAuth2JWTToken) {
            const roles: string[] = token.getAccessTokenPayload()['roles'];
            return roles;
          } else {
            return ['ROLE_USER'];
          }

        }),
      );
  }
}
