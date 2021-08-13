import { Observable } from "rxjs";


export abstract class AddressesServiceData {
    abstract getAllStreets(): Observable<string[]>;
    abstract getAddressesOnStreet(street: string): Observable<Address[]>;
}

export class Address {
    id: number;
    street: string;
    houseNumber: number;
    plotNumber: number;
}