/**
 * Interfaz que representa una ubicación geográfica
 */
export interface Ubicacion {
  id: number;
  nombre: string;
  provincia?: string;
  comunidad_autonoma?: string;
  codigo_postal?: string;
  activo: boolean;
} 