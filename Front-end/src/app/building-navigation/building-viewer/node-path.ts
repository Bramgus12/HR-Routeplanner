import * as THREE from 'three';
import { LineMaterial } from 'three/examples/jsm/lines/LineMaterial';
import { LineGeometry } from 'three/examples/jsm/lines/LineGeometry.js';
import { Line2 } from 'three/examples/jsm/lines/Line2.js';
import { Node, testRoute } from './node'
import { BuildingViewerComponent } from './building-viewer.component';
import { FloorCollection } from './floor-collection';

export class NodePath{
  private nodes: Node[] = [];
  private currentNode: Node;
  private myLocation: THREE.Mesh;
  private pathLine: Line2;

  constructor(private buildingViewer: BuildingViewerComponent){
    // Create sphere for showing my location
    const sphereGeometry: THREE.SphereGeometry = new THREE.SphereGeometry(0.2, 8, 8);
    const sphereMaterial: THREE.MeshStandardMaterial = new THREE.MeshStandardMaterial({color: 0x0000ff});
    this.myLocation = new THREE.Mesh(sphereGeometry, sphereMaterial);
    this.buildingViewer.scene.add(this.myLocation);
    // Create a test route for now.
    this.create(testRoute);
  }

  create(nodes: Node[]){
    this.nodes = nodes;
    if(nodes.length > 0){
      this.currentNode = nodes[0];
      this.myLocation.position.set(this.currentNode.x, this.currentNode.y+0.5, this.currentNode.z)
    }

  }

  draw(nodes: Node[]){
    if(this.pathLine !== undefined){
      this.buildingViewer.scene.remove(this.pathLine);
    }

    // Create a line to represent the path
    const pathMaterial: LineMaterial = new LineMaterial({ 
      color: 0xff0000,
      linewidth: 5,
      dashed: false,
      dashScale: 1,
      dashSize: 1,
      gapSize: 0.5
    });
    pathMaterial.resolution.set( window.innerWidth, window.innerHeight );
    // pathMaterial.defines.USE_DASH = true;
    const pathGeometry: LineGeometry = new LineGeometry();

    const positions: number[] = [];
    nodes.forEach(node => {
      positions.push( node.x, node.y+0.5, node.z );
    });

    pathGeometry.setPositions(positions);
    
    this.pathLine = new Line2( pathGeometry, pathMaterial );
    this.pathLine.computeLineDistances();
    this.pathLine.scale.set( 1, 1, 1 );
    this.buildingViewer.scene.add( this.pathLine );
  }

  onLoad(){
    const visibleFloorCollections: FloorCollection[] = this.buildingViewer.buildingModel.showFloorsInRange(this.currentNode.y-1, this.currentNode.y+1);
    this.drawVisibleNodes(visibleFloorCollections);
  }

  private step(step: number){
    if(this.nodes.length > 0){
      const currentIndex: number = this.nodes.indexOf(this.currentNode);
      const nextIndex: number = currentIndex+step < this.nodes.length && currentIndex+step >= 0 ? currentIndex+step : currentIndex;
      const translation: THREE.Vector3 = new THREE.Vector3().subVectors(this.myLocation.position, new THREE.Vector3(this.currentNode.x, this.currentNode.y+0.5, this.currentNode.z));
      this.translateMyLocation(translation);
      this.currentNode = this.nodes[nextIndex];
    }

    const visibleFloorCollections: FloorCollection[] = this.buildingViewer.buildingModel.showFloorsInRange(this.currentNode.y-1, this.currentNode.y+1);
    this.drawVisibleNodes(visibleFloorCollections);

  }

  next(){
    this.step(1);
  }

  prev(){
    this.step(-1);
  }

  /**
   * Returns a list of nodes between a minimum and maximum height.
   */
  getNodesInRange(min: number, max: number): Node[]{
    const nodes: Node[] = [];
    let lastNode: Node = null;
    this.nodes.forEach(node => {
      if(node.y >= min && node.y <= max || lastNode != null && lastNode.y >= min && lastNode.y <= max){
        nodes.push(node);
        lastNode = node;
      }
    });
    return nodes;
  }

  /**
   * Draw nodes in visible floor collections
   */
  drawVisibleNodes(visibleCollections: FloorCollection[]){
    if(visibleCollections.length == 0){
      return;
    }
    let min = visibleCollections[0].getBoundingBox().min.y + visibleCollections[0].getYPos();
    let max = visibleCollections[0].getBoundingBox().max.y + visibleCollections[0].getYPos();
    visibleCollections.forEach(floorCollection => {
      const bbox: THREE.Box3 = floorCollection.getBoundingBox();
      const yPos: number = floorCollection.getYPos();
      min = bbox.min.y + yPos < min ? bbox.min.y + yPos : min;
      max = bbox.max.y + yPos > max ? bbox.max.y + yPos : max;
    });
    const visibleNodes: Node[] = this.getNodesInRange(min, max);
    this.draw(visibleNodes);
  }

  translateMyLocation(translation: THREE.Vector3){
    this.myLocation.position.x -= translation.x;
    this.myLocation.position.y -= translation.y;
    this.myLocation.position.z -= translation.z;
    
    this.buildingViewer.orbitControls.target.set(this.myLocation.position.x, this.myLocation.position.y, this.myLocation.position.z);
    this.buildingViewer.camera.position.x -= translation.x;
    this.buildingViewer.camera.position.y -= translation.y;
    this.buildingViewer.camera.position.z -= translation.z;
  }

  animate(delta: number){
    const targetPosition: THREE.Vector3 = new THREE.Vector3(this.currentNode.x, this.currentNode.y+0.5, this.currentNode.z);
    const targetDistance: number = this.myLocation.position.distanceTo(targetPosition);
    if(targetDistance > 0){
      const direction: THREE.Vector3 = new THREE.Vector3();
      direction.subVectors(this.myLocation.position, targetPosition).normalize();
      const velocity: number = 7;
      const moveDistance: number = velocity * delta;
      const translation: THREE.Vector3 = new THREE.Vector3(direction.x * moveDistance, direction.y * moveDistance, direction.z * moveDistance);
      if(targetDistance < delta * velocity){
        translation.subVectors(this.myLocation.position, targetPosition);
      }

      this.translateMyLocation(translation);
      
    }
  }

}