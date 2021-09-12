import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { RequestService } from "./request-service";

@Injectable()
export class NewUserRequestsInterconnectionService {

   private _newUserRequestsSource = new BehaviorSubject<number>(0);
   private newUserRequests$: Observable<number> = this._newUserRequestsSource.asObservable();


    public changeNewUserRequestsCount(count: number) {
        this._newUserRequestsSource.next(count);
    }

    public getNewUserRequests(): Observable<number> {
        return this.newUserRequests$;
    }

    constructor(requestsService: RequestService) { // preload for menu icons
        requestsService.getAllNewUserRequests().subscribe (res => {
            this.changeNewUserRequestsCount(res.length);
        });
    }


}