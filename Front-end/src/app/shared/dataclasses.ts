export interface Address {
  id?: number;
  street: string;
  number: number;
  city: string;
  postal: string;
}

export interface NavigationState {
  from: string,
  to: string,
  departNow: boolean,
  time: string
}
