import { Component, OnInit, Output, ViewChild } from '@angular/core';
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
    /*This sets the title of each loaded page/component*/
    this.router.events.pipe(
      filter(event => event instanceof ActivationEnd),
      map((event: ActivationEnd) => event.snapshot.data)
    ).subscribe(data => {
      this.title = data['title'];
      this.titleService.setTitle(data['title']);
    });

    const savedDarkmode = JSON.parse(localStorage.getItem("dark-theme"));
    if(savedDarkmode != null) this.appService.setDarkmode(savedDarkmode);

    this.appService.trigger.subscribe(() => {
      this.sideDrawer.toggle();
    });
  }
}
