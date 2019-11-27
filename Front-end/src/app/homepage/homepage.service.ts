import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Building, Node } from '../shared/dataclasses';

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
    return this.http.get<Node[]>(this.API_URL + 'locationnodenetwork/room');
  }
}
