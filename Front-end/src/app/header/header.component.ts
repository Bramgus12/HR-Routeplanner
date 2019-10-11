import { Component, OnInit, Input } from '@angular/core';
import { AppService } from '../app.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  @Input() title = "";

  constructor(private appService: AppService) { }

  ngOnInit() {
  }

  triggerSidenav(){ this.appService.toggle(); }

}
