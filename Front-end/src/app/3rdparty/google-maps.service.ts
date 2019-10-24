import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { MapsAPILoader } from '@agm/core';

@Injectable({
  providedIn: 'root'
})
export class GoogleMapsService {
  private geocoder: google.maps.Geocoder; // type is 'Geocode' from '@agm/core/map-types' when @agm/core releases an update
  private autocomplete: google.maps.places.AutocompleteService;
  private componentRestrictions = { country: 'nl' }

  constructor(private mapsAPI: MapsAPILoader) {
    mapsAPI.load().then(() => {
      this.geocoder = new google.maps.Geocoder();
      this.autocomplete = new google.maps.places.AutocompleteService();

      console.log("Loaded MapsAPI")
    })
  }

  getPlacePredictions(input: string){
    return new Observable(observer => {
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

}