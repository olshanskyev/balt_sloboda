import { Observable } from 'rxjs';

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

export class Address {
    id: number;
    street: string;
    houseNumber: number;
    plotNumber: number;
}
