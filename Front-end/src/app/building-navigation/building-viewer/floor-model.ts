import { FloorCollection } from './floor-collection';
import { ThreeUtils } from './three-utils';

export class FloorModel{
  private floorMesh: THREE.Mesh;
  private wallMesh: THREE.Mesh;
  private floorMaterial: THREE.MeshStandardMaterial;
  private wallMaterial: THREE.MeshStandardMaterial;

  constructor(private collection: FloorCollection, private floorRoot: THREE.Object3D){
    this.floorMesh = ThreeUtils.getMeshByBuildingPart(floorRoot, "Floor");
    this.wallMesh = ThreeUtils.getMeshByBuildingPart(floorRoot, "Wall");

    this.floorMaterial = <THREE.MeshStandardMaterial> this.floorMesh.material;
    this.wallMaterial = <THREE.MeshStandardMaterial> this.wallMesh.material;

    this.floorMaterial.aoMapIntensity = 0.7;
    this.wallMaterial.aoMapIntensity = 0.7;

    this.floorMaterial.color.set(0xffffff);
    this.wallMaterial.color.set(0xffffff);
  }

  setVisible(visible: boolean){
    this.floorRoot.visible = visible;
  }
}