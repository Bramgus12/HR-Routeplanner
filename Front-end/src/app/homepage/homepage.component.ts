import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl } from '@angular/forms';
import { Observable, interval } from 'rxjs';
import { map, startWith, throttleTime, debounceTime } from 'rxjs/operators';

import { HomepageService } from './homepage.service';
import { AppService } from '../app.service'; 
import { NgxMaterialTimepickerTheme } from 'ngx-material-timepicker';
import { NavigationState, Building, TimeMode, TimeModeOption } from '../shared/dataclasses';
import { GoogleMapsService } from '../3rdparty/google-maps.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit {

  navigationModel: NavigationState = { from: '', to: '', departNow: true, timeMode: TimeMode.DEPART_BY, time: '' }
  fromSuggestions: string[] = [];
  toSuggestions: string[] = [];
  buildings: string[] = [];
  rooms: string[] = [];
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

  //Temp
  componentUrl = 'building-navigation';

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
      this.buildings = data.map(val => val.name)
    });

    this.service.getRoomNodes().subscribe(data => {
      this.rooms = data.map(val => val.code);
    });

    this.fromFormControl.valueChanges.pipe(debounceTime(500)).subscribe((value: string) => {
      if(value.length != 0){
        this.fromSuggestions = this.buildings.filter(val => val.toLowerCase().includes(value.toLowerCase()));
        this.fromSuggestions.concat(this.rooms.filter(val => val.toLowerCase().includes(value.toLowerCase())));

        // Only check if suggetions is empty
        if(this.fromSuggestions.length == 0) this.mapsService.getPlacePredictions(value).subscribe(result => this.fromSuggestions = result.map(val => val.description));
      } else {
        this.navigationModel.from = value;
        this.fromSuggestions = [];
      }
    });

    this.toFormControl.valueChanges.subscribe((value: string) => {
      if(value != ""){
        this.toSuggestions = this.buildings.filter(val => val.toLowerCase().includes(value.toLowerCase()))
        // TODO get classroom suggestions
      } else {
        this.navigationModel.to = value;
        this.toSuggestions = [];
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
    // TODO check which navigation component to navigate to

    // Ways of sending data to other components (no data-binding) using states!
    // https://stackoverflow.com/a/54365098
    // Other alternatives: https://stackoverflow.com/a/44865817
    this.router.navigate([this.componentUrl], { state: this.navigationModel })
  }

}
