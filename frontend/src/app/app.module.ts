import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LandingComponent } from './paginas/landing/landing.component';
import { PrincipalComponent } from './paginas/principal/principal.component';
import { ResultadoBusquedaComponent } from './paginas/resultado-busqueda/resultado-busqueda.component';
import { RouterModule, Routes } from '@angular/router';
import { DetallesAnuncioComponent } from './paginas/detalles-anuncio/detalles-anuncio.component';
import { LoginComponent } from './paginas/login/login.component';
import { RegistroComponent } from './paginas/registro/registro.component';
import { CategoriasComponent } from './entidades/categoria/categoria.component';
import { HeroBuscadorComponent } from './recursos/hero-buscador/hero-buscador.component';
import { NavbarBusquedaComponent } from './recursos/navbar-busqueda/navbar-busqueda.component';
import { FooterComponent } from './recursos/footer/footer.component';
import { ErrorComponent } from './recursos/error/error.component';
import { ErrorInterceptor } from './recursos/error/error.interceptor';
import { UsuarioComponent } from './entidades/usuario/usuario.component';
import { NavbarAdminComponent } from './recursos/navbar-admin/navbar-admin.component';
import { SelectorCategoriaComponent } from './recursos/selector-categoria/selector-categoria.component';
import { AnuncioComponent } from './entidades/anuncio/anuncio.component';
import { ImpresoraComponent } from './entidades/impresora/impresora.component';
import { PerfilComponent } from './paginas/perfil/perfil.component';

@NgModule({
  declarations: [
    AppComponent,
    LandingComponent,
    PrincipalComponent,
    ResultadoBusquedaComponent,
    DetallesAnuncioComponent,
    LoginComponent,
    RegistroComponent,
    CategoriasComponent,
    HeroBuscadorComponent,
    NavbarBusquedaComponent,
    FooterComponent,
    ErrorComponent,
    UsuarioComponent,
    NavbarAdminComponent,
    SelectorCategoriaComponent,
    AnuncioComponent,
    ImpresoraComponent,
    PerfilComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
