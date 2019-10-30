import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Building } from '../shared/dataclasses';

@Injectable({
  providedIn: 'root'
})
export class HomepageService {

  constructor(private http: HttpClient) { }

  private API_URL = "/api/"

  getBuildings() {
    return this.http.get<Building[]>(this.API_URL + "building");
  }
}
