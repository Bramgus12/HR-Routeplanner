import { Injectable, EventEmitter, Renderer2, RendererFactory2 } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Building, Node } from './shared/dataclasses';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  trigger: EventEmitter<null> = new EventEmitter();
  darkMode: boolean = false;

  private renderer: Renderer2;
  private API_URL = "/api/";
  private buildings: Building[] = [];
  private rooms: Node[] = [];

  constructor(private http: HttpClient, rendererFactory: RendererFactory2) { 
    this.renderer = rendererFactory.createRenderer(null, null);
  }

  /**
   * Causes the sidebar to trigger (open or close)
   */
  toggle(){
    this.trigger.next(null);
  }

  /**
   * Returns an Observable with Building[], first retrieves it from the API if the array is empty
   * This Observable is finite, no need to unsubscribe unless you want to cancel
   */
  getBuildings(){
    return new Observable<Building[]>(subscriber => {
      if(this.buildings.length == 0){
        this.http.get<Building[]>(this.API_URL + "building").subscribe(data => {
          this.buildings = data;
          subscriber.next(this.buildings);
          subscriber.complete();
        });
      } else {
        subscriber.next(this.buildings);
        subscriber.complete();
      }
    });
  }

  /**
   * Returns Observable with Node[], first retrieves it from the API if the array is empty
   * This Observable is finite, no need to unsubscribe unless you want to cancel
   */
  getRoomNodes(){
    return new Observable<Node[]>(subscriber => {
      if(this.rooms.length == 0){
        this.http.get<Node[]>(this.API_URL + "locationnodenetwork/room").subscribe(data => {
          this.rooms = data;
          subscriber.next(this.rooms);
          subscriber.complete();
        });
      } else {
        subscriber.next(this.rooms);
        subscriber.complete();
      }
    });
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
