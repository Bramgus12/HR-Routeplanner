import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomepageComponent } from './homepage/homepage.component';
import { TestComponent } from './test/test.component';
import { MapsNavigationComponent } from './maps-navigation/maps-navigation.component';

const routes: Routes = [
  { path: '', component: HomepageComponent, data: { title: "HR Routeplanner" } },
  { path: 'test', component: TestComponent, data: { title: "Test" } },
  { path: 'maps-navigation', component: MapsNavigationComponent, data: { title: "Navigation" } }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class RoutingModule { }
