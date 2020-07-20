import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule }   from '@angular/forms';

import { NgxMaterialTimepickerModule } from 'ngx-material-timepicker';
import { RoutingModule } from './routing.module';
import { MaterialModule } from './material.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { HomepageComponent } from './homepage/homepage.component';
import { SidenavComponent } from './sidenav/sidenav.component';
import { NextStateButtonComponent, NextStateDialog, ArrivalStateDialog } from './shared/next-state-button/next-state-button.component';

import { BuildingNavigationComponent } from './building-navigation/building-navigation.component';
import { BuildingViewerComponent } from './building-navigation/building-viewer/building-viewer.component';
import { MapsNavigationComponent } from './maps-navigation/maps-navigation.component';

import { SafeHTMLPipe } from './pipes/safe-html.pipe';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '../environments/environment';
import { ElectiveCoursesComponent } from './elective-courses/elective-courses.component';
import { ElectiveCourseDescriptionComponent } from './elective-courses/elective-course-description/elective-course-description.component';

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    RoutingModule,
    MaterialModule,
    NgxMaterialTimepickerModule,
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: environment.production })
  ],
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomepageComponent,
    SidenavComponent,
    BuildingNavigationComponent,
    BuildingViewerComponent,
    MapsNavigationComponent,
    SafeHTMLPipe,
    ElectiveCoursesComponent,
    ElectiveCourseDescriptionComponent,
    NextStateButtonComponent,
    NextStateDialog,
    ArrivalStateDialog
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
