import { Observable } from 'rxjs';

export abstract class UserServiceData {
    abstract getAllUsers(): Observable<User[]>;
    abstract getUserByName(userName: string): Observable<User>;
    abstract getUserInfo(): Observable<User>;
}

export class User {
    id: number;
    userName: string;
    roles: Array<string>;
    picture: string;
}


