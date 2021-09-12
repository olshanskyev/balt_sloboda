import { Observable } from 'rxjs';
import { CalendarSelectionData } from './calendar-selection.data';
import { User } from './user-service-data';

export abstract class RequestServiceData {
    abstract getAllNewUserRequests(userName: string): Observable<Request[]>;
    abstract acceptRequest(requestId: number): Observable<Request>;
    abstract createRequestType(requestType: RequestType): Observable<RequestType>;
    abstract getAllUserRequestTypes():  Observable<RequestType[]>;
    abstract getAllRequestTypes():  Observable<RequestType[]>;
    abstract getRequestTypeByName(name: string): Observable<RequestType>;
    abstract deleteRequestType(id: number): Observable<void>;
    abstract getRequestTypeById(id: number): Observable<RequestType>;
    abstract updateRequestType(id: number, requestType: RequestType): Observable<RequestType>;
}

export class Request {
    id: number;
    comment: string;
    paramValues: any;
    type: RequestType;
    status: string;
    selectedDays?: Array<string>;
    createDateTime: Date;
    lastModifiedDate?: Date;
    lastModifiedBy?: User;
    owner: User;
    assignedTo: User;
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


