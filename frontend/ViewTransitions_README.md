# ViewTransitions en el Dashboard

## Descripción
Este proyecto implementa ViewTransitions nativas del navegador para crear transiciones fluidas entre las diferentes páginas del dashboard de administración. Todas las entidades (usuarios, categorías, impresoras, pedidos y anuncios) siguen el mismo patrón simple y consistente.

## Tecnología utilizada
- **CSS View Transitions API** - API nativa del navegador para transiciones entre vistas
- **Angular Router** - Para detectar cambios de navegación automáticamente
- **Data Attributes** - Para marcar elementos específicos que requieren transiciones

## Archivos principales

### 1. `app.component.ts`
Contiene la lógica principal para manejar las ViewTransitions automáticas:
- Detecta cambios de navegación
- Prepara elementos para transiciones
- Asigna `view-transition-name` a elementos clave

### 2. `app.component.css`
Define todas las animaciones CSS para las transiciones:
- Transiciones de página completa
- Animaciones específicas para elementos del dashboard
- Efectos de entrada y salida personalizados

### 3. `navbar-admin.component.ts`
Implementa navegación con ViewTransitions:
- Método `navegarConTransicion()` para iniciar transiciones manuales
- Fallback para navegadores sin soporte

## Patrón de implementación

### Todas las entidades siguen el mismo patrón:

#### HTML Templates:
Cada componente de entidad utiliza los siguientes atributos `data-view-transition`:

```html
<!-- Contenedor de tabla principal -->
<div class="table-container" data-view-transition="card">
  <table>
    <tbody>
      <tr *ngFor="let item of items">
        <!-- Imagen/Icono -->
        <td>
          <div data-view-transition="image" [attr.data-view-id]="'entidad-img-' + item.id">
            <!-- Contenido de imagen -->
          </div>
        </td>
        
        <!-- Título/Nombre principal -->
        <td>
          <div data-view-transition="header" [attr.data-view-id]="'entidad-title-' + item.id">
            <!-- Contenido del título -->
          </div>
        </td>
        
        <!-- Badges/Etiquetas -->
        <td>
          <span data-view-transition="card" [attr.data-view-id]="'entidad-badge-' + item.id">
            <!-- Contenido del badge -->
          </span>
        </td>
      </tr>
    </tbody>
  </table>
</div>
```

#### TypeScript Components:
Todos los componentes implementan el método simplificado:

```typescript
cambiarPagina(pagina: number): void {
  if (pagina >= 1 && pagina <= this.totalPaginas) {
    this.paginaActual = pagina;
    this.aplicarPaginacion();
  }
}
```

## Elementos con transiciones por entidad

### Usuarios (`usuario.component.html`):
- `usuario-img-{id}`: Avatares/imágenes de perfil
- `usuario-title-{id}`: Nombres de usuario  
- `usuario-role-{id}`: Roles de usuario
- `usuario-fecha-{id}`: Fechas de registro

### Categorías (`categoria.component.html`):
- `categoria-icon-{id}`: Iconos de categoría
- `categoria-title-{id}`: Nombres de categoría
- `categoria-services-{id}`: Conteo de servicios

### Impresoras (`impresora.component.html`):
- `impresora-img-{id}`: Imágenes de impresoras
- `impresora-title-{id}`: Modelos de impresoras
- `impresora-precio-{id}`: Precios
- `impresora-category-{id}`: Categorías
- `impresora-stock-{id}`: Estado de stock

### Pedidos (`pedido.component.html`):
- `pedido-title-{id}`: Números de pedido
- `pedido-cliente-{id}`: Información de cliente
- `pedido-fecha-{id}`: Fechas de pedido
- `pedido-estado-{id}`: Estados de pedido
- `pedido-total-{id}`: Totales de pedido

### Anuncios (`anuncio.component.html`):
- `anuncio-img-{id}`: Imágenes de anuncios
- `anuncio-title-{id}`: Títulos de anuncios
- `anuncio-precio-{id}`: Precios
- `anuncio-category-{id}`: Categorías
- `anuncio-fecha-{id}`: Fechas de publicación

## Tipos de transiciones implementadas

### 1. `data-view-transition="card"` 
- **Uso**: Contenedores principales de tabla
- **Animación**: Scale + deslizamiento
- **Elementos**: `table-container`

### 2. `data-view-transition="image"`
- **Uso**: Imágenes, avatares, iconos
- **Animación**: Continuidad visual entre páginas
- **Elementos**: Avatares, imágenes de productos, iconos

### 3. `data-view-transition="header"`
- **Uso**: Títulos y nombres principales
- **Animación**: Deslizamiento suave
- **Elementos**: Nombres de usuario, títulos de anuncios, modelos

### 4. `data-view-transition="card"` (badges)
- **Uso**: Badges, etiquetas, precios
- **Animación**: Efecto bounce
- **Elementos**: Roles, estados, precios, categorías

## Configuración automática de navegación

Las ViewTransitions se activan automáticamente en todos los cambios de ruta gracias al interceptor en `app.component.ts`. No es necesaria configuración adicional en los componentes individuales.

### Navegación manual (opcional):
```typescript
navegarConTransicion(ruta: string): void {
  if ('startViewTransition' in document && document.startViewTransition) {
    document.startViewTransition(() => {
      this.router.navigateByUrl(ruta);
      return Promise.resolve();
    });
  } else {
    this.router.navigateByUrl(ruta);
  }
}
```

## Soporte de navegadores

### Navegadores compatibles:
- Chrome 111+
- Edge 111+
- Opera 97+

### Fallback automático:
Para navegadores sin soporte, se usa navegación estándar sin transiciones.

## Renderimiento y optimización

### Optimizaciones implementadas:
- `contain: layout` en elementos con `data-view-id`
- `will-change: transform, opacity` durante transiciones
- Duración optimizada de animaciones (0.2s - 0.6s)
- Animaciones escalonadas para tarjetas de estadísticas

### Accesibilidad:
- Respeta `prefers-reduced-motion` reduciendo duración de animaciones
- Mantiene la funcionalidad completa sin JavaScript

## Agregar ViewTransitions a nuevas entidades

### 1. HTML Template:
```html
<div class="table-container" data-view-transition="card">
  <table>
    <tr *ngFor="let item of items">
      <td>
        <div data-view-transition="image" [attr.data-view-id]="'nueva-entidad-img-' + item.id">
          <!-- Imagen/Icono -->
        </div>
      </td>
      <td>
        <div data-view-transition="header" [attr.data-view-id]="'nueva-entidad-title-' + item.id">
          <!-- Título principal -->
        </div>
      </td>
      <td>
        <span data-view-transition="card" [attr.data-view-id]="'nueva-entidad-badge-' + item.id">
          <!-- Badge/Etiqueta -->
        </span>
      </td>
    </tr>
  </table>
</div>
```

### 2. TypeScript Component:
```typescript
cambiarPagina(pagina: number): void {
  if (pagina >= 1 && pagina <= this.totalPaginas) {
    this.paginaActual = pagina;
    this.aplicarPaginacion();
  }
}
```

## Debugging

### Para debuguear transiciones:
1. Activar Developer Tools
2. Ir a la pestaña "Animations"
3. Las ViewTransitions aparecerán como "View Transition"
4. Se pueden inspeccionar los pseudo-elementos `::view-transition-*`

### Logs útiles:
```typescript
console.log('View Transitions API soportada:', 'startViewTransition' in document);
```

## Notas importantes

- Todas las entidades siguen el mismo patrón consistente
- Las transiciones funcionan automáticamente en toda la aplicación
- Los elementos deben tener `data-view-id` único por página
- Las transiciones se cancelan automáticamente si hay errores de navegación
- Los nombres de transición se limpian automáticamente para compatibilidad CSS
- No es necesaria configuración adicional en nuevas entidades - solo seguir el patrón establecido 