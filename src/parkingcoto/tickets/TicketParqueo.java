package parkingcoto.tickets;

import java.time.Duration;
import java.time.LocalDateTime;
import parkingcoto.espacios.EspacioParqueo;
import parkingcoto.pagos.Pago;
import parkingcoto.tarifas.PoliticaTarifa;
import parkingcoto.vehiculos.Vehiculo;

public class TicketParqueo {

    private final int numero;
    private final Vehiculo vehiculo;
    private final EspacioParqueo espacio;
    private final LocalDateTime fechaHoraEntrada;
    private LocalDateTime fechaHoraSalida;      // null mientras esté ACTIVO
    private EstadoTicket estado;
    private long horasCobradas;
    private long montoFinal;

    public TicketParqueo(int numero, Vehiculo vehiculo, EspacioParqueo espacio,
                         LocalDateTime fechaHoraEntrada) {
        if (vehiculo == null || espacio == null || fechaHoraEntrada == null) {
            throw new IllegalArgumentException("Vehículo, espacio y fecha de entrada son obligatorios");
        }
        this.numero = numero;
        this.vehiculo = vehiculo;
        this.espacio = espacio;
        this.fechaHoraEntrada = fechaHoraEntrada;
        this.estado = EstadoTicket.ACTIVO;
    }

    /**
     * Cierra el ticket: calcula horas y monto con la política y pasa a CERRADO.
     * Si algo falla, el ticket queda exactamente como estaba (se calcula antes de modificar).
     */
    public void cerrar(LocalDateTime salida, PoliticaTarifa politica) {
        if (estado != EstadoTicket.ACTIVO) {
            throw new IllegalStateException(
                    "El ticket " + numero + " no está activo (estado: " + estado + ")");
        }
        if (salida == null || politica == null) {
            throw new IllegalArgumentException("La salida y la política de tarifa son obligatorias");
        }
        if (salida.isBefore(fechaHoraEntrada)) {
            throw new IllegalArgumentException("La salida no puede ser anterior a la entrada");
        }
        long horas = politica.calcularHorasCobradas(fechaHoraEntrada, salida);
        long monto = politica.calcularMonto(vehiculo, fechaHoraEntrada, salida);

        this.fechaHoraSalida = salida;
        this.horasCobradas = horas;
        this.montoFinal = monto;
        this.estado = EstadoTicket.CERRADO;
    }

    //CERRADO -> PAGADO.
    public void marcarPagado() {
        if (estado != EstadoTicket.CERRADO) {
            throw new IllegalStateException(
                    "El ticket " + numero + " no puede pagarse (estado: " + estado + ")");
        }
        this.estado = EstadoTicket.PAGADO;
    }

    public boolean estaActivo() {
        return estado == EstadoTicket.ACTIVO;
    }

    /** Estancia pendiente = el vehículo aún no completa el 
     * proceso (ACTIVO o CERRADO sin pagar). */
    public boolean estaPendiente() {
        return estado == EstadoTicket.ACTIVO || estado == EstadoTicket.CERRADO;
    }

    public int getNumero() { return numero; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public EspacioParqueo getEspacio() { return espacio; }
    public LocalDateTime getFechaHoraEntrada() { return fechaHoraEntrada; }
    public LocalDateTime getFechaHoraSalida() { return fechaHoraSalida; }
    public EstadoTicket getEstado() { return estado; }
    public long getHorasCobradas() { return horasCobradas; }
    public long getMontoFinal() { return montoFinal; }

    @Override
    public String toString() {
        return "Ticket #" + numero + " [" + estado + "] " + vehiculo.getPlaca()
                + " espacio " + espacio.getNumero();
    }
}
