import * as THREE from 'three';
import { FloorCollection } from './floor-collection';
import { ThreeUtils } from './three-utils';

export class FloorModel{
  private floorMesh: THREE.Mesh;
  private wallMesh: THREE.Mesh;
  private floorMaterial: THREE.MeshStandardMaterial;
  private wallMaterial: THREE.MeshStandardMaterial;
  private wallBoundingBox: THREE.Box3;
  readonly basePosition: THREE.Vector3 = new THREE.Vector3();
  readonly upPosition: THREE.Vector3 = new THREE.Vector3();
  readonly downPosition: THREE.Vector3 = new THREE.Vector3();

  private fromPosition: THREE.Vector3 = new THREE.Vector3();
  private toPosition: THREE.Vector3 = new THREE.Vector3();

  constructor(private collection: FloorCollection, private floorRoot: THREE.Object3D){
    this.basePosition.copy(floorRoot.position);
    this.toPosition.copy(floorRoot.position);
    this.fromPosition.copy(floorRoot.position);
    this.upPosition.copy(floorRoot.position);
    this.downPosition.copy(floorRoot.position);
    this.downPosition.y -= 20;
    this.upPosition.y += 20;
    this.floorMesh = ThreeUtils.getMeshByBuildingPart(floorRoot, "Floor");
    this.wallMesh = ThreeUtils.getMeshByBuildingPart(floorRoot, "Wall");

    this.floorMaterial = <THREE.MeshStandardMaterial> this.floorMesh.material;
    this.wallMaterial = <THREE.MeshStandardMaterial> this.wallMesh.material;

    this.floorMaterial.aoMapIntensity = 0.7;
    this.wallMaterial.aoMapIntensity = 0.7;

    this.floorMaterial.color.set(0xffffff);
    this.wallMaterial.color.set(0xffffff);
    
    this.floorMaterial.transparent = true;
    this.wallMaterial.transparent = true;

    this.wallBoundingBox = new THREE.Box3().setFromObject(this.wallMesh);
  }

  setOpacity(opacity: number){
    this.wallMaterial.opacity = opacity;
    this.floorMaterial.opacity = opacity;
  }

  setVisible(visible: boolean){
    this.floorRoot.visible = visible;
  }

  moveToBase(){
    this.fromPosition.copy(this.floorRoot.position);
    this.toPosition.copy(this.basePosition);
  }

  moveUp(){
    this.fromPosition.copy(this.floorRoot.position);
    this.toPosition.copy(this.upPosition);
  }

  moveDown(){
    this.fromPosition.copy(this.floorRoot.position);
    this.toPosition.copy(this.downPosition);
  }

  getBoundingBox(): THREE.Box3{
    return this.wallBoundingBox;
  }

  animate(delta: number){
    if( this.floorRoot.position.distanceTo(this.toPosition) > 0 ){
      this.floorRoot.position.y = ThreeUtils.tweenValue(this.floorRoot.position.y, this.toPosition.y, 50*delta);
    }

    this.setVisible(this.floorRoot.position.y != this.upPosition.y && this.floorRoot.position.y != this.downPosition.y);
  }
}