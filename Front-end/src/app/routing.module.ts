import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomepageComponent } from './homepage/homepage.component';
import { TestComponent } from './test/test.component';
import { BuildingNavigationComponent } from './building-navigation/building-navigation.component';

const routes: Routes = [
  { path: '', component: HomepageComponent, data: { title: "HR Routeplanner" } },
  { path: 'test', component: TestComponent, data: { title: "Test" } },
  { path: 'building-navigation', component: BuildingNavigationComponent, data: { title: "Building Navigation" } }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class RoutingModule { }
