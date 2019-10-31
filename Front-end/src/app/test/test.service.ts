import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Address } from '../shared/dataclasses'; 

@Injectable({
  providedIn: 'root'
})
export class TestService {

  constructor(private http: HttpClient) { }

  // temp api base url
  private API_URL = "/api/"

  getAddresses() {
    return this.http.get<Address[]>(this.API_URL + "address");
  }

  createAddress(address: Address){
    return this.http.post(this.API_URL + "address", address);
  }

  updateAddress(id: number, address: Address){
    return this.http.put(this.API_URL + "address/" + id, address);
  }

  deleteAddress(id: number){
    return this.http.delete(this.API_URL + "address/" + id);
  }
}
