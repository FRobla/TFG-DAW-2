import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PrincipalComponent } from './paginas/principal/principal.component';
import { ResultadoBusquedaComponent } from './paginas/resultado-busqueda/resultado-busqueda.component';
import { DetallesAnuncioComponent } from './paginas/detalles-anuncio/detalles-anuncio.component';
import { LoginComponent } from './paginas/login/login.component';
import { RegistroComponent } from './paginas/registro/registro.component';
import { ErrorComponent } from './recursos/error/error.component';
import { UsuarioComponent } from './entidades/usuario/usuario.component';
import { LandingComponent } from './paginas/landing/landing.component';
import { CategoriasComponent } from './entidades/categoria/categoria.component';
import { AuthGuard } from './auth/auth.guard';
import { AnuncioComponent } from './entidades/anuncio/anuncio.component';
import { ImpresoraComponent } from './entidades/impresora/impresora.component';

const routes: Routes = [
  { 
    path: '', 
    component: LandingComponent 
  },
  { 
    path: 'servicios', 
    component: PrincipalComponent 
  },
  { 
    path: 'resultados-busqueda', 
    component: ResultadoBusquedaComponent 
  },
  { 
    path: 'detalles-anuncio/:id', 
    component: DetallesAnuncioComponent 
  },
  { 
    path: 'login', 
    component: LoginComponent 
  },
  { 
    path: 'registro', 
    component: RegistroComponent 
  },
  // Rutas administrativas (acceso público temporal)
  { path: 'admin', component: UsuarioComponent },
  { 
    path: 'admin', 
    children: [
      { path: 'usuarios', component: UsuarioComponent },
      { path: 'categorias', component: CategoriasComponent },
      { path: 'anuncios', component: AnuncioComponent },
      { path: 'impresoras', component: ImpresoraComponent }
    ]
  },
  { 
    path: 'error', 
    component: ErrorComponent 
  },
  { 
    path: '**', 
    redirectTo: 'error' 
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    scrollPositionRestoration: 'enabled',  // Restaura la posición del scroll al navegar
    anchorScrolling: 'enabled',          // Habilita el desplazamiento a anchors
    scrollOffset: [0, 64]                // Offset para tener en cuenta el navbar fijo
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
