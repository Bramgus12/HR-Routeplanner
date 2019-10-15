import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { NavigationState } from '../shared/dataclasses';
@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit {

  navigationModel: NavigationState = { from: '', to: '', departNow: true, time: '' }

  constructor(private router: Router) { }

  ngOnInit() {
    
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
