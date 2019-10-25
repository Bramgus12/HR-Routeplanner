export interface Address {
  id?: number;
  street: string;
  number: number;
  city: string;
  postal: string;
}

export interface NavigationState {
  from: google.maps.places.AutocompletePrediction,
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
