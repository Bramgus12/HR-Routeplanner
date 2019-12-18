import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { BuildingViewerComponent } from './building-viewer/building-viewer.component';
import { MatSliderChange, MatSlider } from '@angular/material';
import { BuildingNavigationService } from './building-navigation.service';
import { Building, Address, Node } from '../shared/dataclasses';
import { NavigationState, BuildingStep } from '../shared/navigation-state';
import { Observable, forkJoin } from 'rxjs';

@Component({
  selector: 'app-building-navigation',
  templateUrl: './building-navigation.component.html',
  styleUrls: ['./building-navigation.component.scss']
})
export class BuildingNavigationComponent implements OnInit {
  @ViewChild(BuildingViewerComponent) buildingViewer: BuildingViewerComponent;
  @ViewChild(MatSlider) travelledDistanceSlider: MatSlider;
  
  private navigationStateData: BuildingStep;
  public navigationState: NavigationState;

  get sliderValue(): number {
    return this.buildingViewer.nodePath.getTravelledPercentage() * this.travelledDistanceSlider.max;
  }
  set sliderValue(value: number) {
    this.travelledDistanceSlider.value = value;
  }

  get nextStateButtonVisible(): boolean{
    return this.buildingViewer.nodePath.nearEnd && this.navigationStateData.toNode == null;
  }

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private service: BuildingNavigationService) {
    const state: NavigationState = <NavigationState> this.router.getCurrentNavigation().extras.state;
    
    if( state !== undefined ){
      try {
        this.navigationState = <NavigationState>state;
        const currentStep = this.navigationState.getNextStep('building-navigation');
        this.navigationStateData = <BuildingStep>currentStep.data;
      } catch(err) {
        console.error(err);
        this.router.navigate(['/']);
      }
    }
    else{
      this.router.navigate(['/']);
    }
    
  }

  ngOnInit(): void {
    this.buildingViewer.currentLocationName = this.navigationStateData.locationName;
    this.buildingViewer.loadBuilding(this.navigationStateData.locationName);
    if(this.navigationStateData.fromNode != null && this.navigationStateData.toNode != null){
      this.buildingViewer.createRoute(this.navigationStateData.locationName, this.navigationStateData.fromNode, this.navigationStateData.toNode);
    }
    else{
      this.service.getNodes(this.navigationStateData.locationName, "Entrance").subscribe( (entranceNodes: Node[]) => {
        if(entranceNodes.length < 1){
          console.log(`There are no entrances for location '${this.navigationStateData.locationName}'`);
          this.router.navigate(['/']);
        }

        if(this.navigationStateData.fromNode == null) {
          this.buildingViewer.createRoute(this.navigationStateData.locationName, entranceNodes[0].number, this.navigationStateData.toNode);
        }
        else if(this.navigationStateData.toNode == null) {
          this.buildingViewer.createRoute(this.navigationStateData.locationName, this.navigationStateData.fromNode, entranceNodes[0].number);
        }
      },
      error => {
        console.log("Error retrieving entrance nodes.");
        console.log(error);
        this.router.navigate(['/']);
      } );
    }
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

  onSliderChange(event: MatSliderChange){
    this.buildingViewer.nodePath.setTravelledPercentage(event.value/event.source.max);
  }

}
