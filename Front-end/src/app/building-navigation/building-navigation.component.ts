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

  forwardPressed(){
    this.buildingViewer.nodePath.forward(true);
  }
  forwardReleased(){
    this.buildingViewer.nodePath.forward(false);
  }

  backwardPressed(){
    this.buildingViewer.nodePath.backward(true);
  }
  backwardReleased(){
    this.buildingViewer.nodePath.backward(false);
  }

}
