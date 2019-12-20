import { Component, OnInit, Output, ViewChild, ElementRef } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivationEnd } from '@angular/router';
import { MatDrawer } from '@angular/material/sidenav';
import { filter, map } from 'rxjs/operators';

import { AppService } from './app.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{

  @Output() title = 'No title';
  @ViewChild('sideDrawer') sideDrawer: MatDrawer;

  constructor(private router: Router, private titleService: Title, private appService: AppService){}

  ngOnInit(){
    /* This checks the loading route as snapshot */
    this.router.events.pipe(
      filter(event => event instanceof ActivationEnd),
      map((event: ActivationEnd) => event.snapshot)
    ).subscribe(snapshot => {
      const path = snapshot.routeConfig.path;

      if(path == "building-navigation" || path == "maps-navigation") this.appService.showFooter(false) 
      else this.appService.showFooter(true);

      /*This sets the title of each loaded page/component*/
      if(snapshot.hasOwnProperty("data")){
        this.title = snapshot.data['title'];
        this.titleService.setTitle(snapshot.data['title']);
      }
    });

    const savedDarkmode = JSON.parse(localStorage.getItem("dark-theme"));
    if(savedDarkmode != null) this.appService.setDarkmode(savedDarkmode);

    this.appService.trigger.subscribe(() => {
      if(this.sideDrawer.opened) this.sideDrawer.autoFocus = false;
      else this.sideDrawer.autoFocus = true;
      this.sideDrawer.toggle();
    });
  }
}
