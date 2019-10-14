import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-maps-navigation',
  templateUrl: './maps-navigation.component.html',
  styleUrls: ['./maps-navigation.component.scss']
})
export class MapsNavigationComponent implements OnInit {

  from: any = '';
  to: any = '';

  constructor(private router: Router) {
    const state = this.router.getCurrentNavigation().extras.state;

    if(state == undefined || state.from == undefined || state.to == undefined) this.router.navigate(['/'])

    this.from = state.from;
    this.to = state.to;
  }

  ngOnInit() {

  }

}
