import { ToasterConfig } from 'angular2-toaster';
import { NbGlobalPhysicalPosition, NbGlobalPosition, NbComponentStatus, NbToastrService } from '@nebular/theme';

export class Toaster {

    constructor(private toastrService: NbToastrService) {
    }

    config: ToasterConfig;

    index = 1;
    destroyByClick = true;
    duration = 5000;
    hasIcon = true;
    position: NbGlobalPosition = NbGlobalPhysicalPosition.TOP_RIGHT;
    preventDuplicates = false;
    status: NbComponentStatus = 'primary';

    title = 'HI there!';
    content = `I'm cool toaster!`;

    types: NbComponentStatus[] = [
        'primary',
        'success',
        'info',
        'warning',
        'danger',
    ];

    public showToast(type: NbComponentStatus, title: string, body: string) {
        const config = {
        status: type,
        destroyByClick: this.destroyByClick,
        duration: this.duration,
        hasIcon: this.hasIcon,
        position: this.position,
        preventDuplicates: this.preventDuplicates,
        };
        const titleContent = title ? `${title}` : '';

        this.index += 1;
        this.toastrService.show(
        body,
        `${titleContent}`,
        config);
    }
}
