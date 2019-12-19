import * as THREE from 'three';
import { FloorCollection } from './floor-collection';
import { ThreeUtils } from './three-utils';

export class FloorModel{
  static readonly HIDDEN: number = 0;
  static readonly VISIBLE: number = 1;
  static readonly APPEAR: number = 2;
  static readonly DISAPPEAR: number = 3;
  private state: number = 0;
  public wallObstruction: boolean = false;

  readonly hiddenOpacity: number = 0;
  readonly visibleOpacity: number = 1;
  readonly hiddenScale: number = 0.0001;
  readonly visibleScale: number = 1;
  readonly fadeSpeed: number = 6;
  readonly slowFadeSpeed: number = 0.8;
  private opacity: number = 0;
  private obstructedOpacity: number = 0.85;
  
  readonly floorMesh: THREE.Mesh;
  readonly wallMesh: THREE.Mesh;
  private floorMaterial: THREE.MeshStandardMaterial;
  private wallMaterial: THREE.MeshStandardMaterial;
  private wallBoundingBox: THREE.Box3;

  get visible(): boolean{
    return this.floorRoot.visible;
  }
  set visible(value: boolean){
    this.floorRoot.visible = value;
  }

  constructor(private collection: FloorCollection, readonly floorRoot: THREE.Object3D){
    this.floorMesh = ThreeUtils.getMeshByBuildingPart(floorRoot, "Floor");
    this.wallMesh = ThreeUtils.getMeshByBuildingPart(floorRoot, "Wall");

    this.floorMaterial = <THREE.MeshStandardMaterial> this.floorMesh.material;
    this.wallMaterial = <THREE.MeshStandardMaterial> this.wallMesh.material;

    this.floorMaterial.aoMapIntensity = 0.7;
    this.wallMaterial.aoMapIntensity = 0.7;

    this.floorMaterial.color.set(0xffffff);
    // this.wallMaterial.color.set(0xffffff);
    
    this.floorMaterial.transparent = true;
    this.wallMaterial.transparent = true;

    this.floorMaterial.side = THREE.FrontSide;
    this.wallMaterial.side = THREE.FrontSide;

    this.wallBoundingBox = new THREE.Box3().setFromObject(this.wallMesh);
  }

  getBoundingBox(): THREE.Box3{
    return this.wallBoundingBox;
  }

  setState(state: number){

    if(state == FloorModel.HIDDEN){
      this.visible = false
      this.opacity = this.hiddenOpacity;
      this.state = state;
      this.wallMesh.scale.y = this.hiddenScale;
      this.floorMesh.scale.y = this.hiddenScale;
    }
    else if(state == FloorModel.VISIBLE){
      this.visible = true
      this.opacity = this.visibleOpacity;
      this.state = state;
      this.wallMesh.scale.y = this.visibleScale;
      this.floorMesh.scale.y = this.visibleScale;
    }
    else if(state == FloorModel.APPEAR && this.state != FloorModel.APPEAR && this.state != FloorModel.VISIBLE){
      this.visible = true;
      this.opacity = this.hiddenOpacity;
      this.state = state;
      this.wallMesh.scale.y = this.hiddenScale;
      this.floorMesh.scale.y = this.hiddenScale;
    }
    else if(state == FloorModel.DISAPPEAR && this.state != FloorModel.DISAPPEAR && this.state != FloorModel.HIDDEN){
      this.visible = true;
      this.opacity = this.visibleOpacity;
      this.state = state;
      this.wallMesh.scale.y = this.visibleScale;
      this.floorMesh.scale.y = this.visibleScale;
    }

  }

  getState(){
    return this.state;
  }

  animate(delta: number){

    if(this.state == FloorModel.APPEAR){
      this.opacity = ThreeUtils.tweenValue(this.opacity, this.visibleOpacity, this.fadeSpeed * delta);
      this.wallMesh.scale.y = ThreeUtils.tweenValue(this.wallMesh.scale.y, this.visibleScale, this.fadeSpeed * delta);
      this.floorMesh.scale.y = ThreeUtils.tweenValue(this.floorMesh.scale.y, this.visibleScale, this.fadeSpeed * delta);
      if(this.opacity == this.visibleOpacity && this.wallMesh.scale.y == this.visibleScale){
        this.setState(FloorModel.VISIBLE);
      }
    }
    else if(this.state == FloorModel.DISAPPEAR){
      this.opacity = ThreeUtils.tweenValue(this.opacity, this.hiddenOpacity, this.fadeSpeed * delta);
      this.wallMesh.scale.y = ThreeUtils.tweenValue(this.wallMesh.scale.y, this.hiddenScale, this.fadeSpeed * delta);
      this.floorMesh.scale.y = ThreeUtils.tweenValue(this.floorMesh.scale.y, this.hiddenScale, this.fadeSpeed * delta);
      if(this.opacity == this.hiddenOpacity && this.wallMesh.scale.y == this.hiddenScale){
        this.setState(FloorModel.HIDDEN);
      }
    }

    if(this.wallObstruction && this.opacity >= this.obstructedOpacity){
      this.wallMaterial.opacity = ThreeUtils.tweenValue(this.wallMaterial.opacity, this.obstructedOpacity, delta * this.slowFadeSpeed);
    }
    else if(this.wallMaterial.opacity != this.opacity && this.opacity == this.visibleOpacity){
      this.wallMaterial.opacity = ThreeUtils.tweenValue(this.wallMaterial.opacity, this.opacity, delta * this.slowFadeSpeed);
    }
    else if(this.wallMaterial.opacity != this.opacity){
      this.wallMaterial.opacity = this.opacity;
    }

    if(this.floorMaterial.opacity != this.opacity){
      this.floorMaterial.opacity = this.opacity;
    }

  }
}