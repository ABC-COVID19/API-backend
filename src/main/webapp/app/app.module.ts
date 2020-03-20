import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { Abccovid19SharedModule } from 'app/shared/shared.module';
import { Abccovid19CoreModule } from 'app/core/core.module';
import { Abccovid19AppRoutingModule } from './app-routing.module';
import { Abccovid19HomeModule } from './home/home.module';
import { Abccovid19EntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    Abccovid19SharedModule,
    Abccovid19CoreModule,
    Abccovid19HomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    Abccovid19EntityModule,
    Abccovid19AppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class Abccovid19AppModule {}
