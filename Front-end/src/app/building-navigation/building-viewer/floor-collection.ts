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

  setVisible(visible: boolean){
    this.floorModels.forEach(floorModel => {
      floorModel.setVisible(visible);
    });
  }

  getYPos(): number | null{
    if(this.floorModels.length > 0){
      return this.floorModels[0].basePosition.y;
    }
    return null;
  }

  moveToBase(){
    this.floorModels.forEach(floorModel => {
      floorModel.moveToBase();
    });
  }

  moveUp(){
    this.floorModels.forEach(floorModel => {
      floorModel.moveUp();
    });
  }

  moveDown(){
    this.floorModels.forEach(floorModel => {
      floorModel.moveDown();
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