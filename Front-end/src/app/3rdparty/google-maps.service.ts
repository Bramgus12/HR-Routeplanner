import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { keys } from './api_keys';

@Injectable({
  providedIn: 'root'
})
export class GoogleMapsService {

  constructor(private http: HttpClient) { }

  private API_URL = "";
  private API_KEY = keys.google_maps;
}
