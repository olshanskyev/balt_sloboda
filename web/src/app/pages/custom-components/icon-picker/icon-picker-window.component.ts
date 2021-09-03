import { Component } from "@angular/core";
import { NbDialogRef, NbIconLibraries } from "@nebular/theme";

@Component({
    selector: 'ngx-icon-picker-window',
    templateUrl: 'icon-picker-window.component.html',
    styleUrls: ['icon-picker-window.component.scss'],
  })

  export class IconPickerWindowComponent {

    selectedIcon: string;
    evaIcons = [];
    constructor(protected ref: NbDialogRef<IconPickerWindowComponent>,
      iconsLibrary: NbIconLibraries) {

      this.evaIcons = Array.from(iconsLibrary.getPack('eva').icons.keys());

    }

    close() {
        this.ref.close(this.selectedIcon);
    }

  }
