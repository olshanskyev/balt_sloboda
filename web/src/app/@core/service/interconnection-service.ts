import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { RequestService } from "./request-service";

@Injectable()
export class InterconnectionService {

   private _newUserRequestsSource = new BehaviorSubject<number>(0);
   private newUserRequests$: Observable<number> = this._newUserRequestsSource.asObservable();

   private _requestsListChangedSource = new BehaviorSubject<void>(null);
   private requestsListChanged: Observable<void> = this._requestsListChangedSource.asObservable();




    public changeNewUserRequestsCount(count: number) {
        this._newUserRequestsSource.next(count);
    }

    public getNewUserRequests(): Observable<number> {
        return this.newUserRequests$;
    }

    public notifyRequestsListChanged() {
        this._requestsListChangedSource.next();
    }

    public getRequestsListChanged(): Observable<void> {
        return this.requestsListChanged;
    }

    constructor(requestsService: RequestService) { // preload for menu icons
        requestsService.getAllNewUserRequests().subscribe (res => {
            this.changeNewUserRequestsCount(res.length);
        });
    }


}