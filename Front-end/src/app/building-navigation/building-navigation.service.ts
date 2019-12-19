import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Building, Address, Node } from '../shared/dataclasses';

@Injectable({
  providedIn: 'root'
})
export class BuildingNavigationService {

  constructor(private http: HttpClient) { }

  private API_URL = "/api/"

  getBuildings() {
    return this.http.get<Building[]>(this.API_URL + "building");
  }

  getAddresses() {
    return this.http.get<Address[]>(this.API_URL + "address");
  }

  getNodes(locationName: string, type: string){
    const params = new HttpParams()
    .set("locationName", locationName)
    .set("type", type);
    return this.http.get<Node[]>(this.API_URL + "locationnodenetwork", { params });
  }
  
}
