import { Building, TimeMode } from './dataclasses';

export class NavigationState {

  from: string;
  to: string;

  private _progress: number;
  private _steps: NavigationStep[]

  constructor(from: string, to: string, steps: NavigationStep[]) {
    this.from = from;
    this.to = to;
    this._progress = -1;
    this._steps = steps;
  }

  private handleError(errMsg: string): null {
    throw new NavigationStateError(errMsg, this._progress, this._steps);
    return null;
  }

  get currentStep(){
    if(this._progress < 0 || this._progress > this._steps.length-1)
      return this.handleError("Current step is Out-of-Bounds!");

    const step = this._steps[this._progress];

    return step;
  }

  get currentComponentUrl(){
    return this.currentStep.componentUrl
  }

  get nextComponentUrl(){
    const next = this._progress+1;
    
    if(next < 0 || next > this._steps.length-1)
      return this.handleError("Next step is Out-of-Bounds!");

    return this._steps[next].componentUrl;
  }

  getNextStep(currentComponentUrl: string){
    this._progress +=1;
    const step = this.currentStep;
    
    if(step == null ||
      step.componentUrl == currentComponentUrl &&
      (step.componentUrl == "building-navigation" && step.data.hasOwnProperty("building") && step.data.hasOwnProperty("fromNode") && step.data.hasOwnProperty("toNode")) ||
      (step.componentUrl == "maps-navigation" && step.data.hasOwnProperty("departNow") && step.data.hasOwnProperty("timeMode") && step.data.hasOwnProperty("time"))
    ) {
      if(step == null) console.warn("Step is null, this shouldn't exec!") // TEMP
      return step;
    } else {
      return this.handleError("Step is invalid!");
    }
  }
}

class NavigationStateError extends Error {
  progress: number;
  steps: NavigationStep[];

  constructor(message: string, progress: number, steps: NavigationStep[]){
    super(message);
    this.progress = progress;
    this.steps = steps;
  }
}

export interface NavigationStep {
  componentUrl: string,
  data: BuildingStep | MapsStep
}

export interface BuildingStep {
  locationName: string,
  fromNode: number,
  toNode: number,
}

export interface MapsStep {
  departNow: boolean,
  timeMode: TimeMode,
  time: string
}
