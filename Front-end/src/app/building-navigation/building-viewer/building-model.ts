import * as THREE from 'three';
import { BuildingViewerComponent } from './building-viewer.component'
import { GLTFLoader, GLTF } from 'three/examples/jsm/loaders/GLTFLoader';
import { ThreeUtils } from './three-utils'
import { FloorCollection } from './floor-collection';
import { FloorModel } from './floor-model';

export class BuildingModel{
  private root: THREE.Scene;
  private floorsCollections: FloorCollection[] = [];

  constructor(private buildingViewer: BuildingViewerComponent, filepath: string){

    // Load models
    const loadingManager: THREE.LoadingManager = new THREE.LoadingManager();
    loadingManager.onProgress = (url, itemsLoaded, itemsTotal) => {
      // console.log('Loading file: ' + url + '.\nLoaded ' + itemsLoaded + ' of ' + itemsTotal + ' files.');
    };
    loadingManager.onLoad = () => { 
      this.onLoad();
      this.buildingViewer.nodePath.onLoad();
    };
    loadingManager.onError = (url) => {
      // console.log('There was an error loading ' + url);
    };

    const gltfLoader = new GLTFLoader(loadingManager);
    gltfLoader.load(
      filepath,
      (gltf: GLTF) => {
        this.root = gltf.scene;
      },
    );
  }

  onLoad(){
    // Setup floor collections and materials
    const floorRoots: THREE.Object3D[] = this.getObjectsByBuildingPart("FloorRoot");
    floorRoots.forEach(floorRoot => {
      // floorRoot.visible = false;
      if( typeof floorRoot.userData.floorNumber === 'number' ){
        const floorCollection = this.getFloorCollectionByNumber(floorRoot.userData.floorNumber, true);
        const floorModel = new FloorModel(floorCollection, floorRoot);
        floorModel.setState(FloorModel.HIDDEN);
        floorCollection.addFloorModel(floorModel);
      }
    });

    this.floorsCollections.forEach(floorCollection => {
      // floorCollection.setVisible(floorCollection.number === 0);
    });

    this.buildingViewer.scene.add(this.root);
  }

  getFloorCollectionByNumber(number: number, create: boolean=false): FloorCollection | null{
    for(let i = 0; i < this.floorsCollections.length; i++){
      if(this.floorsCollections[i].number == number){
        return this.floorsCollections[i];
      }
    }
    if(create){
      const floor = new FloorCollection(this, number);
      this.floorsCollections.push(floor);
      return floor;
    }
    return null;
  }

  /**
   * Returns a list of floors between a minimum and maximum height.
   */
  getFloorsInRange(min: number, max: number): FloorCollection[]{
    const floorCollections: FloorCollection[] = [];
    this.floorsCollections.forEach(floorCollection => {
      const bbox: THREE.Box3 = floorCollection.getBoundingBox();
      const yPos: number = floorCollection.getYPos();
      if(bbox.max.y + yPos > min && bbox.min.y + yPos < max){
        floorCollections.push(floorCollection);
      }
    });
    return floorCollections;
  }

  showFloorsInRange(min: number, max: number, instant: boolean=false): FloorCollection[]{
    const visibleFloorCollections: FloorCollection[] = this.getFloorsInRange(min, max);
    this.floorsCollections.forEach(floorCollection => {
      const visible: boolean = visibleFloorCollections.indexOf(floorCollection) >= 0;

      // floorCollection.setVisible( visible );
      if(visible && instant){
        floorCollection.setState(FloorModel.VISIBLE);
      }
      else if(!visible && instant){
        floorCollection.setState(FloorModel.HIDDEN);
      }
      else if(visible){
        floorCollection.setState(FloorModel.APPEAR);
      }
      else if(!visible){
        floorCollection.setState(FloorModel.DISAPPEAR);
      }
    });
    return visibleFloorCollections;
  }

  getVisibleFloorModels(): FloorModel[]{
    let visibleFloorsModels: FloorModel[] = [];
    this.floorsCollections.forEach(floorsCollection => {
      visibleFloorsModels = visibleFloorsModels.concat(floorsCollection.getVisibleFloorModels());
    });
    return visibleFloorsModels;
  }

  getObjectsByBuildingPart(part: string): THREE.Object3D[]{
    return ThreeUtils.getObjectsByFilter(this.root, (object: THREE.Object3D) => object.userData.buildingPart === part );
  }

  animate(delta: number){
    this.floorsCollections.forEach(floorCollection => {
      floorCollection.animate(delta);
    });
  }
}