import { Component, OnInit, Input } from '@angular/core';
import { SidenavService } from '../sidenav/sidenav.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  @Input() title = "";

  constructor(private sidenavService: SidenavService) { }

  ngOnInit() {
  }

}
