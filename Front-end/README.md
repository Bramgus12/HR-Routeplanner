# ProjectC Front-end

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 7.3.9.

#### Index:
- [Angular](#angular)
- [Three.js](#threejs)
- [Navigation APIs](#navigation-apis)

---
## Angular

#### Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

#### Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

#### Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory. Use the `--prod` flag for a production build.

#### Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

#### Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via [Protractor](http://www.protractortest.org/).

#### Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI README](https://github.com/angular/angular-cli/blob/master/README.md).

---

## Three.js

#### Import three in your component
```
import { ... } from '@angular/core';
import * as THREE from 'three';
// or
import { ... } from 'three';

@Component({ ... })
export class ...Component implements OnInit {

  constructor() { }

  ngOnInit() {
    const scene = new THREE.Scene;
    ...
  }
  
}
```

---

## Navigation APIs

* [openroute service](https://openrouteservice.org/) for general navigation
  * limited request rates (GeocodeSearch: 1000*, Directions: 2000*)(* per 24 hours)
* [openOV](https://openov.nl/) / [OVAPI](https://github.com/skywave/KV78Turbo-OVAPI/wiki) for public transport
  * openOV has a lot of datasets, but a route planner has to be made by ourselves
  * OVAPI isn't meant for mass requests, but a good example of how to create a REST based backend application
