import { Observable } from "rxjs";
import { Address } from "./addresses-service-data";
import { User } from "./user-service-data";

export class Resident {
    id: number;
    firstName: string;
    lastName: string;
    address: Address;
    user: User;
}


export abstract class ResidentsServiceData {
    abstract getAllResidents(): Observable<Resident[]>;
}

