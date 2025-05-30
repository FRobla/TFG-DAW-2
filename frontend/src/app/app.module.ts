import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgxPayPalModule } from 'ngx-paypal';

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
import { FavoritoComponent } from './paginas/favorito/favorito.component';
import { CarritoComponent } from './paginas/carrito/carrito.component';
import { PaypalComponent } from './recursos/paypal/paypal.component';
import { PedidoComponent } from './entidades/pedido/pedido.component';

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
    FavoritoComponent,
    CarritoComponent,
    PaypalComponent,
    PedidoComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgxPayPalModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
