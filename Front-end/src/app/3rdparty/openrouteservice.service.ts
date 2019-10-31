import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { keys } from './api_keys';
import { FeatureCollection, Point, LineString, BBox } from 'geojson'; 
import { latLngBounds } from 'leaflet';

@Injectable({
  providedIn: 'root'
})
export class OpenrouteserviceService {

  constructor(private http: HttpClient) { }

  private API_URL = "https://api.openrouteservice.org";
  private API_KEY = keys.openrouteservice;

  getDirection(profile: string, fromCoordinate: string, toCoordinate: string){
    return this.http.get<FeatureCollection<LineString>>(this.API_URL + '/v2/directions/' + profile, { params: { api_key: this.API_KEY, start: fromCoordinate, end: toCoordinate } })
  }

  searchGeocode(text: string){
    return this.http.get<FeatureCollection<Point>>(this.API_URL + '/geocode/search', { params: { api_key: this.API_KEY, text } })
  }

  getBoundsFromBBox(bbox: BBox){
    return latLngBounds([bbox[1], bbox[0]], [bbox[3], bbox[2]]);
  }

}

export const PROFILES = {
  car: 'driving-car',
  hgv: 'driving-hgv',
  walking: 'foot-walking',
  hiking: 'foot-hiking',
  cycling:'cycling-regular',
  roadCycling: 'cycling-road',
  mountainCycling: 'cycling-mountain',
  electricCycling: 'cycling-electric',
  wheelchair: 'wheelchair'
}

/* TODO: find out if this is needed, or even complete */
interface GeocodeSearchPropeties {
  id: string,
  gid: string,
  layer: string,
  source: string,
  source_id: string,
  name: string,
  street: string,
  confidence: number,
  match_type: string,
  accuracy: string,
  country: string,
  country_gid: string,
  country_a: string,
  region: string,
  region_gid: string,
  region_a: string,
  localadmin: string,
  localadmin_gid: string,
  locality: string,
  locality_gid: string,
  neighbourhood: string,
  neighbourhood_gid: string,
  continent: string,
  continent_gid: string,
  label: string
}
