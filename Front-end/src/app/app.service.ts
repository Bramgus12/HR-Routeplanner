import { Injectable, EventEmitter, Renderer2, RendererFactory2 } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  trigger: EventEmitter<null> = new EventEmitter();
  darkMode: boolean = false;

  private renderer: Renderer2;

  constructor(rendererFactory: RendererFactory2) { 
    this.renderer = rendererFactory.createRenderer(null, null);
  }

  /**
   * Causes the sidebar to trigger (open or close)
   */
  toggle(){
    this.trigger.next(null);
  }

  /**
   * Sets the dark mode (on or off)
   */
  setDarkmode(mode: boolean){
    if(mode) this.renderer.addClass(document.body, 'dark-theme');
    else this.renderer.removeClass(document.body, 'dark-theme');

    this.darkMode = mode;
    localStorage.setItem("dark-theme", mode.toString());
  }
}
