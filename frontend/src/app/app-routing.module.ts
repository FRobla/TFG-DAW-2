import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from './paginas/landing/landing.component';
import { PrincipalComponent } from './paginas/principal/principal.component';
import { ResultadoBusquedaComponent } from './paginas/resultado-busqueda/resultado-busqueda.component';
import { DetallesAnuncioComponent } from './paginas/detalles-anuncio/detalles-anuncio.component';
import { LoginComponent } from './paginas/login/login.component';

const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'servicios', component: PrincipalComponent },
  { path: 'resultados-busqueda', component: ResultadoBusquedaComponent },
  { path: 'detalles-anuncio/:id', component: DetallesAnuncioComponent },
  { path: 'login', component: LoginComponent },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
