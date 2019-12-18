import * as THREE from 'three';
import { LineMaterial } from 'three/examples/jsm/lines/LineMaterial';
import { LineGeometry } from 'three/examples/jsm/lines/LineGeometry.js';
import { Line2 } from 'three/examples/jsm/lines/Line2.js';
import { BuildingViewerComponent } from './building-viewer.component';
import { FloorCollection } from './floor-collection';
import { ThreeUtils } from './three-utils';
import { FloorModel } from './floor-model';
import { Node, NodeConnection } from 'src/app/shared/dataclasses';

export class NodePath{
  private nodes: Node[] = [];
  private nodeConnections: NodeConnection[] = [];
  private currentConnection: NodeConnection;

  private myLocation: THREE.Mesh;
  private pathLine: Line2;
  
  private velocity: number = 7;
  private direction: number = 0;

  private totalDistance: number = 0;
  private travelledDistance: number = 0;

  private visibilityRaycaster: THREE.Raycaster = new THREE.Raycaster();

  constructor(private buildingViewer: BuildingViewerComponent){
    // Create sphere for showing my location
    const sphereGeometry: THREE.SphereGeometry = new THREE.SphereGeometry(0.2, 16, 16);
    const sphereMaterial: THREE.MeshStandardMaterial = new THREE.MeshStandardMaterial({color: 0x0000ff});
    this.myLocation = new THREE.Mesh(sphereGeometry, sphereMaterial);
    this.myLocation.visible = false;
    this.buildingViewer.scene.add(this.myLocation);
    this.currentConnection = null;
    // // Create a test route for now.
    // this.create(testRoute);
  }

  create(nodes: Node[]){
    this.nodes = nodes;
    this.myLocation.visible = true;

    this.nodeConnections = [];
    this.totalDistance = 0;
    this.travelledDistance = 0;
    this.currentConnection = null;
    for(let i: number = 0; i < nodes.length-1; i++){
      const node1: Node = nodes[i];
      const node2: Node = nodes[i+1];
      const node1Vector: THREE.Vector3 = ThreeUtils.nodeToVector(node1);
      const node2Vector: THREE.Vector3 = ThreeUtils.nodeToVector(node2);
      const distance: number = node1Vector.distanceTo(node2Vector);

      this.nodeConnections.push({
        node1: node1,
        node2: node2,
        node1Vector: node1Vector,
        node2Vector: node2Vector,
        node1Distance: this.totalDistance,
        node2Distance: this.totalDistance + distance,
        distance: node1Vector.distanceTo(node2Vector)
      });

      this.totalDistance += distance;

      if(i == 0){
        this.currentConnection = this.nodeConnections[i];
        this.myLocation.position.set(this.nodeConnections[i].node1.x, this.nodeConnections[i].node1.y+0.5, this.nodeConnections[i].node1.z)
      }
    }

    this.drawVisibleNodes();

    this.moveCameraToMyLocation();

  }

  getConnectionByDistance(distance: number): NodeConnection | null{
    for(let i:number = 0; i < this.nodeConnections.length; i++){
      const nodeConnection: NodeConnection = this.nodeConnections[i];
      if(distance >= nodeConnection.node1Distance && distance < nodeConnection.node2Distance ){
        return nodeConnection;
      }
    }
    return null;
  }

  setTravelledDistance(distance: number): number{
    const start: number = this.travelledDistance;
    if(distance > this.totalDistance){
      distance = this.totalDistance;
    }
    else if (distance < 0){
      distance = 0;
    }
    this.travelledDistance = distance;
    const nodeConnection: NodeConnection = this.getConnectionByDistance(distance);
    if(nodeConnection !== null){
      this.currentConnection = nodeConnection;
      const myLocationDistance: number = distance - nodeConnection.node1Distance;
      const myLocationTranslation =  new THREE.Vector3().subVectors(nodeConnection.node2Vector, nodeConnection.node1Vector).normalize().multiply(new THREE.Vector3(myLocationDistance,myLocationDistance,myLocationDistance));
      this.myLocation.position.set( nodeConnection.node1Vector.x + myLocationTranslation.x, nodeConnection.node1Vector.y + myLocationTranslation.y+0.5, nodeConnection.node1Vector.z + myLocationTranslation.z );
    }
    else{
      this.currentConnection = null;
    }
    const difference: number = this.travelledDistance - start;
    
    if(difference != 0){
      this.drawVisibleNodes();

      // Move camera to my location.
      this.moveCameraToMyLocation();

    }

    return difference;
  }

  moveCameraToMyLocation(): THREE.Vector3{
    const camera: THREE.PerspectiveCamera = this.buildingViewer.camera;
    const cameraTranslation: THREE.Vector3 = new THREE.Vector3().subVectors(this.myLocation.position, this.buildingViewer.orbitControls.target);
    this.buildingViewer.orbitControls.target.x += cameraTranslation.x;
    this.buildingViewer.orbitControls.target.y += cameraTranslation.y;
    this.buildingViewer.orbitControls.target.z += cameraTranslation.z;
    
    camera.position.x += cameraTranslation.x;
    camera.position.y += cameraTranslation.y;
    camera.position.z += cameraTranslation.z;
    return cameraTranslation;
  }

  getTravelledDistance(): number{
    return this.travelledDistance;
  }

  setTravelledPercentage(percentage: number){
    this.setTravelledDistance(percentage * this.totalDistance);
  }

  getTravelledPercentage(): number{
    return this.travelledDistance/this.totalDistance;
  }

  draw(vectors: THREE.Vector3[]){
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
    vectors.forEach(vector => {
      positions.push( vector.x, vector.y, vector.z );
    });

    pathGeometry.setPositions(positions);
    
    this.pathLine = new Line2( pathGeometry, pathMaterial );
    this.pathLine.computeLineDistances();
    this.pathLine.scale.set( 1, 1, 1 );
    this.buildingViewer.scene.add( this.pathLine );
  }

  onLoad(){
    this.drawVisibleNodes(true);
  }

  forward(move: boolean){
    this.direction = move ? 1 : 0;
  }

  backward(move: boolean){
    this.direction = move ? -1 : 0;
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

  getConnectionsInRange(min: number, max: number, startAtCurrentConnection: boolean = false): NodeConnection[]{
    const nodeConnections: NodeConnection[] = [];
    const currentIndex: number = this.nodeConnections.indexOf(this.currentConnection);
    if(startAtCurrentConnection && currentIndex == -1){
      return nodeConnections;
    }
    for(let i: number = startAtCurrentConnection ? currentIndex : 0; i < this.nodeConnections.length; i++){
      const nodeConnection: NodeConnection = this.nodeConnections[i];
      if (nodeConnection.node1Vector.y >= min && nodeConnection.node1Vector.y <= max || nodeConnection.node2Vector.y >= min && nodeConnection.node2Vector.y <= max){
        nodeConnections.push(nodeConnection);
      }
      else {
        break;
      }
    }

    return nodeConnections;
  }

  /**
   * Draw nodes in visible floor collections
   */
  drawVisibleNodes(instant: boolean=false){
    const visibleCollections: FloorCollection[] = this.buildingViewer.buildingModel.showFloorsInRange(this.myLocation.position.y-0.5, this.myLocation.position.y+0.02, instant);
    if(visibleCollections.length == 0){
      return;
    }
    let min = visibleCollections[0].getBoundingBox().min.y + visibleCollections[0].getYPos();
    let max = visibleCollections[0].getBoundingBox().max.y + visibleCollections[0].getYPos();
    let currentFloorFound: boolean = false;
    visibleCollections.forEach(floorCollection => {
      const bbox: THREE.Box3 = floorCollection.getBoundingBox();
      const yPos: number = floorCollection.getYPos();
      min = bbox.min.y + yPos < min ? bbox.min.y + yPos : min;
      max = bbox.max.y + yPos > max ? bbox.max.y + yPos : max;

      // Find current floor
      if( !currentFloorFound && this.myLocation.position.y >= bbox.min.y + yPos && this.myLocation.position.y < bbox.max.y + yPos ){
        this.buildingViewer.currentFloor = floorCollection.number.toString();
        currentFloorFound = true;
      }
    });

    const visibleConnections: NodeConnection[] = this.getConnectionsInRange(min, max, true);
    const visibleVectors: THREE.Vector3[] = [];

    visibleVectors.push(this.myLocation.position);

    for(let i: number = 0; i < visibleConnections.length; i++){
      if(i > 0){
        const vector: THREE.Vector3 = new THREE.Vector3().copy(visibleConnections[i].node1Vector);
        vector.y += 0.5;
        visibleVectors.push( vector );
      }
      if(i+1 == visibleConnections.length){
        const vector: THREE.Vector3 = new THREE.Vector3().copy(visibleConnections[i].node2Vector);
        vector.y += 0.5;
        visibleVectors.push( vector );
      }
    }

    this.draw(visibleVectors);
  }

  animate(delta: number){
    
    if(this.nodes.length > 0){

      const difference: number = this.setTravelledDistance( this.travelledDistance + this.velocity * this.direction * delta );
      
      // Check if my location is visible to the camera
      const camera: THREE.PerspectiveCamera = this.buildingViewer.camera;
      const visibleFloorModels: FloorModel[] = this.buildingViewer.buildingModel.getVisibleFloorModels();
      const wallMeshes: THREE.Mesh[] = [];
      visibleFloorModels.forEach(floorModel => {
        if(floorModel.wallObstruction == true) floorModel.wallObstruction = false;
        wallMeshes.push(floorModel.wallMesh);
      });
      
      const myLocationDistance: number = camera.position.distanceTo(this.myLocation.position);
      this.visibilityRaycaster.far = myLocationDistance;
      const myLocationDirection: THREE.Vector3 = new THREE.Vector3().subVectors(this.myLocation.position, camera.position).normalize();
      this.visibilityRaycaster.set(camera.position, myLocationDirection);
      const intersects: THREE.Intersection[] = this.visibilityRaycaster.intersectObjects(wallMeshes);
  
      intersects.forEach(intersect => {
        visibleFloorModels.forEach(floorModel => {
          if(floorModel.wallMesh === intersect.object){
            floorModel.wallObstruction = true;
          }
        });
      });
    }
  }

}