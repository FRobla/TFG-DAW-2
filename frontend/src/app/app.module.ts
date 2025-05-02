import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LandingComponent } from './paginas/landing/landing.component';
import { PrincipalComponent } from './paginas/principal/principal.component';
import { ResultadoBusquedaComponent } from './paginas/resultado-busqueda/resultado-busqueda.component';
import { RouterModule, Routes } from '@angular/router';
import { DetallesAnuncioComponent } from './paginas/detalles-anuncio/detalles-anuncio.component';
import { LoginComponent } from './paginas/login/login.component';
import { CategoriasComponent } from './entidades/categoria/categoria.component';
import { HeroBuscadorComponent } from './recursos/hero-buscador/hero-buscador.component';
import { NavbarBusquedaComponent } from './recursos/navbar-busqueda/navbar-busqueda.component';
import { FooterComponent } from './recursos/footer/footer.component';
import { ErrorComponent } from './recursos/error/error.component';

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
    NavbarBusquedaComponent,
    FooterComponent,
    ErrorComponent,
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


