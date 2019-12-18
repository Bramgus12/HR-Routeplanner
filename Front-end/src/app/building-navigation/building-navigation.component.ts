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

  private locationName: string;
  private from: number;
  private to: number;
  
  private navigationStateData: BuildingStep;
  private navigationState: NavigationState;

  get sliderValue(): number {
    return this.buildingViewer.nodePath.getTravelledPercentage() * this.travelledDistanceSlider.max;
  }
  set sliderValue(value: number) {
    this.travelledDistanceSlider.value = value;
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
      // this.navigationState = <NavigationState>state;

      // const retrieveAddresses: Observable<Address[]> = this.service.getAddresses();
      // const retrieveBuildings: Observable<Building[]> = this.service.getBuildings();

      // forkJoin(retrieveAddresses, retrieveBuildings).subscribe(responses => {
      //   const addresses: Address[] = responses[0];
      //   const buildings: Building[] = responses[1];
      //   for(let i = 0; i < addresses.length; i++){
      //     const address: Address = addresses[i];
      //     const addressStr: string = `${address.street} ${address.number}, ${address.city}`;
      //     if(addressStr == this.navigationState.from){
      //       for(let j = 0; j < buildings.length; j++){
      //         const building: Building = buildings[j];
      //         if(building.address_id == address.id){
      //           this.buildingViewer.currentLocationName = building.name;
      //           this.service.getNodes(building.name, "Entrance").subscribe( (entranceNodes: Node[]) => {
      //             if(this.navigationState.from == this.navigationState.to) {
      //               this.buildingViewer.createRoute(building.name, this.navigationState.fromNode, this.navigationState.toNode);
      //             }
      //             else if(entranceNodes.length > 0) {
      //               this.buildingViewer.createRoute(building.name, this.navigationState.fromNode, entranceNodes[0].number);
      //             }
      //           },
      //           error => {
      //             console.log(`Error retrieving entrance nodes.`);
      //             console.log(error);
      //           } );
      //           this.buildingViewer.loadBuilding(building.name);
      //           break;
      //         }
      //       }
      //       break;
      //     }
      //   }
      // },
      // error => {
      //   console.log("Error retrieving addresses or buildings.");
      //   console.log(error);
      // })
    }
    else{
      this.router.navigate(['/']);
    }
    
  }

  ngOnInit(): void {
    this.buildingViewer.currentLocationName = this.navigationStateData.locationName;
    this.buildingViewer.loadBuilding(this.navigationStateData.locationName);
    this.buildingViewer.createRoute(this.navigationStateData.locationName, this.navigationStateData.fromNode, this.navigationStateData.toNode);
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
