import * as THREE from 'three';
import { BuildingModel } from './building-model';
import { FloorModel } from './floor-model';

export class FloorCollection{
  private floorModels: FloorModel[] = [];

  constructor(public buildingModel: BuildingModel, readonly number: number){
  }

  addFloorModel(floorModel: FloorModel){
    this.floorModels.push(floorModel);
    this.getBoundingBox();
  }

  getYPos(): number | null{
    if(this.floorModels.length > 0){
      return this.floorModels[0].floorRoot.position.y;
    }
    return null;
  }

  setState(state: number){
    this.floorModels.forEach(floorModel => {
      floorModel.setState(state);
    });
  }

  getBoundingBox(): THREE.Box3 | null{
    if(this.floorModels.length < 1){
      return null;
    }
    const boundingBox = this.floorModels[0].getBoundingBox();
    this.floorModels.forEach(floorModel => {
      const floorModelBoundingBox: THREE.Box3 = floorModel.getBoundingBox();
      ['x','y','z'].forEach(axis => {
        boundingBox.min[axis] = Math.min(boundingBox.min[axis], floorModelBoundingBox.min[axis]);
        boundingBox.max[axis] = Math.max(boundingBox.max[axis], floorModelBoundingBox.max[axis]);
      });
    });
    return boundingBox;
  }

  animate(delta: number){
    this.floorModels.forEach(floorModel => {
      floorModel.animate(delta);
    });
  }

}