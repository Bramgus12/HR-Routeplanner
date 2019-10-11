export interface Address {
  id?: number;
  street: string;
  number: number;
  city: string;
  postal: string;
}

// Bad?
/*export interface Building {
  id?: number;
  address_id: number;
  name: string;
}*/
//Better
export interface Building {
  id?: number;
  address: Address;
  institute: Institute;
  name: string;
}

export interface Institute {
  id?: number;
  name: string;
}

