import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { loadAPI } from './google-maps-api-loader';
import { keys } from './api_keys';

@Injectable({
  providedIn: 'root'
})
export class GoogleMapsService {
  private geocoder: google.maps.Geocoder;
  private autocomplete: google.maps.places.AutocompleteService;
  private directions: google.maps.DirectionsService;
  private componentRestrictions = { country: 'nl' }

  constructor() {
    loadAPI({
      apiKey: keys.google_maps, region: 'NL', libraries: ['places', 'directions']
    }).then(() => {
      console.log("Loaded GMaps API");

      this.geocoder = new google.maps.Geocoder();
      this.autocomplete = new google.maps.places.AutocompleteService();
      this.directions = new google.maps.DirectionsService();

      console.log("Loaded GMaps Services")
    }).catch(err => console.error("Error loading Google Maps API:\n" + err))
  }

  /**
   * Returns a list of locations
   */
  getPlacePredictions(input: string){
    return new Observable<google.maps.places.AutocompletePrediction[]>(observer => {
      this.autocomplete.getPlacePredictions({ input, componentRestrictions: this.componentRestrictions, types: ['geocode'] }, (results, status) => {
        if (status === google.maps.places.PlacesServiceStatus.OK) {
          observer.next(results);
        } else {
          console.error('Place prediction error:', results, '\nStatus: ', status);
          observer.next([]);
        }
        observer.complete();
      });
    });
  }

  /**
   * Returns the geocode of a given location
   */
  getGeocode(location: string){
    return new Observable(observer => {
      this.geocoder.geocode({'address': location, componentRestrictions: this.componentRestrictions}, (results, status) => {
        if (status === google.maps.GeocoderStatus.OK) {
          observer.next(results[0]);
        } else {
          observer.error(new Error('Geocoding error: '+results+'\nStatus: '+status));
        }
        observer.complete();
      });
    });
  }

  /**
   * Returns the possible directions from the 'origin' to 'destination' locations
   */
  getDirections(origin: string, destination: string, travelMode: google.maps.TravelMode, alternateRoutes: boolean, transitOptions?: google.maps.TransitOptions){
    return new Observable<google.maps.DirectionsResult>(observer => {
      this.directions.route({ origin, destination, travelMode, transitOptions, provideRouteAlternatives: alternateRoutes }, (result, status) => {
        if (status === google.maps.DirectionsStatus.OK) {
          observer.next(result);
        } else {
          observer.error(new Error('Directions error: '+result+'\nStatus: '+status));
        }
        observer.complete();
      })
    })
  }

}

export interface TravelMode {
  name: string,
  value: google.maps.TravelMode
}

export interface TransitMode {
  name: string,
  value: google.maps.TransitMode
}
