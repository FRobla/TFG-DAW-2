import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LandingComponent } from './landing/landing.component';
import { PrincipalComponent } from './principal/principal.component';
import { ResultadoBusquedaComponent } from './resultado-busqueda/resultado-busqueda.component';
import { RouterModule, Routes } from '@angular/router';
import { DetallesAnuncioComponent } from './detalles-anuncio/detalles-anuncio.component';
import { LoginComponent } from './login/login.component';
import { CategoriasComponent } from './categoria/categoria.component';
import { HeroBuscadorComponent } from './hero-buscador/hero-buscador.component';
import { NavbarBusquedaComponent } from './navbar-busqueda/navbar-busqueda.component';

@NgModule({
  declarations: [
    AppComponent,
    LandingComponent,
    PrincipalComponent,
    ResultadoBusquedaComponent,
    DetallesAnuncioComponent,
    LoginComponent,
    CategoriasComponent,
    HeroBuscadorComponent,
    NavbarBusquedaComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }


