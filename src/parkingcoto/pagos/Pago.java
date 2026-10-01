package parkingcoto.pagos;

import java.time.LocalDateTime;
import parkingcoto.tickets.TicketParqueo;

/**
 * Registro inmutable de un pago confirmado. 
 */
public class Pago {

    private final int id;
    private final TicketParqueo ticket;
    private final LocalDateTime fechaHora;
    private final long monto;
    private final TipoPago tipoPago;

    public Pago(int id, TicketParqueo ticket, LocalDateTime fechaHora, long monto, TipoPago tipoPago) {
        if (ticket == null || fechaHora == null || tipoPago == null) {
            throw new IllegalArgumentException("Ticket, fecha y tipo de pago son obligatorios");
        }
        if (monto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        this.id = id;
        this.ticket = ticket;
        this.fechaHora = fechaHora;
        this.monto = monto;
        this.tipoPago = tipoPago;
    }

    public int getId() { return id; }
    public TicketParqueo getTicket() { return ticket; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public long getMonto() { return monto; }
    public TipoPago getTipoPago() { return tipoPago; }

    @Override
    public String toString() {
        return "Pago #" + id + " [" + tipoPago + "] ₡" + monto + " ticket #" + ticket.getNumero();
    }
}
