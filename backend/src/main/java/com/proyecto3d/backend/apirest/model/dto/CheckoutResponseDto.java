package com.proyecto3d.backend.apirest.model.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la respuesta del checkout
 * Evita problemas de serialización con proxies de Hibernate
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutResponseDto {

    private Long id;
    private String numero_pedido;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date fecha_pedido;
    
    private String estado;
    private Double total;
    private Double subtotal;
    private String metodo_pago;
    private String referencia_pago;
    private String direccion_envio;
    private String codigo_postal;
    private String ciudad;
    private String provincia;
    private String notas_cliente;
    
    // Información del usuario simplificada
    private UsuarioSimpleDto usuario;
    
    // Detalles del pedido simplificados
    private List<DetallePedidoSimpleDto> detallesPedido;
    
    /**
     * DTO simplificado para información de usuario
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UsuarioSimpleDto {
        private Long id;
        private String nombre;
        private String email;
    }
    
    /**
     * DTO simplificado para detalles de pedido
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetallePedidoSimpleDto {
        private Long id;
        private Integer cantidad;
        private Double precio_unitario;
        private Double subtotal;
        private String especificaciones;
        private String tituloAnuncio;
        private String nombreProveedor;
    }
} 