import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl } from '@angular/forms';
import { Observable, interval, forkJoin, of } from 'rxjs';
import { map, startWith, throttleTime, debounceTime, delay } from 'rxjs/operators';

import { HomepageService } from './homepage.service';
import { AppService } from '../app.service'; 
import { NgxMaterialTimepickerTheme } from 'ngx-material-timepicker';
import { NavigationState, Building, TimeMode, TimeModeOption, Address, Node } from '../shared/dataclasses';
import { GoogleMapsService } from '../3rdparty/google-maps.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit {

  navigationModel: NavigationState = { from: '', to: '', departNow: true, timeMode: TimeMode.DEPART_BY, time: '', fromNode: 0, toNode: 0 }
  fromSuggestions: string[] = [];
  toSuggestions: string[] = [];
  buildings: Building[] = [];
  rooms: Node[] = [];
  errorMessage: string = "";
  timeModeOptions: TimeModeOption[] = [
    { name: "Arrival by", value: TimeMode.ARRIVAL_BY },
    { name: "Depart by", value: TimeMode.DEPART_BY }
  ];
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

  @ViewChild('fromInput') fromFormControl: FormControl;
  @ViewChild('toInput') toFormControl: FormControl;

  constructor(private service: HomepageService, private appService: AppService, private mapsService: GoogleMapsService, private router: Router) { }

  ngOnInit() {
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
    };

    this.service.getBuildings().subscribe(data => {
      this.buildings = data;
    }, error => this.errorMessage = "Failed to get buildings from API");

    this.service.getRoomNodes().subscribe(data => {
      this.rooms = data;
    }, error => this.errorMessage = "Failed to get classrooms from API");

    this.fromFormControl.valueChanges.pipe(debounceTime(500)).subscribe((value: string) => {
      if(value.length != 0){
        this.fromSuggestions = this.buildings.map(val => val.name).filter(val => val.toLowerCase().includes(value.toLowerCase()));
        this.fromSuggestions = this.fromSuggestions.concat(this.rooms.map(val => val.code).filter(val => val.toLowerCase().includes(value.toLowerCase())));

        // Only check if suggetions is empty
        if(this.fromSuggestions.length == 0) this.mapsService.getPlacePredictions(value).subscribe(result => this.fromSuggestions = result.map(val => val.description));
        this.fromSuggestions.sort();

        if(this.rooms.filter(room => room.code == value).length > 0){
          this.navigationModel.fromNode = this.rooms.find(({ code }) => code == value).number;
        } else if(this.navigationModel.hasOwnProperty("fromNode")) this.navigationModel.fromNode = null;
      } else {
        this.navigationModel.from = value;
        this.fromSuggestions = [];

        if(this.navigationModel.hasOwnProperty("fromNode")) this.navigationModel.fromNode = null;
      }
    });

    this.toFormControl.valueChanges.subscribe((value: string) => {
      if(value.length != 0){
        this.toSuggestions = this.buildings.map(val => val.name).filter(val => val.toLowerCase().includes(value.toLowerCase()))
        this.toSuggestions = this.toSuggestions.concat(this.rooms.map(val => val.code).filter(val => val.toLowerCase().includes(value.toLowerCase())));
        this.toSuggestions.sort();

        if(this.rooms.filter(room => room.code == value).length > 0){
          this.navigationModel.toNode = this.rooms.find(({ code }) => code == value).number;
        } else if(this.navigationModel.hasOwnProperty("toNode")) this.navigationModel.toNode = null;
      } else {
        this.navigationModel.to = value;
        this.toSuggestions = [];

        if(this.navigationModel.hasOwnProperty("toNode")) this.navigationModel.toNode = null;
      }
    })
  }

  /**
   * Gets called when the "Depart now" slide-toggle is changed
   */
  departNowUpdate(departNow: boolean){
    if(!departNow) this.navigationModel.time = new Date().toLocaleTimeString(undefined, {
      hour: '2-digit',
      minute:'2-digit'
    });
  }

  /**
   * Gets called when #navigationForm is submitted
   */
  goToNavigation(){
    const retrieveAddresses: Observable<Address>[] = [];
    let loadBuildingNav = false,
      componentUrl = 'maps-navigation';

    this.errorMessage = "";

    if(this.navigationModel.from == this.navigationModel.to){
      this.errorMessage = "Seems like you're travelling to the same place where you want to start";
      return;
    }
    
    if(this.buildings.filter(building => building.name == this.navigationModel.to).length > 0){
      retrieveAddresses.push(this.service.getBuildingAddress(this.navigationModel.to));
    } else if(this.rooms.filter(room => room.code == this.navigationModel.to).length > 0){
      retrieveAddresses.push(this.service.getRoomAddress(this.navigationModel.to));
    } else {
      this.errorMessage = "'To classroom / building' field has to be a building name or room code";
      return;
    } 

    if(this.buildings.filter(building => building.name == this.navigationModel.from).length > 0){
      retrieveAddresses.push(this.service.getBuildingAddress(this.navigationModel.from));
    } else if(this.rooms.filter(room => room.code == this.navigationModel.from).length > 0){
      retrieveAddresses.push(this.service.getRoomAddress(this.navigationModel.from));
      loadBuildingNav = true;
    }
    
    forkJoin(retrieveAddresses).subscribe(results => {
      const toAddr = results[0];
      this.navigationModel.to = `${toAddr.street} ${toAddr.number}, ${toAddr.city}`;

      if(results.length == 2){
        const fromAddr = results[1];
        this.navigationModel.from = `${fromAddr.street} ${fromAddr.number}, ${fromAddr.city}`;
      }

      if(loadBuildingNav || this.navigationModel.to == this.navigationModel.from){
        componentUrl = 'building-navigation';
      }

      /* Ways of sending data to other components (no data-binding) using states!
      https://stackoverflow.com/a/54365098
      Other alternatives: https://stackoverflow.com/a/44865817 */
      this.router.navigate([componentUrl], { state: this.navigationModel })
    }, error => this.errorMessage = "Failed to get the addresses from the API")
  }

}
