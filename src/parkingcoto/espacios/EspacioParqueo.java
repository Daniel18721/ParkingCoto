package parkingcoto.espacios;

import java.util.Objects;
import parkingcoto.vehiculos.TipoVehiculo;
import parkingcoto.vehiculos.Vehiculo;

public class EspacioParqueo {

    private final String numero;
    private final TipoEspacio tipo;
    private EstadoEspacio estado;

    public EspacioParqueo(String numero, TipoEspacio tipo) {
        if (numero == null || numero.isBlank()) 
            throw new IllegalArgumentException("El número del espacio es obligatorio.");
        
        this.numero = numero.strip();
        this.tipo = Objects.requireNonNull(tipo, "El tipo de espacio es obligatorio.");
        this.estado = EstadoEspacio.DISPONIBLE;
    }

    public String getNumero() {
        return numero;
    }

    public TipoEspacio getTipo() {
        return tipo;
    }

    public EstadoEspacio getEstado() {
        return estado;
    }

    // Si el vehículo no es uno de los ya establecidos devuelve false
    private boolean esCompatibleCon(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "El vehículo es obligatorio.");
        TipoVehiculo tipoVehiculo = vehiculo.getTipoVehiculo();

        return switch (tipo) {
            case MOTOCICLETA -> tipoVehiculo == TipoVehiculo.MOTOCICLETA;

            case AUTOMOVIL -> tipoVehiculo == TipoVehiculo.AUTOMOVIL;

            case CARGA -> tipoVehiculo == TipoVehiculo.VEHICULO_CARGA;
        };
    }
    
    // Retorna true si el vehículo cumple con las validaciones necesariar para ser asignado
    public boolean puedeAsignarseA(Vehiculo vehiculo) {
        boolean compatible = esCompatibleCon(vehiculo);

        return estado == EstadoEspacio.DISPONIBLE && compatible;
    }

    // Ocupa el espacio si el vehículo existe, el espacio está disponible
    // y su tipo es compatible con el vehículo.
    public void ocupar(Vehiculo vehiculo) {

        Objects.requireNonNull( vehiculo, "El vehículo es obligatorio.");

        if (estado == EstadoEspacio.OCUPADO) 
            throw new IllegalStateException("El espacio ya está ocupado.");
        
        if (estado == EstadoEspacio.FUERA_DE_SERVICIO) 
            throw new IllegalStateException("El espacio está fuera de servicio.");
        

        if (!esCompatibleCon(vehiculo)) 
            throw new IllegalArgumentException("El vehículo no es compatible con el espacio.");
        
        estado = EstadoEspacio.OCUPADO;
    }

    // Devuelve un espacio ocupado al estado disponible.
    public void liberar() {

        if (estado != EstadoEspacio.OCUPADO) 
            throw new IllegalStateException("Solo puede liberarse un espacio ocupado.");
        
        estado = EstadoEspacio.DISPONIBLE;
    }

    // Retira temporalmente un espacio disponible para impedir su asignación.
    public void ponerFueraDeServicio() {

        if (estado != EstadoEspacio.DISPONIBLE) 
            throw new IllegalStateException("Solo un espacio disponible puede ponerse "+ "fuera de servicio.");
        
        estado = EstadoEspacio.FUERA_DE_SERVICIO;
    }

    // Reactiva un espacio que estaba fuera de servicio.
    public void habilitar() {

        if (estado != EstadoEspacio.FUERA_DE_SERVICIO) 
            throw new IllegalStateException("Solo puede habilitarse un espacio "+ "fuera de servicio.");
        
        estado = EstadoEspacio.DISPONIBLE;
    }

    @Override
    public String toString() {
        return "Espacio: " + numero + " | Tipo: " + tipo + " | Estado: " + estado;
    }
}