import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { NavigationState } from '../shared/dataclasses';
import { keys } from '../3rdparty/api_keys';
import { OpenrouteserviceService, profiles } from '../3rdparty/openrouteservice.service';
import { tileLayer, latLng, Map, geoJSON } from 'leaflet';
@Component({
  selector: 'app-maps-navigation',
  templateUrl: './maps-navigation.component.html',
  styleUrls: ['./maps-navigation.component.scss']
})
export class MapsNavigationComponent implements OnInit {

  navigationState: NavigationState = { from: '', to: '', departNow: true, time: '' };

  options = {
    layers: [
      tileLayer('https://api.openrouteservice.org/mapsurfer/{z}/{x}/{y}.png?api_key={accessToken}', { accessToken: keys.openrouteservice })
    ],
    zoom: 8,
    center: latLng(51.816139, 4.403112)
  };

  constructor(private router: Router, private routeService: OpenrouteserviceService) {
    const state = this.router.getCurrentNavigation().extras.state;

    if(state == undefined || !Object.keys(this.navigationState).every(prop => state.hasOwnProperty(prop))) this.router.navigate(['/'])
    
    this.navigationState = <NavigationState>state;
  }

  ngOnInit() {
  }

  onMapReady(map: Map){
    this.routeService.getDirection(profiles.car, "4.403112,51.816139", "4.484072,51.917145").subscribe(res => map.addLayer(geoJSON(res as any)))
    
  }

}
