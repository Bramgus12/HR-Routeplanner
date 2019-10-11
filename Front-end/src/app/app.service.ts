import { Injectable, Output, EventEmitter, Renderer2, RendererFactory2 } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  @Output() trigger: EventEmitter<null> = new EventEmitter();

  private renderer: Renderer2;

  constructor(rendererFactory: RendererFactory2) { 
    this.renderer = rendererFactory.createRenderer(null, null);
  }

  toggle(){
    this.trigger.next(null);
  }

  setDarkmode(mode: boolean){
    if(mode) this.renderer.addClass(document.body, 'dark-theme');
    else this.renderer.removeClass(document.body, 'dark-theme');

    localStorage.setItem("dark-theme", mode.toString());
  }
}
