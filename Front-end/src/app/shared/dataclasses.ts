export interface Address {
  id?: number;
  street: string;
  number: number;
  city: string;
  postal: string;
}

export interface Building {
  id?: number,
  address_id: number,
  name: string
}

export interface NavigationState {
  from: string,
  to: string,
  departNow: boolean,
  time: string
}

export class GeoLocation {
  latitute: number;
  longtitute: number;

  constructor(lat: number, long: number){
    this.latitute = lat;
    this.longtitute = long;
  }

  toString(): string{
    return this.latitute + ',' + this.longtitute;
  }
}
