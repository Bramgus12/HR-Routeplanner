/**
 * Class use for functions to monitor performance of the three.js building viewer.
 */
import { GUI } from 'three/examples/jsm/libs/dat.gui.module.js';
import * as Stats from 'stats.js';
import * as POSTPROCESSING from "postprocessing";
export class DevGui {
  public gui: GUI;
  public stats: Stats;

  constructor(effectPass:POSTPROCESSING.EffectPass, normalPass: POSTPROCESSING.NormalPass, ssaoEffect: POSTPROCESSING.SSAOEffect){
    // Dev GUI for Ambient Occlusion
    // Adapted from the example of the postprocessing module
    // https://github.com/vanruesc/postprocessing/blob/master/public/demo/index.js
    this.gui = new GUI( {autoPlace: false} );
    {
      var menu = this.gui;
      var blendMode = ssaoEffect.blendMode;
      var uniforms = ssaoEffect.uniforms;
      var RenderMode = {
        DEFAULT: 0,
        NORMALS: 1
      };
      var params = {
        "distance": {
          "threshold": uniforms.get("distanceCutoff").value.x,
          "falloff": uniforms.get("distanceCutoff").value.y - uniforms.get("distanceCutoff").value.x
        },
        "proximity": {
          "threshold": uniforms.get("proximityCutoff").value.x,
          "falloff": uniforms.get("proximityCutoff").value.y - uniforms.get("proximityCutoff").value.x
        },
        "lum influence": uniforms.get("luminanceInfluence").value,
        "scale": uniforms.get("scale").value,
        "bias": uniforms.get("bias").value,
        "render mode": RenderMode.DEFAULT.toString(),
        "opacity": blendMode.opacity.value,
        "blend mode": blendMode.blendFunction
      };

      menu.add(params, "render mode", RenderMode).onChange(() => {
        var mode = Number.parseInt(params["render mode"]);
        effectPass.enabled = mode === RenderMode.DEFAULT;
        normalPass.renderToScreen = mode === RenderMode.NORMALS;
        effectPass.recompile();
      });
      menu.add(ssaoEffect, "samples").min(1).max(32).step(1).onChange(function () {
        return effectPass.recompile();
      });
      menu.add(ssaoEffect, "rings").min(1).max(16).step(1).onChange(function () {
        return effectPass.recompile();
      });
      menu.add(ssaoEffect, "radius").min(0.01).max(200.0).step(0.01);
      menu.add(params, "lum influence").min(0.0).max(1.0).step(0.001).onChange(function () {
        uniforms.get("luminanceInfluence").value = params["lum influence"];
      });
      var f = menu.addFolder("Distance Cutoff");
      f.add(params.distance, "threshold").min(0.0).max(1.0).step(0.001).onChange(function () {
        ssaoEffect.setDistanceCutoff(params.distance.threshold, params.distance.falloff);
      });
      f.add(params.distance, "falloff").min(0.0).max(1.0).step(0.001).onChange(function () {
        ssaoEffect.setDistanceCutoff(params.distance.threshold, params.distance.falloff);
      });
      f = menu.addFolder("Proximity Cutoff");
      f.add(params.proximity, "threshold").min(0.0).max(0.05).step(0.0001).onChange(function () {
        ssaoEffect.setProximityCutoff(params.proximity.threshold, params.proximity.falloff);
      });
      f.add(params.proximity, "falloff").min(0.0).max(0.1).step(0.0001).onChange(function () {
        ssaoEffect.setProximityCutoff(params.proximity.threshold, params.proximity.falloff);
      });
      menu.add(params, "bias").min(-1.0).max(1.0).step(0.001).onChange(function () {
        uniforms.get("bias").value = params.bias;
      });
      menu.add(params, "scale").min(0.0).max(2.0).step(0.001).onChange(function () {
        uniforms.get("scale").value = params.scale;
      });
      menu.add(params, "opacity").min(0.0).max(3.0).step(0.01).onChange(function () {
        blendMode.opacity.value = params.opacity;
      });
      menu.add(params, "blend mode", POSTPROCESSING.BlendFunction).onChange(function () {
        blendMode.blendFunction = Number.parseInt(params["blend mode"]);
        effectPass.recompile();
      });
    }
    
    // Stats to monitor fps/memory/latency
    this.stats = new Stats();
    this.stats.showPanel(0);
  }

  appendToElement(element: HTMLElement){
    element.appendChild(this.gui.domElement);
    element.appendChild(this.stats.dom);
  }

}