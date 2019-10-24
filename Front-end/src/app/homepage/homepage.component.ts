import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {  } from 'rxjs';

import { NavigationState } from '../shared/dataclasses';
import { GoogleMapsService } from '../3rdparty/google-maps.service';
@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit {

  navigationModel: NavigationState = { from: '', to: '', departNow: true, time: '' }

  constructor(private mapsService: GoogleMapsService, private router: Router) { }

  ngOnInit() {
    //setTimeout(() => this.mapsService.getPlacePredictions('Frans Halsstraat').subscribe(result => console.log(result)), 1000);
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

}
