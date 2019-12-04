import { Component, OnInit } from '@angular/core';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';

import { AppService } from '../app.service';

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss']
})
export class SidenavComponent implements OnInit {

  storedDarkmode: boolean = false;

  constructor(private appService: AppService) { }

  ngOnInit() {
    this.storedDarkmode = JSON.parse(localStorage.getItem("dark-theme"));
  }

  changeTheme(event: MatSlideToggleChange){
    this.appService.setDarkmode(event.checked);
  }

  closeNav(){
    this.appService.toggle();
  }

}
