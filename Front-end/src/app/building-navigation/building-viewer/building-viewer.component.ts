import { Component, OnInit, ViewChild, ElementRef, OnDestroy, AfterViewInit, Host, Inject } from '@angular/core';
import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import { DevGui } from './dev-gui';
import { BuildingModel } from './building-model';
import { NodePath } from './node-path';
import { BuildingNavigationComponent } from '../building-navigation.component';

@Component({
  selector: 'app-building-viewer',
  templateUrl: './building-viewer.component.html',
  styleUrls: ['./building-viewer.component.scss']
})
export class BuildingViewerComponent implements AfterViewInit, OnDestroy {
  @ViewChild('threejscontainer') threejsContainer: ElementRef;

  public scene: THREE.Scene;
  public camera: THREE.PerspectiveCamera;
  private renderer: THREE.WebGLRenderer;
  public orbitControls: OrbitControls;
  private nextFrameId: number;
  private clock: THREE.Clock;
  public buildingModel: BuildingModel;
  public nodePath: NodePath;

  private devGui: DevGui;

  constructor() {
    // Renderer
    this.renderer = new THREE.WebGLRenderer({
      antialias: true,
      logarithmicDepthBuffer: false,
    });
    this.renderer.gammaOutput = true;
    this.renderer.setSize(1280, 720);
    this.renderer.setPixelRatio( window.devicePixelRatio );
    
    // Camera
    this.camera = new THREE.PerspectiveCamera(50, 1280/720, 0.01, 200);
    this.camera.position.set(10, 10, 10);

    this.orbitControls = new OrbitControls(this.camera, this.renderer.domElement);
    this.orbitControls.enableDamping = true;
    this.orbitControls.dampingFactor = 0.05;
    this.orbitControls.enableKeys = false;
    this.orbitControls.update();
    
    // Scene
    this.scene = new THREE.Scene();
    // this.scene.background = new THREE.Color(0xffffff);

    // Clock for delta time
    this.clock = new THREE.Clock();

    // Lighting
    const ambientLight: THREE.AmbientLight = new THREE.AmbientLight(0xffffff, 1.0);
    this.scene.add(ambientLight);

    // Load building
    this.buildingModel = new BuildingModel(this, 'assets/building-viewer/Wijnhaven.glb');

    // Create path
    this.nodePath = new NodePath(this);

    // Dev gui for monitoring three.js performance
    // This should be removed or commented out once the 3D viewer correctly.
    this.devGui = new DevGui();

  }

  ngAfterViewInit() {
    // Add three.js canvas element to the div in this component.
    this.threejsContainer.nativeElement.appendChild(this.renderer.domElement);
    this.devGui.appendToElement(this.threejsContainer.nativeElement);
    this.resizeCanvasToContainer();
    this.animate();
  }

  ngOnDestroy() {
    // Remove event listeners from orbit controls
    this.orbitControls.dispose();
    // Stop render loop
    window.cancelAnimationFrame(this.nextFrameId);
    this.scene = undefined;
  }  

  // Main render loop for 3D view.
  animate() {
    const delta = this.clock.getDelta();
    // Start monitoring frame
    this.devGui.stats.begin();

    // Update orbit controls for damping
    this.orbitControls.update();

    // Update position of objects in the scene
    this.nodePath.animate(delta);
    this.buildingModel.animate(delta);

    // Render scene
    this.renderer.render(this.scene, this.camera);

    // End monitoring frame
    this.devGui.stats.end();

    this.nextFrameId = window.requestAnimationFrame(() => this.animate());
  }

  onResize() {
    this.resizeCanvasToContainer();
  }

  resizeCanvasToContainer() {
    const width: number = this.threejsContainer.nativeElement.clientWidth;
    const height: number = this.threejsContainer.nativeElement.clientHeight;
    if (this.renderer.domElement.width != width || this.renderer.domElement.height != height) {
      this.renderer.setSize(width, height);
      this.camera.aspect = width / height;
      this.camera.updateProjectionMatrix();
    }
  }

}
