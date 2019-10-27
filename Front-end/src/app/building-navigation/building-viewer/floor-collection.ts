import * as THREE from 'three';
import { BuildingModel } from './building-model';
import { FloorModel } from './floor-model';

export class FloorCollection{
  private floorModels: FloorModel[] = [];

  constructor(public buildingModel: BuildingModel, readonly number: number){

  }

  addFloorModel(floorModel: FloorModel){
    this.floorModels.push(floorModel);
  }

  setVisible(visible: boolean){
    this.floorModels.forEach(floorModel => {
      floorModel.setVisible(visible);
    });
  }

}