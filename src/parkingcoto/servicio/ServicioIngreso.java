package parkingcoto.servicio;

import java.time.LocalDateTime;
import java.util.Objects;
import parkingcoto.asignacion.AsignadorEspacio;
import parkingcoto.espacios.EspacioParqueo;
import parkingcoto.tickets.TicketParqueo;
import parkingcoto.vehiculos.Vehiculo;

/**
 * [PERSONA 3]
 * Coordina el proceso de ingreso de un vehículo al parqueo.
 */
public class ServicioIngreso {

    private final Parqueo parqueo;
    private final AsignadorEspacio asignador;

    public ServicioIngreso(Parqueo parqueo, AsignadorEspacio asignador) {
        this.parqueo = Objects.requireNonNull(
                parqueo, "El parqueo es obligatorio");

        this.asignador = Objects.requireNonNull(
                asignador, "El asignador es obligatorio");
    }

    public TicketParqueo ingresar(String placa, LocalDateTime fechaHoraEntrada) {

        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException(
                    "La placa es obligatoria");
        }

        if (fechaHoraEntrada == null) {
            throw new IllegalArgumentException(
                    "La fecha y hora de entrada son obligatorias");
        }

        Vehiculo vehiculo = parqueo.buscarVehiculo(placa)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe un vehículo registrado con placa " + placa));

        if (parqueo.tieneEstanciaPendiente(placa)) {
            throw new IllegalStateException(
                    "El vehículo ya tiene una estancia pendiente");
        }

        EspacioParqueo espacio = asignador
                .seleccionarEspacio(
                        vehiculo,
                        parqueo.consultarEspacios())
                .orElseThrow(() -> new IllegalStateException(
                        "No hay espacios disponibles para este vehículo"));

        int numeroTicket = parqueo.generarNumeroTicket();

        TicketParqueo ticket = new TicketParqueo(
                numeroTicket,
                vehiculo,
                espacio,
                fechaHoraEntrada
        );

        espacio.ocupar(vehiculo);

        try {
            parqueo.registrarTicket(ticket);
        } catch (RuntimeException ex) {

            espacio.liberar();

            throw ex;
        }

        return ticket;
    }
}