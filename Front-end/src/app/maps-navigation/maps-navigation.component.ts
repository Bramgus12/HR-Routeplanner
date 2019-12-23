import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AppService } from '../app.service';
import { keys } from '../3rdparty/api_keys';
import { GoogleMapsService, TravelMode, TransitMode } from '../3rdparty/google-maps.service';
import { NgxMaterialTimepickerTheme } from 'ngx-material-timepicker';
import { TimeMode, TimeModeOption } from '../shared/dataclasses';
import { NavigationState, MapsStep } from '../shared/navigation-state';

@Component({
  selector: 'app-maps-navigation',
  templateUrl: './maps-navigation.component.html',
  styleUrls: ['./maps-navigation.component.scss']
})
export class MapsNavigationComponent implements OnInit {

  navigationState: NavigationState;
  stateData: MapsStep;
  directions: google.maps.DirectionsStep[] = [];
  travelModes: TravelMode[] = [];
  transitModes: TransitMode[] = [];
  travelMode: google.maps.TravelMode;
  transitMode: google.maps.TransitMode[] = [];
  timeModeOptions: TimeModeOption[] = [
    { name: "Arrival by", value: TimeMode.ARRIVAL_BY },
    { name: "Depart by", value: TimeMode.DEPART_BY }
  ];
  timeInfo = "";
  fastestRoute = false;
  timepickerTheme: NgxMaterialTimepickerTheme = {
    container: {
      buttonColor: '#d32f2f'
    },
    dial: {
      dialBackgroundColor: '#d32f2f',
    },
    clockFace: {
      clockHandColor: '#d32f2f',
    }
  };

  lat = 51.917218;
  lng = 4.48405;
  zoom = 16;

  private directionsRenderer: google.maps.DirectionsRenderer;

  constructor(private router: Router, private googleMapsService: GoogleMapsService, private appService: AppService/*, private routeService: OpenrouteserviceService*/) {
    const state = this.router.getCurrentNavigation().extras.state;

    if(state == undefined || !(state instanceof NavigationState)) {
      this.router.navigate(['/']);
      return;
    }
    
    try {
      this.navigationState = <NavigationState>state;
      const currentStep = this.navigationState.getNextStep('maps-navigation');
      this.stateData = <MapsStep>currentStep.data;
    } catch(err) {
      console.error(err);
      this.router.navigate(['/']);
    }
  }

  ngOnInit(){
    // Init google stuff here, google isn't loaded in before this
    this.directionsRenderer = new google.maps.DirectionsRenderer();

    this.travelModes = [
      { name: "Driving", value: google.maps.TravelMode.DRIVING },
      { name: "Bicycling", value: google.maps.TravelMode.BICYCLING },
      { name: "Transit", value: google.maps.TravelMode.TRANSIT },
      { name: "Walking", value: google.maps.TravelMode.WALKING }
    ];
    this.transitModes = [
      { name: "Bus", value: google.maps.TransitMode.BUS },
      { name: "Rail", value: google.maps.TransitMode.RAIL },
      { name: "Subway", value: google.maps.TransitMode.SUBWAY },
      { name: "Train", value: google.maps.TransitMode.TRAIN },
      { name: "Tram", value: google.maps.TransitMode.TRAM }
    ]
    this.travelMode = google.maps.TravelMode.TRANSIT;

    if(this.appService.darkMode) this.timepickerTheme = {
      container: {
        bodyBackgroundColor: '#303030',
        buttonColor: '#d32f2f'
      },
      dial: {
        dialBackgroundColor: '#d32f2f',
      },
      clockFace: {
        clockFaceBackgroundColor: '#424242',
        clockHandColor: '#d32f2f',
        clockFaceTimeInactiveColor: '#fff'
      }
    }
  }

  /**
   * Gets called when google maps is loaded into the html DOM
   */
  onMapReady(map: google.maps.Map){
    this.directionsRenderer.setMap(map);
    this.getDirections();
  }

  /**
   * Gets called when the "Depart now" slide-toggle changed
   */
  onDepartNowChange(){
    if(!this.stateData.departNow) this.stateData.time = new Date().toLocaleTimeString(undefined, {
      hour: '2-digit',
      minute:'2-digit'
    });

    this.getDirections();
  }

  /**
   * Loads the directions from google maps
   */
  getDirections(){
    const transitOptions: google.maps.TransitOptions = { modes: this.transitMode };

    if(!this.stateData.departNow){
      const time = this.stateData.time.split(':'),
        dateTime = new Date();
      dateTime.setHours(parseInt(time[0]));
      dateTime.setMinutes(parseInt(time[1]));

      if(this.stateData.timeMode == TimeMode.ARRIVAL_BY) transitOptions.arrivalTime = dateTime;
      else if(this.stateData.timeMode == TimeMode.DEPART_BY) transitOptions.departureTime = dateTime;
    }

    this.googleMapsService.getDirections(this.navigationState.from, this.navigationState.to, this.travelMode, this.fastestRoute, transitOptions).subscribe(data => {
      this.directionsRenderer.setDirections(data);

      let route : google.maps.DirectionsRoute;
      if(this.fastestRoute){
        const fastestRoute = this.getFastestRoute(data.routes);
        this.directionsRenderer.setRouteIndex(fastestRoute.index);
        route = fastestRoute.route;
      } else {
        route = data.routes[0];
      }

      const firstLeg = route.legs[0];
      this.directions = firstLeg.steps;

      if(this.travelMode == google.maps.TravelMode.TRANSIT && firstLeg.hasOwnProperty("departure_time") && firstLeg.hasOwnProperty("arrival_time")) {
        this.timeInfo = "Departure: " + firstLeg.departure_time.text + " - Arrival: " + firstLeg.arrival_time.text;
      } else if(firstLeg.hasOwnProperty("duration")){
        this.timeInfo = "Duration: " + firstLeg.duration.text;
      } else {
        this.timeInfo = "No info found";
      }
    })
  }

  getFastestRoute(routes: google.maps.DirectionsRoute[]){
    const fastestTime = routes.reduce((fastestTime, route) => route.legs[0].duration.value < fastestTime ? route.legs[0].duration.value : fastestTime, Number.MAX_SAFE_INTEGER);
    let index = routes.findIndex(val => val.legs[0].duration.value == fastestTime);
    index = index > -1 ? index : 0;

    return { route: routes[index], index };
  }

  /**
   * Returns the pixel position based on the type of direction maneuver
   */
  getIconPosition(direction: google.maps.DirectionsStep) {
    if(this.appService.darkMode){
      switch (direction["maneuver"]) {
        case "turn-sharp-left":
          return "-191px";
          break;
        case "uturn-right":
          return "-321px";
          break;
        case "turn-slight-right":
          return "-86px";
          break;
        case "merge":
          return "-268px";
          break;
        case "roundabout-left":
          return "-248px";
          break;
        case "roundabout-right":
          return "-124px";
          break;
        case "uturn-left":
          return "-464px";
          break;
        case "turn-slight-left":
          return "-446px";
          break;
        case "turn-left":
          return "-16px";
          break;
        case "ramp-right":
          return "-341px";
          break;
        case "turn-right":
          return "-68px";
          break;
        case "fork-right":
          return "-214px";
          break;
        case "straight":
          return "-105px";
          break;
        case "fork-left":
          return "-287px";
          break;
        case "ferry-train":
          return "-395px";
          break;
        case "turn-sharp-right":
          return "-179px";
          break;
        case "ramp-left":
          return "-516px";
          break;
        case "ferry":
          return "-360px";
          break;

        default:
          return "-105px";
      }
    } else {
      switch (direction["maneuver"]) {
        case "turn-sharp-left":
          return "0px";
          break;
        case "uturn-right":
          return "-34px";
          break;
        case "turn-slight-right":
          return "-51px";
          break;
        case "merge":
          return "-142px";
          break;
        case "roundabout-left":
          return "-196px";
          break;
        case "roundabout-right":
          return "-231px";
          break;
        case "uturn-left":
          return "-304px";
          break;
        case "turn-slight-left":
          return "-378px";
          break;
        case "turn-left":
          return "-414px";
          break;
        case "ramp-right":
          return "-430px";
          break;
        case "turn-right":
          return "-483px";
          break;
        case "fork-right":
          return "-499px";
          break;
        case "straight":
          return "-533px";
          break;
        case "fork-left":
          return "-549px";
          break;
        case "ferry-train":
          return "-566px";
          break;
        case "turn-sharp-right":
          return "-583px";
          break;
        case "ramp-left":
          return "-598px";
          break;
        case "ferry":
          return "-614px";
          break;
        
        default:
          return "-533px";
      }
    }
    
  }

}
