import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-building-navigation',
  templateUrl: './building-navigation.component.html',
  styleUrls: ['./building-navigation.component.scss']
})
export class BuildingNavigationComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
  }

}
