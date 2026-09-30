package parkingcoto.tickets;

import java.time.Duration;
import java.time.LocalDateTime;
import parkingcoto.espacios.EspacioParqueo;
import parkingcoto.pagos.Pago;
import parkingcoto.vehiculos.Vehiculo;

public class TicketParqueo {

    private final int numero;
    private final Vehiculo vehiculo;
    private final EspacioParqueo espacio;
    private final LocalDateTime fechaHoraEntrada;
    private LocalDateTime fechaHoraSalida;
    private EstadoTicket estado;
    private int horasCobradas;
    private double montoFinal;
    private Pago pago;

    public TicketParqueo(int numero, Vehiculo vehiculo, EspacioParqueo espacio,
                         LocalDateTime fechaHoraEntrada) {
        this.numero = numero;
        this.vehiculo = vehiculo;
        this.espacio = espacio;
        this.fechaHoraEntrada = fechaHoraEntrada;
        this.estado = EstadoTicket.ACTIVO;
    }


    public int calcularHorasCobradas(LocalDateTime salida) {
        long minutos = Duration.between(fechaHoraEntrada, salida).toMinutes();
        return Math.max(1, (int) Math.ceil(minutos / 60.0));
    }

    public int getNumero() { return numero; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public EspacioParqueo getEspacio() { return espacio; }
    public LocalDateTime getFechaHoraEntrada() { return fechaHoraEntrada; }
    public LocalDateTime getFechaHoraSalida() { return fechaHoraSalida; }
    public EstadoTicket getEstado() { return estado; }
    public int getHorasCobradas() { return horasCobradas; }
    public double getMontoFinal() { return montoFinal; }
    public Pago getPago() { return pago; }
}
