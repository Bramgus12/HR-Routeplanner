import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Node } from 'src/app/shared/dataclasses';

@Injectable({
  providedIn: 'root'
})
export class BuildingViewerService {

  constructor(private http: HttpClient) { }

  private API_URL = "/api/"

  getRoute(locationName: string, from: number, to: number){
    const params: HttpParams = new HttpParams()
      .set("from", from.toString())
      .set("to", to.toString())
      .set("locationName", locationName);
    return this.http.get<Node[]>(this.API_URL + "routes", {params: params});
  }
}
