import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { BuildingViewerComponent } from './building-viewer/building-viewer.component';
import { MatSliderChange, MatSlider } from '@angular/material';

@Component({
  selector: 'app-building-navigation',
  templateUrl: './building-navigation.component.html',
  styleUrls: ['./building-navigation.component.scss']
})
export class BuildingNavigationComponent implements OnInit {
  @ViewChild(BuildingViewerComponent) buildingViewer: BuildingViewerComponent;
  @ViewChild(MatSlider) travelledDistanceSlider: MatSlider;
  get sliderValue(): number {
    return this.buildingViewer.nodePath.getTravelledPercentage() * this.travelledDistanceSlider.max;
  }
  set sliderValue(value: number) {
    this.travelledDistanceSlider.value = value;
  }

  constructor(private router: Router) { }

  ngOnInit() {}

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

  onSliderChange(event: MatSliderChange){
    this.buildingViewer.nodePath.setTravelledPercentage(event.value/event.source.max);
  }

}
