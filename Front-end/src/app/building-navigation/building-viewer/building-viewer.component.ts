import { Component, OnInit, ViewChild, ElementRef, OnDestroy, AfterViewInit } from '@angular/core';
import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import { GLTFLoader, GLTF } from 'three/examples/jsm/loaders/GLTFLoader';
import { ThreeUtils } from './three-utils'
import { EffectComposer } from 'three/examples/jsm/postprocessing/EffectComposer.js';
import { SAOPass } from 'three/examples/jsm/postprocessing/SAOPass.js';
import { RenderPass } from 'three/examples/jsm/postprocessing/RenderPass.js';
import { GUI } from 'three/examples/jsm/libs/dat.gui.module.js';

@Component({
  selector: 'app-building-viewer',
  templateUrl: './building-viewer.component.html',
  styleUrls: ['./building-viewer.component.scss']
})
export class BuildingViewerComponent implements AfterViewInit, OnDestroy {
  @ViewChild('threejscontainer') threejsContainer: ElementRef;

  private scene: THREE.Scene;
  private camera: THREE.PerspectiveCamera;
  private renderer: THREE.WebGLRenderer;
  private composer: EffectComposer;
  private orbitControls: OrbitControls;
  private nextFrameId: number;
  private gui: GUI;

  constructor() {
    // Setup the three.js scene
    this.scene = new THREE.Scene();
    this.scene.background = new THREE.Color(0x505050);

    // Camera and renderer
    this.camera = new THREE.PerspectiveCamera(75, 1280 / 720, 0.1, 1000);
    this.renderer = new THREE.WebGLRenderer({ preserveDrawingBuffer: false, powerPreference: "high-performance"});
    console.log(this.renderer);
    this.renderer.setSize(1280, 720);
    this.renderer.shadowMap.enabled = true;
    this.renderer.shadowMap.type = THREE.VSMShadowMap;
    
    this.composer = new EffectComposer( this.renderer );
    const renderPass = new RenderPass( this.scene, this.camera );
    this.composer.addPass( renderPass );

    // Ambient Occlusion
    const saoPass: SAOPass = new SAOPass(this.scene, this.camera, false, true);
    saoPass.params.saoBias = -0.35;
    saoPass.params.saoIntensity = 0.11;
    saoPass.params.saoScale = 246.5;
    saoPass.params.saoKernelRadius = 37;
    saoPass.params.saoMinResolution = 0;
    // saoPass.params.saoBlur = 0;
    saoPass.params.saoBlurRadius = 116.6;
    saoPass.params.saoBlurStdDev = 2;
    saoPass.params.saoBlurDepthCutoff = 0.001;
    saoPass.enabled = true;
    // TODO Optimize performance, shadows and ambient oclusion.
    this.composer.addPass(saoPass);

    this.orbitControls = new OrbitControls(this.camera, this.renderer.domElement);
    this.camera.position.z = 5;
    // Setup lighting
    const directionalLight: THREE.DirectionalLight = new THREE.DirectionalLight(0xffffff, 0.8);
    directionalLight.castShadow = true;
    directionalLight.position.x = 10;
    directionalLight.position.z = -10;
    directionalLight.position.y = 50;
    // this.scene.add(directionalLight);

    directionalLight.shadow.mapSize.width = 512;
    directionalLight.shadow.mapSize.height = 512;
    directionalLight.shadow.camera.near = 10;
    directionalLight.shadow.camera.far = 200;
    directionalLight.shadow.camera.left = -50;
    directionalLight.shadow.camera.right = 50;
    directionalLight.shadow.camera.top = -50;
    directionalLight.shadow.camera.bottom = 50;

    // Gui tools for development
    this.gui = new GUI({ autoPlace: true });
    this.gui.add( saoPass.params, 'output', {
      'Beauty': SAOPass.OUTPUT.Beauty,
      'Beauty+SAO': SAOPass.OUTPUT.Default,
      'SAO': SAOPass.OUTPUT.SAO,
      'Depth': SAOPass.OUTPUT.Depth,
      'Normal': SAOPass.OUTPUT.Normal
    } ).onChange( function ( value ) {
      saoPass.params.output = parseInt( value );
    } );
    this.gui.add( saoPass.params, 'saoBias', - 1, 1 );
    this.gui.add( saoPass.params, 'saoIntensity', 0, 1 );
    this.gui.add( saoPass.params, 'saoScale', 0, 300 );
    this.gui.add( saoPass.params, 'saoKernelRadius', 1, 100 );
    this.gui.add( saoPass.params, 'saoMinResolution', 0, 1 );
    this.gui.add( saoPass.params, 'saoBlur' );
    this.gui.add( saoPass.params, 'saoBlurRadius', 0, 200 );
    this.gui.add( saoPass.params, 'saoBlurStdDev', 0.5, 150 );
    this.gui.add( saoPass.params, 'saoBlurDepthCutoff', 0.0, 0.1 );
    
    const directionalLightHelper:THREE.CameraHelper = new THREE.CameraHelper( directionalLight.shadow.camera );
    // this.scene.add( directionalLightHelper );

    const hemisphereLight = new THREE.HemisphereLight( 0xffffff, 0x080820, 0.5 );
    // this.scene.add( hemisphereLight );

    const ambientLight: THREE.AmbientLight = new THREE.AmbientLight(0xffffff, 2.0);
    this.scene.add(ambientLight);

    // Load models
    const loadingManager: THREE.LoadingManager = new THREE.LoadingManager();
    loadingManager.onProgress = (url, itemsLoaded, itemsTotal) => {
      console.log('Loading file: ' + url + '.\nLoaded ' + itemsLoaded + ' of ' + itemsTotal + ' files.');
    };
    loadingManager.onLoad = () => {
      console.log('Loading complete!');
    }
    loadingManager.onError = (url) => {
      console.log('There was an error loading ' + url);
    };

    const gltfLoader = new GLTFLoader(loadingManager);
    gltfLoader.load(
      'assets/building-viewer/Buildings.glb',
      (gltf: GLTF) => {
        const floorMeshes: THREE.Mesh[] = ThreeUtils.getMeshesByBuildingPart(gltf.scene, "Floor");
        floorMeshes.forEach(floorMesh => {
          // console.log(floorMesh);
          floorMesh.castShadow = false;
          floorMesh.receiveShadow = true;
          const material: THREE.MeshStandardMaterial = new THREE.MeshStandardMaterial();
          material.roughness = 1;
          floorMesh.material = material;
          
        });

        const wallMeshes: THREE.Mesh[] = ThreeUtils.getMeshesByBuildingPart(gltf.scene, "Wall");
        wallMeshes.forEach(wallMesh => {
          wallMesh.castShadow = true;
          wallMesh.receiveShadow = true;
          const material: THREE.MeshStandardMaterial = new THREE.MeshStandardMaterial();
          // material.roughness = 1;
          material.color = new THREE.Color( 0xBDBDBD );
          wallMesh.material = material;
          
        });
        
        this.scene.add(gltf.scene);
      },
    )

  }

  ngAfterViewInit() {
    // Add three.js canvas element to the div in this component.
    this.threejsContainer.nativeElement.appendChild(this.renderer.domElement);
    this.threejsContainer.nativeElement.appendChild(this.gui.domElement);
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
    // Update scene
    this.orbitControls.update();

    // Render
    // this.renderer.render(this.scene, this.camera);
    this.composer.render();

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
      this.composer.setSize(width, height);
      this.camera.aspect = width / height;
      this.camera.updateProjectionMatrix();
    }
  }

}
