import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { NavigationState } from '../shared/dataclasses';
@Component({
  selector: 'app-maps-navigation',
  templateUrl: './maps-navigation.component.html',
  styleUrls: ['./maps-navigation.component.scss']
})
export class MapsNavigationComponent implements OnInit {

  navigationState: NavigationState = { from: '', to: '', departNow: true, time: '' };

  constructor(private router: Router) {
    const state = this.router.getCurrentNavigation().extras.state;

    if(state == undefined || !Object.keys(this.navigationState).every(prop => state.hasOwnProperty(prop))) this.router.navigate(['/'])
    
    this.navigationState = <NavigationState>state;
  }

  ngOnInit() {

  }

}
