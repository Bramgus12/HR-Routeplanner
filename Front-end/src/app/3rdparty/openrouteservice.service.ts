import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { keys } from './api_keys';

@Injectable({
  providedIn: 'root'
})
export class OpenrouteserviceService {

  constructor(private http: HttpClient) { }

  private API_URL = "https://api.openrouteservice.org";
  private API_KEY = keys.openrouteservice;

  getDirection(profile: string, fromCoordinate: string, toCoordinate: string){
    return this.http.get(this.API_URL + '/v2/directions/' + profile, { params: { api_key: this.API_KEY, start: fromCoordinate, end: toCoordinate } })
  }

  getCoordinate(text: string){
    return this.http.get<CoordinateResponse>('/geocode/search', { params: { api_key: this.API_KEY, text } })
  }

}

export const profiles = {
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

interface DirectionsResponse {
  routes: {
    summary: {
      distance: number,
      duration: number
    },
    segments: {
      distance: number,
      duration: number,
      steps: {
        distance: number,
        duration: number,
        type: number,
        instruction: string,
        name: string,
        way_points: number[]
      }[]
    }[],
    bbox: number[],
    geometry: string,
    way_points: number[]
  }[],
  bbox: number[]
}

interface CoordinateResponse {
  features: {
    type: string,
    geometry: {
      type: string,
      coordinates: number[],
      properties: {
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
    }
  }[],
  bbox: number[]
}
