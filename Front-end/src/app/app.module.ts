import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule }   from '@angular/forms';

import { RoutingModule } from './routing.module';
import { MaterialModule } from './material.module';
import { NgxMaterialTimepickerModule } from 'ngx-material-timepicker';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { AgmCoreModule } from '@agm/core';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { HomepageComponent } from './homepage/homepage.component';
import { TestComponent, TestCreateDialog, TestDeleteDialog } from './test/test.component';
import { SidenavComponent } from './sidenav/sidenav.component';
import { MapsNavigationComponent } from './maps-navigation/maps-navigation.component';

import { keys } from './3rdparty/api_keys'; 

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    RoutingModule,
    MaterialModule,
    NgxMaterialTimepickerModule,
    LeafletModule.forRoot(),
    AgmCoreModule.forRoot({ apiKey: keys.google_maps, region: 'NL', libraries: ['places'] })
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
    MapsNavigationComponent
  ],
  entryComponents: [
    TestCreateDialog,
    TestDeleteDialog
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
