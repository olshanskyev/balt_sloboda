import { Component, Input } from "@angular/core";
import { NbDialogRef } from "@nebular/theme";

@Component({
    selector: 'ngx-resolve-window',
    templateUrl: 'resolve-window.component.html',
    styleUrls: ['resolve-window.component.scss'],
  })

  export class ResolveWindowComponent {

    title: string = '';
    comment: string = '';

    constructor(protected ref: NbDialogRef<ResolveWindowComponent>) {

    }


    close() {
      this.ref.close({
        ok: true,
        comment: this.comment,
      }
      );
    }

    cancel() {
      this.ref.close({
        ok: false,
      })
    }

}
