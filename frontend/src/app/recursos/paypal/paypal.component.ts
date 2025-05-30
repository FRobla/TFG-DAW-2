import { Component, OnInit, OnChanges, Input, Output, EventEmitter } from '@angular/core';
import { IPayPalConfig, ICreateOrderRequest } from 'ngx-paypal';

@Component({
  selector: 'app-paypal',
  standalone: false,
  templateUrl: './paypal.component.html',
  styleUrl: './paypal.component.css'
})
export class PaypalComponent implements OnInit, OnChanges {
  @Input() totalCarrito: number = 0;
  @Input() usuarioId: number = 1;
  @Input() elementosCarrito: any[] = [];
  @Output() pagoCompletado = new EventEmitter<any>();
  @Output() pagoCancelado = new EventEmitter<any>();
  @Output() pagoError = new EventEmitter<any>();

  public payPalConfig?: IPayPalConfig;

  ngOnInit() {
    this.initConfig();
  }

  ngOnChanges() {
    // Reinicializar configuración cuando cambien las propiedades de entrada
    if (this.totalCarrito > 0) {
      this.initConfig();
    }
  }

  private initConfig(): void {
    this.payPalConfig = {
      currency: 'EUR',
      clientId: 'AUd0GEMFmaV5uviQDweoOZjMuIJK5LqRk6QIzj23r-AemR_oeVOGTM6QPE4X7ByofAWsC4P-boQ5rgTF',
      createOrderOnClient: (data) => <ICreateOrderRequest>{
        intent: 'CAPTURE',
        purchase_units: [{
          amount: {
            currency_code: 'EUR',
            value: this.totalCarrito.toFixed(2)
          }
        }]
      },
      advanced: {
        commit: 'true'
      },
      style: {
        label: 'paypal',
        layout: 'vertical'
      },
      onApprove: (data, actions) => {
        console.log('onApprove - Pago aprobado', data);
        return actions.order.capture().then((details: any) => {
          console.log('Pago capturado:', details);
          
          // Emitir evento de pago completado
          const checkoutData = {
            usuarioId: this.usuarioId,
            metodoPago: 'paypal',
            referenciaPago: details.id,
            estado: 'en_proceso',
            direccionEnvio: 'Dirección por defecto',
            codigoPostal: '28000',
            ciudad: 'Madrid',
            provincia: 'Madrid',
            notasCliente: null,
            notasInternas: null
          };

          this.pagoCompletado.emit(checkoutData);
        });
      },
      onCancel: (data, actions) => {
        console.log('Pago cancelado:', data);
        
        // Emitir evento de pago cancelado
        const checkoutData = {
          usuarioId: this.usuarioId,
          metodoPago: 'paypal',
          referenciaPago: null,
          estado: 'cancelado',
          direccionEnvio: 'Dirección por defecto',
          codigoPostal: '28000',
          ciudad: 'Madrid',
          provincia: 'Madrid',
          notasCliente: null,
          notasInternas: 'Cancelado por el cliente'
        };

        this.pagoCancelado.emit(checkoutData);
      },
      onError: err => {
        console.log('Error en el pago:', err);
        this.pagoError.emit(err);
      },
      onClick: (data, actions) => {
        console.log('Botón PayPal clickeado:', data);
      }
    };
  }
}
