import { Component, OnInit } from '@angular/core';
import { SidenavService } from './sidenav.service';

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss']
})
export class SidenavComponent implements OnInit {

  constructor(private sidenavService: SidenavService) { }

  ngOnInit() {
    this.sidenavService.trigger.subscribe(() => {
      // open sidenav
    });
  }

}
