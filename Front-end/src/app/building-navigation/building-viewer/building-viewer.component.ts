import { Component, OnInit, ViewChild, ElementRef, OnDestroy, AfterViewInit } from '@angular/core';
import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import { GLTFLoader, GLTF } from 'three/examples/jsm/loaders/GLTFLoader';
import { ThreeUtils } from './three-utils'
import * as POSTPROCESSING from "postprocessing";
import { DevGui } from './dev-gui';

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
  private composer: POSTPROCESSING.EffectComposer;
  private orbitControls: OrbitControls;
  private nextFrameId: number;

  private devGui: DevGui;

  constructor() {
    // Renderer and composer
    this.renderer = new THREE.WebGLRenderer({
      logarithmicDepthBuffer: false
    });
    this.renderer.setSize(1280, 720);
    this.composer = new POSTPROCESSING.EffectComposer(this.renderer);
    
    // Camera
    this.camera = new THREE.PerspectiveCamera(50, 1280/720, 2.0, 200);
    this.camera.position.set(46, 30, -15);

    this.orbitControls = new OrbitControls(this.camera, this.renderer.domElement);
    this.orbitControls.target.set(22, 2, -34);
    this.orbitControls.update();
    
    // Scene
    this.scene = new THREE.Scene();

    // Lighting
    const ambientLight: THREE.AmbientLight = new THREE.AmbientLight(0x404040, 5.0);
    this.scene.add(ambientLight);

    const dirLight1: THREE.DirectionalLight = new THREE.DirectionalLight(0xffffff, 0.2);
    dirLight1.castShadow = false;
    dirLight1.position.x = -50;
    dirLight1.position.z = 50;
    dirLight1.position.y = 50;
    dirLight1.shadow.mapSize.width = 512;
    dirLight1.shadow.mapSize.height = 512;
    dirLight1.shadow.camera.near = 10;
    dirLight1.shadow.camera.far = 200;
    dirLight1.shadow.camera.left = -50;
    dirLight1.shadow.camera.right = 50;
    dirLight1.shadow.camera.top = -50;
    dirLight1.shadow.camera.bottom = 50;
    this.scene.add(dirLight1);
    
    const dirLight2: THREE.DirectionalLight = dirLight1.clone();
    dirLight2.position.x = 50;
    console.log(dirLight2);
    this.scene.add(dirLight2);
    
    const dirLightHelper1 = new THREE.DirectionalLightHelper( dirLight1, 5 );
    const dirLightHelper2 = new THREE.DirectionalLightHelper( dirLight2, 5 );
    this.scene.add(dirLightHelper1);
    this.scene.add(dirLightHelper2);

    // Ambient Occlusion
    const normalPass: POSTPROCESSING.NormalPass = new POSTPROCESSING.NormalPass(this.scene, this.camera, {
      resolutionScale: 1.0
    });
    const ssaoEffect = new POSTPROCESSING.SSAOEffect(this.camera, normalPass.renderTarget.texture, {
      blendFunction: POSTPROCESSING.BlendFunction.MULTIPLY,
      samples: 16,
      rings: 7,
      distanceThreshold: 0.975,
      distanceFalloff: 1.0,
      rangeThreshold: 0.025,
      rangeFalloff: 0.1,
      luminanceInfluence: 0.702,
      radius: 49.46,
      scale: 0.8,
      bias: 0.665
    });
    ssaoEffect.blendMode.opacity.value = 3.0;

    const renderPass: POSTPROCESSING.RenderPass = new POSTPROCESSING.RenderPass(this.scene, this.camera);
    renderPass.renderToScreen = false;
    const effectPass: POSTPROCESSING.EffectPass = new POSTPROCESSING.EffectPass(this.camera, ssaoEffect);
    effectPass.renderToScreen = true;

    this.composer.addPass(renderPass);
    this.composer.addPass(normalPass);
    this.composer.addPass(effectPass);

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
          floorMesh.receiveShadow = false;
          const material: THREE.MeshStandardMaterial = new THREE.MeshStandardMaterial();
          material.roughness = 0.8;
          floorMesh.material = material;
          
        });

        const wallMeshes: THREE.Mesh[] = ThreeUtils.getMeshesByBuildingPart(gltf.scene, "Wall");
        wallMeshes.forEach(wallMesh => {
          wallMesh.castShadow = false;
          wallMesh.receiveShadow = false;
          const material: THREE.MeshStandardMaterial = new THREE.MeshStandardMaterial();
          material.roughness = 0.8;
          material.color = new THREE.Color( 0xBDBDBD );
          wallMesh.material = material;
          
        });

        this.scene.add(gltf.scene);
      },
    );

    // Dev gui for monitoring three.js performance
    // This should be removed or commented out once the 3D viewer correctly.
    this.devGui = new DevGui(effectPass, normalPass, ssaoEffect);

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
    // Start monitoring frame
    this.devGui.stats.begin();

    // Render scene
    this.composer.render();
    // this.renderer.render(this.scene, this.camera);

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
      this.composer.setSize(width, height);
      this.camera.aspect = width / height;
      this.camera.updateProjectionMatrix();
    }
  }

}
