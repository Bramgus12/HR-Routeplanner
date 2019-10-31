/**
 * Class use for functions to monitor performance of the three.js building viewer.
 */
import * as Stats from 'stats.js';
export class DevGui {
  public stats: Stats;

  constructor(){    
    // Stats to monitor fps/memory/latency
    this.stats = new Stats();
    this.stats.showPanel(0);
    this.stats.dom.style.top = "64px";
  }

  appendToElement(element: HTMLElement){
    element.appendChild(this.stats.dom);
  }

}