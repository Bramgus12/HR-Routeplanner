import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { BuildingViewerComponent } from './building-viewer/building-viewer.component';

@Component({
  selector: 'app-building-navigation',
  templateUrl: './building-navigation.component.html',
  styleUrls: ['./building-navigation.component.scss']
})
export class BuildingNavigationComponent implements OnInit {

  @ViewChild(BuildingViewerComponent) buildingViewer: BuildingViewerComponent;

  constructor(private router: Router) { }

  ngOnInit() {
  }

  forward(){
    this.buildingViewer.forward();
  }
  backward(){
    this.buildingViewer.backward();
  }

}
