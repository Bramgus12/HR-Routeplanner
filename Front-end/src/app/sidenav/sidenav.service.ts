import { Injectable, Output, EventEmitter } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SidenavService {

  @Output() trigger: EventEmitter<null> = new EventEmitter();

  constructor() { }

  toggle(){
    this.trigger.next(null);
  }

}
