import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LandingComponent } from './landing/landing.component';
import { PrincipalComponent } from './principal/principal.component';

const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'servicios', component: PrincipalComponent },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
