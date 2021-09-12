import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { RequestService } from "./request-service";

@Injectable()
export class InterconnectionService {

   private _requestsListChangedSource = new BehaviorSubject<void>(null);
   private requestsListChanged: Observable<void> = this._requestsListChangedSource.asObservable();

    public notifyRequestsListChanged() {
        this._requestsListChangedSource.next();
    }

    public getRequestsListChanged(): Observable<void> {
        return this.requestsListChanged;
    }

    constructor() {
    }


}