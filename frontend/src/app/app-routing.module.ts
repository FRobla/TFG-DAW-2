import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from './landing/landing.component';
import { PrincipalComponent } from './principal/principal.component';
import { ResultadoBusquedaComponent } from './resultado-busqueda/resultado-busqueda.component';
import { DetallesAnuncioComponent } from './detalles-anuncio/detalles-anuncio.component';
import { LoginComponent } from './login/login.component';

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
