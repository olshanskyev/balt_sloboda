import { Observable } from 'rxjs';
import { User } from './user-service-data';

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
    roles: Array<Role>
    assignTo: User
    parameters?: Array<RequestParam>
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
    ENUM = 'ENUM'
}

export enum Role {
    ROLE_USER,
    ROLE_ADMIN
}

export enum RequestStatus {
    NEW,
    ACCEPTED,
    IN_PROGRESS,
    CLOSED,
    REJECTED,
}


