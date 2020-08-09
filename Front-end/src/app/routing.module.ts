import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomepageComponent } from './homepage/homepage.component';
import { ElectiveCoursesComponent } from './elective-courses/elective-courses.component';
import { ElectiveCourseDescriptionComponent } from './elective-courses/elective-course-description/elective-course-description.component';
import { BuildingNavigationComponent } from './building-navigation/building-navigation.component';
import { MapsNavigationComponent } from './maps-navigation/maps-navigation.component';

const routes: Routes = [
  { path: '', component: HomepageComponent, data: { title: "HR Routeplanner" } },
  // { path: 'elective-courses', component: ElectiveCoursesComponent, data: { title: "Elective Course List" } },
  // { path: 'elective-course/:code/:group', component: ElectiveCourseDescriptionComponent, data: { title: "Elective Course" } },
  { path: 'building-navigation', component: BuildingNavigationComponent, data: { title: "Navigation" } },
  { path: 'maps-navigation', component: MapsNavigationComponent, data: { title: "Navigation" } },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class RoutingModule { }
