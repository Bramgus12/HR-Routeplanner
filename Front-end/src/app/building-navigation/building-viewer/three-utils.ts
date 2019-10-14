/**
 * Common functions for managing a three.js scene.
 */
import * as THREE from 'three';

export class ThreeUtils {

  static getObjectsByFilter(object: THREE.Object3D, filter: (object: THREE.Object3D) => boolean): THREE.Object3D[] {

    let results: THREE.Object3D[] = [];

    object.children.forEach(childObject => {
      if (childObject.children.length > 0) {
        results = results.concat(this.getObjectsByFilter(childObject, filter));
      }
      if (filter(childObject)) {
        results.push(childObject);
      }
    });

    return results;
  }

  static getMeshesByBuildingPart(object: THREE.Object3D, part: string): THREE.Mesh[] {
    return <THREE.Mesh[]> ThreeUtils.getObjectsByFilter(object, (object: THREE.Object3D) => object.constructor.name === 'Mesh' && typeof (object.userData.buildingPart) === "string" && object.userData.buildingPart === part );
  }

}