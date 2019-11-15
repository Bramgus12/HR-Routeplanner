import * as THREE from 'three';
import { LineMaterial } from 'three/examples/jsm/lines/LineMaterial';
import { LineGeometry } from 'three/examples/jsm/lines/LineGeometry.js';
import { Line2 } from 'three/examples/jsm/lines/Line2.js';
import { Node, testRoute, NodeConnection } from './node'
import { BuildingViewerComponent } from './building-viewer.component';
import { FloorCollection } from './floor-collection';
import { ThreeUtils } from './three-utils';

export class NodePath{
  private nodes: Node[] = [];
  private nodeConnections: NodeConnection[] = [];
  private currentConnection: NodeConnection;

  private myLocation: THREE.Mesh;
  private pathLine: Line2;
  private drawCooldown: number;
  
  private velocity: number = 7;
  private direction: number = 0;

  private totalDistance: number = 0;
  private travelledDistance: number = 0;

  constructor(private buildingViewer: BuildingViewerComponent){
    // Create sphere for showing my location
    const sphereGeometry: THREE.SphereGeometry = new THREE.SphereGeometry(0.2, 8, 8);
    const sphereMaterial: THREE.MeshStandardMaterial = new THREE.MeshStandardMaterial({color: 0x0000ff});
    this.myLocation = new THREE.Mesh(sphereGeometry, sphereMaterial);
    this.buildingViewer.scene.add(this.myLocation);
    this.currentConnection = null;
    // Create a test route for now.
    this.create(testRoute);
  }

  create(nodes: Node[]){
    this.nodes = nodes;

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
      return this.travelledDistance - start;
    }
    this.currentConnection = null;
    return this.travelledDistance - start;
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
    this.drawVisibleNodes();
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

  /**
   * Draw nodes in visible floor collections
   */
  drawVisibleNodes(){
    const visibleCollections: FloorCollection[] = this.buildingViewer.buildingModel.showFloorsInRange(this.myLocation.position.y-1, this.myLocation.position.y+0.1);
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

  animate(delta: number){

    if(this.direction != 0){
      const difference: number = this.setTravelledDistance( this.travelledDistance + this.velocity * this.direction * delta );

      this.drawVisibleNodes();
    }


  }

}