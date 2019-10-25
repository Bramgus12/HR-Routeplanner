import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { FormControl } from '@angular/forms';
import { Observable, interval } from 'rxjs';
import { map, startWith, throttleTime, debounceTime } from 'rxjs/operators';

import { NavigationState } from '../shared/dataclasses';
import { GoogleMapsService } from '../3rdparty/google-maps.service';
@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit {

  navigationModel: NavigationState = { from: null, to: null, departNow: true, time: '' }
  @ViewChild('fromInput') fromFormControl: FormControl;
  fromSuggestions: google.maps.places.AutocompletePrediction[] = [];

  constructor(private mapsService: GoogleMapsService, private router: Router) { }

  ngOnInit() {
    this.fromFormControl.valueChanges.pipe(
      debounceTime(650),
      startWith<string | google.maps.places.AutocompletePrediction>('')
    ).subscribe(value => {
      if(typeof value == 'string' && value != "" ){
        console.log("from value update:", value);
        this.mapsService.getPlacePredictions(value).subscribe(result => this.fromSuggestions = result);
      } else {
        this.navigationModel.from = <google.maps.places.AutocompletePrediction>value;
        this.fromSuggestions = [];
      }
    })
  }

  departNowUpdate(departNow: boolean){
    if(!departNow) this.navigationModel.time = new Date().toLocaleTimeString(undefined, {
      hour: '2-digit',
      minute:'2-digit'
    });
  }

  goToNavigation(){
    // Ways of sending data to other components (no data-binding) using states!
    // https://stackoverflow.com/a/54365098
    // Other alternatives: https://stackoverflow.com/a/44865817
    console.log(this.navigationModel.from, this.navigationModel.to)
    this.router.navigate(['maps-navigation'], { state: this.navigationModel })
  }

  displayFn(prediction?: google.maps.places.AutocompletePrediction){
    console.log(prediction ? prediction.description : undefined)
    return prediction ? prediction.description : undefined;
  }

}
