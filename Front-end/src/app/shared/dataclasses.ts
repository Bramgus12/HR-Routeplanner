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
  timeMode: TimeMode,
  time: string
}

export enum TimeMode {
  ARRIVAL_BY,
  DEPART_BY
}

export interface TimeModeOption {
  name: string,
  value: TimeMode
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
