package parkingcoto.servicio;

import java.time.LocalDateTime;
import java.util.Objects;
import parkingcoto.tarifas.PoliticaTarifa;
import parkingcoto.tickets.TicketParqueo;

/**
 * [PERSONA 3]
 * Coordina la salida de un vehículo del parqueo.
 */
public class ServicioSalida {

    private final Parqueo parqueo;
    private final PoliticaTarifa politicaTarifa;

    public ServicioSalida(Parqueo parqueo, PoliticaTarifa politicaTarifa) {

        this.parqueo = Objects.requireNonNull(
                parqueo, "El parqueo es obligatorio");

        this.politicaTarifa = Objects.requireNonNull(
                politicaTarifa, "La política de tarifa es obligatoria");
    }

    public TicketParqueo salir(String placa, LocalDateTime fechaHoraSalida) {

        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException(
                    "La placa es obligatoria");
        }

        if (fechaHoraSalida == null) {
            throw new IllegalArgumentException(
                    "La fecha y hora de salida son obligatorias");
        }

        TicketParqueo ticket = parqueo.buscarTicketActivo(placa)
                .orElseThrow(() -> new IllegalStateException(
                        "El vehículo no tiene un ticket activo"));

        if (fechaHoraSalida.isBefore(ticket.getFechaHoraEntrada())) {
            throw new IllegalArgumentException(
                    "La salida no puede ser anterior a la entrada");
        }

        ticket.cerrar(fechaHoraSalida, politicaTarifa);

        return ticket;
    }
}