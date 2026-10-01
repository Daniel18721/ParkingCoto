package parkingcoto.servicio;

import java.time.LocalDateTime;
import java.util.Objects;
import parkingcoto.pagos.Pago;
import parkingcoto.pagos.TipoPago;
import parkingcoto.tickets.EstadoTicket;
import parkingcoto.tickets.TicketParqueo;
import parkingcoto.espacios.EstadoEspacio;


/**
 * [PERSONA 3]
 * Coordina el pago de un ticket cerrado.
 */
public class ServicioPago {

    private final Parqueo parqueo;

    public ServicioPago(Parqueo parqueo) {
        this.parqueo = Objects.requireNonNull(
                parqueo, "El parqueo es obligatorio");
    }

    // Valida el ticket, registra el pago y completa la salida liberando el espacio.
    public Pago pagar(
            int numeroTicket,
            TipoPago tipoPago,
            LocalDateTime fechaHoraPago) {

        if (tipoPago == null) {
            throw new IllegalArgumentException(
                    "El tipo de pago es obligatorio");
        }

        if (fechaHoraPago == null) {
            throw new IllegalArgumentException(
                    "La fecha y hora del pago son obligatorias");
        }

        TicketParqueo ticket = parqueo.buscarTicket(numeroTicket)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe el ticket " + numeroTicket));

        if (ticket.getEstado() != EstadoTicket.CERRADO) {
            throw new IllegalStateException(
                    "Solo se puede pagar un ticket cerrado");
        }

        if (fechaHoraPago.isBefore(ticket.getFechaHoraSalida())) {
            throw new IllegalArgumentException(
                    "El pago no puede ser anterior al cierre del ticket");
        }

        // Comprueba antes del cobro la condición necesaria para liberar el espacio.
        if (ticket.getEspacio().getEstado() != EstadoEspacio.OCUPADO) {
            throw new IllegalStateException(
                    "El espacio del ticket no está ocupado");
        }

        int idPago = parqueo.generarIdPago();

        Pago pago = new Pago(
                idPago,
                ticket,
                fechaHoraPago,
                ticket.getMontoFinal(),
                tipoPago
        );

        // Si el registro se rechaza, el ticket y el espacio siguen como estaban.
        parqueo.registrarPago(pago);

        ticket.marcarPagado();
        ticket.getEspacio().liberar();

        return pago;
    }
}