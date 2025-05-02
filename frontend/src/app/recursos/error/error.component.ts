import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-error',
  standalone: false,
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css']
})
export class ErrorComponent implements OnInit {
  errorCode: string = '404';
  errorMessage: string = 'Lo sentimos, la página que buscas no existe o ha sido movida.';
  errorTitle: string = 'Página no encontrada';

  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // Obtenemos los parámetros de error si existen
    this.route.queryParams.subscribe(params => {
      if (params['code']) {
        this.errorCode = params['code'];
      }
      
      if (params['message']) {
        this.errorMessage = params['message'];
      }

      if (params['title']) {
        this.errorTitle = params['title'];
      }
    });
  }

  volverAlInicio() {
    this.router.navigate(['/']);
  }

  explorarServicios() {
    this.router.navigate(['/servicios']);
  }
}
