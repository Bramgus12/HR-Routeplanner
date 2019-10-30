import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { NavigationState } from '../shared/dataclasses';
import { keys } from '../3rdparty/api_keys';
// import { OpenrouteserviceService, PROFILES } from '../3rdparty/openrouteservice.service';
// import { tileLayer, latLng, Map, geoJSON, latLngBounds } from 'leaflet';
import { GoogleMapsService } from '../3rdparty/google-maps.service'

@Component({
  selector: 'app-maps-navigation',
  templateUrl: './maps-navigation.component.html',
  styleUrls: ['./maps-navigation.component.scss']
})
export class MapsNavigationComponent implements OnInit {

  navigationState: NavigationState = { from: null, to: null, departNow: true, time: '' };
  directions: google.maps.DirectionsStep[] = [];

  lat = 51.917218;
  lng = 4.48405;
  zoom = 16;

  /*options = {
    layers: [
      tileLayer('https://api.openrouteservice.org/mapsurfer/{z}/{x}/{y}.png?api_key={accessToken}', { accessToken: keys.openrouteservice })
    ],
    zoom: 15,
    center: latLng(51.917218, 4.48405)
  };*/

  constructor(private router: Router, private googleMapsService: GoogleMapsService/*, private routeService: OpenrouteserviceService*/) {
    const state = this.router.getCurrentNavigation().extras.state;

    if(state == undefined || !Object.keys(this.navigationState).every(prop => state.hasOwnProperty(prop))) this.router.navigate(['/'])
    
    this.navigationState = <NavigationState>state;
  }

  ngOnInit() {
  }

  /*onMapReady(map: Map){
    this.routeService.getDirection(PROFILES.car, "4.403112,51.816139", "4.484072,51.917145").subscribe(res => {
      map.addLayer(geoJSON(res));
      map.flyToBounds(this.routeService.getBoundsFromBBox(res.bbox), { duration: 1 });
    })

    this.routeService.searchGeocode("Wijnhaven 107").subscribe(res => {
      console.log(res.features)
      for (const feature of res.features){
        const name = feature.properties["label"]
        const coords = feature.geometry.coordinates
        console.log(name, coords)
      }
    })
  }*/

  onMapReady(map: google.maps.Map){
    const directionsRenderer = new google.maps.DirectionsRenderer();
    directionsRenderer.setMap(map);

    //this.googleMapsService.getDirections(this.navigationState.from, this.navigationState.to, google.maps.TravelMode.DRIVING).subscribe(data => {
    this.googleMapsService.getDirections('Frans Halsstraat 6, Oud-Beijerland', 'Wijnhaven 107, Rotterdam', google.maps.TravelMode.DRIVING).subscribe(data => {
      directionsRenderer.setDirections(data);
      this.directions = data.routes[0].legs[0].steps;
      console.log(data.routes[0].legs[0].steps);
    })
  }

}
