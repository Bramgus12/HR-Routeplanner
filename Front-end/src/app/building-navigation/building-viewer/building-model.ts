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
    loadingManager.onLoad = () => { this.onLoad() };
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
      if( typeof floorRoot.userData.floorNumber === 'number' ){
        const floorCollection = this.getFloorCollectionByNumber(floorRoot.userData.floorNumber, true);
        const floorModel = new FloorModel(floorCollection, floorRoot);
        floorCollection.addFloorModel(floorModel);
      }
    });

    this.floorsCollections.forEach(floorCollection => {
      floorCollection.setVisible(floorCollection.number === 0);
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

  getObjectsByBuildingPart(part: string): THREE.Object3D[]{
    return ThreeUtils.getObjectsByFilter(this.root, (object: THREE.Object3D) => object.userData.buildingPart === part );
  }
}