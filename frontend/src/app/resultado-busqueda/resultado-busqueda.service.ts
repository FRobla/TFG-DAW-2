import { Injectable, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Categoria } from '../categoria/categoria';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ResultadoBusquedaService implements OnInit{

  constructor(private http: HttpClient, private router: Router) { }

  ngOnInit(): void {
    
  }

  private urlEndPoint: string = 'http://localhost:8080/api/categorias';
  
  cargarCategorias(): Observable<Categoria[]> {
    return this.http.get<Categoria[]>(`${this.urlEndPoint}`)
  }
}
