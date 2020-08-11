import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

import { Building, LocationRooms, Address, Node } from '../shared/dataclasses';

@Injectable({
  providedIn: 'root'
})
export class HomepageService {

  constructor(private http: HttpClient) { }

  private API_URL = "/api/"

  getBuildings() {
    return this.http.get<Building[]>(this.API_URL + "building");
  }

  getRoomNodes(){
    return this.http.get<LocationRooms[]>(this.API_URL + 'locationnodenetwork/room');
  }

  getBuildingAddress(name: string){
    const params = new HttpParams().set("name", name);
    return this.http.get<Address>(this.API_URL + "address/building", { params });
  }

  getRoomAddress(code: string){
    const params = new HttpParams().set("code", code);
    return this.http.get<Address>(this.API_URL + "address/room", { params });
  }

  getBuildingEntrances(name: string) {
    const params = new HttpParams().set("locationName", name).set("type", "Entrance");
    params.append("Test", "testing");
    console.log(params)

    return this.http.get<Node[]>(this.API_URL + "locationnodenetwork", { params });
  }
}
