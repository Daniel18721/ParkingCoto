package parkingcoto.servicio;

import java.time.LocalDateTime;
import java.util.Objects;
import parkingcoto.pagos.Pago;
import parkingcoto.pagos.TipoPago;
import parkingcoto.tickets.EstadoTicket;
import parkingcoto.tickets.TicketParqueo;

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

    public Pago pagar(int numeroTicket, TipoPago tipoPago, LocalDateTime fechaHoraPago) {

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

        int idPago = parqueo.generarIdPago();

        Pago pago = new Pago(
                idPago,
                ticket,
                fechaHoraPago,
                ticket.getMontoFinal(),
                tipoPago
        );

        parqueo.registrarPago(pago);

        ticket.marcarPagado();

        ticket.getEspacio().liberar();

        return pago;
    }
}