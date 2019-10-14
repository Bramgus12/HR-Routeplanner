import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import { GLTFLoader, GLTF } from 'three/examples/jsm/loaders/GLTFLoader';
import { ThreeUtils } from './three-utils'
import { MeshStandardMaterial, LoadingManager } from 'three';

@Component({
  selector: 'app-building-viewer',
  templateUrl: './building-viewer.component.html',
  styleUrls: ['./building-viewer.component.scss']
})
export class BuildingViewerComponent implements OnInit {
  @ViewChild('threejscontainer') threejsContainer: ElementRef;

  private scene: THREE.Scene;
  private camera: THREE.PerspectiveCamera;
  private renderer: THREE.WebGLRenderer;
  private orbitControls: OrbitControls;

  constructor() {
    // Setup the three.js scene
    this.scene = new THREE.Scene();
    this.scene.background = new THREE.Color(0x505050);

    // Camera and renderer
    this.camera = new THREE.PerspectiveCamera(75, 1280 / 720, 0.1, 1000);
    this.renderer = new THREE.WebGLRenderer({ antialias: true });
    this.renderer.setSize(1280, 720);
    this.orbitControls = new OrbitControls(this.camera, this.renderer.domElement);
    this.camera.position.z = 5;

    // Setup lighting
    const directionalLight: THREE.DirectionalLight = new THREE.DirectionalLight(0xffffff, 0.8);
    directionalLight.position.x = 50;
    directionalLight.position.z = 50;
    directionalLight.position.y = 50;
    this.scene.add(directionalLight);

    const hemisphereLight = new THREE.HemisphereLight( 0xffffff, 0x080820, 0.5 );
    this.scene.add( hemisphereLight );

    const ambientLight: THREE.AmbientLight = new THREE.AmbientLight(0xffffff, 0.5);
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
          floorMesh.castShadow = true;
          floorMesh.receiveShadow = true;
          const material: THREE.MeshStandardMaterial = new MeshStandardMaterial();
          material.roughness = 1;
          floorMesh.material = material;
          
        });

        const wallMeshes: THREE.Mesh[] = ThreeUtils.getMeshesByBuildingPart(gltf.scene, "Wall");
        wallMeshes.forEach(wallMesh => {
          wallMesh.castShadow = true;
          wallMesh.receiveShadow = true;
          const material: THREE.MeshStandardMaterial = new MeshStandardMaterial();
          material.roughness = 1;
          wallMesh.material = material;
          
        });
        
        this.scene.add(gltf.scene);
      },
    )

  }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    // Add three.js canvas element to the div in this component.
    this.threejsContainer.nativeElement.appendChild(this.renderer.domElement);
    this.resizeCanvasToContainer();
    this.animate();
  }

  // Main render loop for 3D view.
  animate(): void {
    this.resizeCanvasToContainer();
    this.orbitControls.update();

    this.renderer.render(this.scene, this.camera);
    window.requestAnimationFrame(() => this.animate());
  }

  resizeCanvasToContainer(): void {
    const width: number = this.threejsContainer.nativeElement.clientWidth;
    const height: number = this.threejsContainer.nativeElement.clientHeight;
    if (this.renderer.domElement.width != width || this.renderer.domElement.height != height) {
      this.renderer.setSize(width, height);
      this.camera.aspect = width / height;
      this.camera.updateProjectionMatrix();
    }
  }

}
