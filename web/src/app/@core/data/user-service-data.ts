import { Observable } from 'rxjs';
import { Address } from './addresses-service-data';

export abstract class UserServiceData {
    abstract getUserByName(userName: string): Observable<User>;
    abstract getUserInfo(): Observable<User>;
    abstract getAllUsers(): Observable<User[]>;
}

export class User {
    id: number;
    user: string;
    roles: Array<string>;
    firstName: string;
    lastName: string;
    address: Address;
    picture: string;
}


