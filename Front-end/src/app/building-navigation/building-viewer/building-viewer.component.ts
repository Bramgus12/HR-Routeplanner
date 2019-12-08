import { Component, OnInit, ViewChild, ElementRef, OnDestroy, AfterViewInit, Host, Inject } from '@angular/core';
import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import { DevGui } from './dev-gui';
import { BuildingModel } from './building-model';
import { NodePath } from './node-path';
import { BuildingNavigationComponent } from '../building-navigation.component';
import { BuildingViewerService } from './building-viewer.service';
import { Node } from 'src/app/shared/dataclasses';
import { RenderPass } from 'three/examples/jsm/postprocessing/RenderPass.js';
import { ShaderPass } from 'three/examples/jsm/postprocessing/ShaderPass.js';
import { FXAAShader } from 'three/examples/jsm/shaders/FXAAShader';
import { EffectComposer } from 'three/examples/jsm/postprocessing/EffectComposer';
import { MeshStandardMaterial } from 'three';

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
  private composer: EffectComposer;
  private fxaaPass: ShaderPass
  public orbitControls: OrbitControls;
  private nextFrameId: number;
  private clock: THREE.Clock;
  public buildingModel: BuildingModel;
  public nodePath: NodePath;

  private devGui: DevGui;

  currentFloor: string = "0";

  constructor(private service: BuildingViewerService) {
    // Renderer
    this.renderer = new THREE.WebGLRenderer({
      antialias: false,
      logarithmicDepthBuffer: false,
    });
    this.renderer.gammaOutput = true;
    this.renderer.setSize(1280, 720);
    this.renderer.setPixelRatio( window.devicePixelRatio );
    
    // Camera
    this.camera = new THREE.PerspectiveCamera(50, 1280/720, 0.01, 200);
    this.camera.position.set(10, 10, 10);

    this.orbitControls = new OrbitControls(this.camera, this.renderer.domElement);
    this.orbitControls.enableKeys = false;
    this.orbitControls.maxPolarAngle = (Math.PI/2)-(Math.PI/20);
    this.orbitControls.update();
    
    // Scene
    this.scene = new THREE.Scene();
    // this.scene.background = new THREE.Color(0xffffff);

    // Effect composer
    this.composer = new EffectComposer(this.renderer);
    const renderPass: RenderPass = new RenderPass(this.scene, this.camera);
    this.composer.addPass(renderPass);
    this.composer.setPixelRatio( window.devicePixelRatio );

    this.fxaaPass = new ShaderPass( FXAAShader );
    this.composer.addPass(this.fxaaPass);

    // Clock for delta time
    this.clock = new THREE.Clock();

    // Lighting
    const ambientLight: THREE.AmbientLight = new THREE.AmbientLight(0xffffff, 1.0);
    this.scene.add(ambientLight);

    // Create path
    this.nodePath = new NodePath(this);

    // Dev gui for monitoring three.js performance
    // This should be removed or commented out once the 3D viewer correctly.
    this.devGui = new DevGui();

  }

  loadBuilding(buildingName: string){
    // Load building
    this.buildingModel = new BuildingModel(this, `assets/building-viewer/${buildingName}.glb`);
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

  createRoute(locationName: string, from: number, to: number){
    this.service.getRoute(locationName, from, to).subscribe(nodes => {
      this.nodePath.create(nodes);
    });
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
    if(this.buildingModel !== undefined){
      this.buildingModel.animate(delta);
    }

    // Render scene
    // this.renderer.render(this.scene, this.camera);
    this.composer.render();

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
      // fxaa
      const pixelRatio: number = this.renderer.getPixelRatio();
      const fxaaPassMaterial: THREE.ShaderMaterial = <THREE.ShaderMaterial> this.fxaaPass.material;
      fxaaPassMaterial.uniforms[ 'resolution' ].value.x = 1 / ( width * pixelRatio );
      fxaaPassMaterial.uniforms[ 'resolution' ].value.y = 1 / ( height * pixelRatio );

      // renderer
      this.renderer.setSize(width, height);
      this.composer.setSize(width, height);
      this.camera.aspect = width / height;
      this.camera.updateProjectionMatrix();
    }
  }

}
