import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule }   from '@angular/forms';

import { AgmCoreModule } from '@agm/core';
import { NgxMaterialTimepickerModule } from 'ngx-material-timepicker';
import { RoutingModule } from './routing.module';
import { MaterialModule } from './material.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { HomepageComponent } from './homepage/homepage.component';
import { TestComponent, TestCreateDialog, TestDeleteDialog } from './test/test.component';
import { SidenavComponent } from './sidenav/sidenav.component';

import { BuildingNavigationComponent } from './building-navigation/building-navigation.component';
import { BuildingViewerComponent } from './building-navigation/building-viewer/building-viewer.component';
import { MapsNavigationComponent } from './maps-navigation/maps-navigation.component';

import { keys } from './3rdparty/api_keys';
import { SafeHTMLPipe } from './pipes/safe-html.pipe';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '../environments/environment';

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    RoutingModule,
    MaterialModule,
    NgxMaterialTimepickerModule,
    AgmCoreModule.forRoot({ apiKey: keys.google_maps, region: 'NL', libraries: ['places', 'directions'] })
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: environment.production })
  ],
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomepageComponent,
    TestComponent,
    TestCreateDialog,
    TestDeleteDialog,
    SidenavComponent,
    BuildingNavigationComponent,
    BuildingViewerComponent,
    MapsNavigationComponent,
    SafeHTMLPipe
  ],
  entryComponents: [
    TestCreateDialog,
    TestDeleteDialog
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
