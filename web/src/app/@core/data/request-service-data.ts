import { Observable } from 'rxjs';

export abstract class RequestServiceData {
    abstract getAllNewUserRequests(userName: string): Observable<Request[]>;
    abstract acceptRequest(requestId: number): Observable<Request>;
}

export class Request {
    id: number;
    subject: string;
    comment: string;
    paramValues: any;
    type: string;
    status: string;
    createDateTime: Date;
    lastModifiedDate?: Date;
    lastModifiedBy?: number;
    owner: number;
}

export enum RequestStatus {
    NEW,
    ACCEPTED,
    IN_PROGRESS,
    CLOSED,
    REJECTED,
}


