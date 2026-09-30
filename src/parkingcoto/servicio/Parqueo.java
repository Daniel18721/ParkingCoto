package parkingcoto.servicio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import parkingcoto.espacios.EspacioParqueo;
import parkingcoto.espacios.TipoEspacio;
import parkingcoto.pagos.Pago;
import parkingcoto.pagos.TipoPago;
import parkingcoto.tickets.TicketParqueo;
import parkingcoto.vehiculos.Vehiculo;


public class Parqueo {

    private final String nombre;
    private final Map<String, Vehiculo> vehiculos = new LinkedHashMap<>(); 
    private final List<EspacioParqueo> espacios = new ArrayList<>();
    private final List<TicketParqueo> tickets = new ArrayList<>();
    private final List<Pago> pagos = new ArrayList<>();
    private int siguienteNumeroTicket = 1;
    private int siguienteIdPago = 1;

    public Parqueo(String nombre) {
        this.nombre = nombre;
    }

    // Getters de solo lectura
    public String getNombre() { return nombre; }
    public List<Vehiculo> getVehiculos() { return Collections.unmodifiableList(new ArrayList<>(vehiculos.values())); }
    public List<EspacioParqueo> getEspacios() { return Collections.unmodifiableList(espacios); }
    public List<TicketParqueo> getTickets() { return Collections.unmodifiableList(tickets); }
    public List<Pago> getPagos() { return Collections.unmodifiableList(pagos); }
}
