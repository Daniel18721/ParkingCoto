package parkingcoto.pagos;

import java.time.LocalDateTime;
import parkingcoto.tickets.TicketParqueo;

public class Pago {

    private final int id;
    private final TicketParqueo ticket;
    private final LocalDateTime fechaHora;
    private final double monto;
    private final TipoPago tipoPago;

    public Pago(int id, TicketParqueo ticket, LocalDateTime fechaHora, double monto, TipoPago tipoPago) {
        this.id = id;
        this.ticket = ticket;
        this.fechaHora = fechaHora;
        this.monto = monto;
        this.tipoPago = tipoPago;
    }

    public int getId() { return id; }
    public TicketParqueo getTicket() { return ticket; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public double getMonto() { return monto; }
    public TipoPago getTipoPago() { return tipoPago; }
}
