# ProjectC Front-end

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 7.3.9.

#### Index:
- [Installation](#installation)
- [File structure](#file-structure)
- [Angular](#angular)
- [Three.js](#threejs)
- [Navigation APIs](#navigation-apis)

---

## Installation

### Install NodeJS
The front-end uses [NodeJS](https://nodejs.org/) to run [angular](https://angular.io/), Node also installs NPM which is the pakage manager for NodeJS


### Install `@angular/cli`
```
  npm install -g @angular/cli
```

### Install node dependencies
Make sure you are in the `Front-end` directory and run
```
  npm install
```

### Install `api_keys.ts`
* Download `api_keys.ts` from google drive or other safe storage location
* Place the file in `src/app/3rdparty`

Now you are good to go

### Start local development server
```
  ng serve
```

---

## File structure
Important files

```
.
+-- dist <-- contains build project (run "ng build [--prod]" to build project)
+-- src
:   +-- app
:   :   +-- 3rdparty <-- services for 3rdparty APIs
:   :   +-- ... <-- other components
:   :   +-- pipes <-- pipes (see angular documentation)
:   :   +-- shared <-- typescript files that can be used in all components
:   :   +-- ... <-- other components
:   :   |-- app.component.html <-- root component html template
:   :   |-- app.component.scss <-- root component stylesheet
:   :   |-- app.component.ts <-- root component controller
:   :   |-- app.module.ts <-- main module, imports all modules & components
:   :   |-- app.service.ts <-- root component service (can also be used in other components)
:   :   |-- material.module.ts <-- module imports all angular material modules
:   :   |-- routing.module.ts <-- modules exports routes (urls mapped to components)
:   +-- assets <-- all assets
:   +-- sass
:   :   |-- helpers.scss <-- helper mixins (functions)
:   +-- themes
:   :   |-- app-theme.scss <-- sets main css & theme based classes
:   :   |-- hro-theme.scss <-- sets the theme
:   |-- index.html <-- main html template
:   |-- styles.scss <-- main stylesheet (scss) file, unused

```

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

### Drawing a route in the building navigation page.
To draw call the routing engine from the building navigation page, provide the parameters `locationName` `to` and `from` in the url.

For example:
```
http://localhost:8080/building-navigation?locationName=Wijnhaven&from=0&to=744
```

---

## Navigation APIs

* Google Maps using `@agm/core` and `@types/googlemaps`
