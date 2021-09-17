import { Observable } from 'rxjs';
import { CalendarSelectionData } from './calendar-selection.data';
import { User } from './user-service-data';

export abstract class RequestServiceData {
    //requests
    abstract getMyActiveRequestsSubscription(): Observable<Request[]>;
    abstract getAssignedToMeActiveRequestsSubscription(): Observable<Request[]>;
    abstract getNewUserRequestsSubscription(userName: string): Observable<Request[]>;
    abstract getMyRequests(requestStatuses?: RequestStatus[]): Observable<Request[]>;
    abstract getAssignedToMeRequests(requestStatuses?: RequestStatus[]): Observable<Request[]>;
    abstract acceptRequest(requestId: number): Observable<Request>;
    abstract rejectRequest(requestId: number): Observable<Request>;
    abstract createRequest(request: Request): Observable<Request>;
    //request types
    abstract createRequestType(requestType: RequestType): Observable<RequestType>;
    abstract getRequestTypesSubscription():  Observable<RequestType[]>;
    abstract getAllRequestTypes():  Observable<RequestType[]>;
    abstract getRequestTypeByName(name: string): Observable<RequestType>;
    abstract deleteRequestType(id: number): Observable<void>;
    abstract getRequestTypeById(id: number): Observable<RequestType>;
    abstract updateRequestType(id: number, requestType: RequestType): Observable<RequestType>;
}

export class Request {
    id?: number;
    comment: string;
    paramValues: any;
    type: RequestType;
    status?: string;
    calendarSelection?: CalendarSelectionData;
    createDateTime?: Date;
    lastModifiedDate?: Date;
    lastModifiedBy?: User;
    owner?: User;
    assignedTo?: User;
    generatedIdentifier?: string;
}

export class RequestType {
    id: number;
    name: string;
    title: string;
    description?: string;
    durable: boolean;
    roles: Array<Role>;
    assignTo: User;
    parameters?: Array<RequestParam>;
    displayOptions?: any;
    calendarSelection?: CalendarSelectionData;
    systemRequest: boolean;
    requestIdPrefix?: string;
}


export class RequestParam {
    id: number;
    requestType: RequestType;
    name: string;
    type: RequestParamType;
    enumValues?: string[];
    optional: boolean;
    defaultValue?: string;
    comment?: string;
}

export enum RequestParamType{
    INTEGER = 'INTEGER',
    STRING = 'STRING',
    ENUM = 'ENUM',
}

export enum Role {
    ROLE_USER,
    ROLE_ADMIN,
}

export enum RequestStatus {
    NEW,
    ACCEPTED,
    IN_PROGRESS,
    CLOSED,
    REJECTED,
}


