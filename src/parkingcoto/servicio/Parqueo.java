package parkingcoto.servicio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import parkingcoto.espacios.EspacioParqueo;
import parkingcoto.espacios.TipoEspacio;
import parkingcoto.pagos.Pago;
import parkingcoto.pagos.TipoPago;
import parkingcoto.tickets.EstadoTicket;
import parkingcoto.tickets.TicketParqueo;
import parkingcoto.vehiculos.Vehiculo;
import parkingcoto.espacios.EstadoEspacio;
import parkingcoto.vehiculos.TipoVehiculo;


public class Parqueo {

    private final String nombre;
    private final Map<String, Vehiculo> vehiculos = new LinkedHashMap<>();          // clave: placa
    private final Map<String, EspacioParqueo> espacios = new LinkedHashMap<>();   // antes Map<Integer,...>
    private final Map<Integer, TicketParqueo> tickets = new LinkedHashMap<>();      // clave: número
    private final List<Pago> pagos = new ArrayList<>();
    private int ultimoNumeroTicket = 0;
    private int ultimoIdPago = 0;

    public Parqueo(String nombre) {
        this.nombre = nombre;
    }

    /** Siguiente número de ticket
     *cada llamada consume un número. */
    public int generarNumeroTicket() {
        return ++ultimoNumeroTicket;
    }

    /** Siguiente identificador de pago
     * cada llamada consume un número. */
    public int generarIdPago() {
        return ++ultimoIdPago;
    }

    // Si es nulo o la placa ya existe
    public void registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo es obligatorio");
        }
        String placa = normalizar(vehiculo.getPlaca());
        if (vehiculos.containsKey(placa)) {
            throw new IllegalArgumentException("Ya existe un vehículo con placa " + placa);
        }
        vehiculos.put(placa, vehiculo);
    }

    /** Si es nulo o el número ya existe */
    public void registrarEspacio(EspacioParqueo espacio) {
        if (espacio == null) {
            throw new IllegalArgumentException("El espacio es obligatorio");
        }
        if (espacios.containsKey(espacio.getNumero())) {
            throw new IllegalArgumentException("Ya existe el espacio " + espacio.getNumero());
        }
        espacios.put(espacio.getNumero(), espacio);
    }

    // Guarda un ticket ACTIVO si es válido, no está repetido
    //y el vehículo no tiene otra estancia pendiente
    public void registrarTicket(TicketParqueo ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("El ticket es obligatorio");
        }
        if (ticket.getEstado() != EstadoTicket.ACTIVO) {
            throw new IllegalStateException("Solo se puede registrar un ticket ACTIVO");
        }
        if (tickets.containsKey(ticket.getNumero())) {
            throw new IllegalArgumentException("Ya existe el ticket " + ticket.getNumero());
        }
        if (!buscarVehiculo(ticket.getVehiculo().getPlaca()).isPresent()) {
            throw new IllegalArgumentException("El vehículo del ticket no está registrado");
        }
        if (!buscarEspacio(ticket.getEspacio().getNumero()).isPresent()) {
            throw new IllegalArgumentException("El espacio del ticket no está registrado");
        }
        if (tieneEstanciaPendiente(ticket.getVehiculo().getPlaca())) {
            throw new IllegalStateException(
                    "El vehículo " + ticket.getVehiculo().getPlaca() + " ya tiene una estancia pendiente");
        }
        if (buscarVehiculo(ticket.getVehiculo().getPlaca()).orElse(null) != ticket.getVehiculo()) {
        throw new IllegalArgumentException(
            "El vehículo del ticket no está registrado o no es el mismo objeto registrado");
        }
        if (buscarEspacio(ticket.getEspacio().getNumero()).orElse(null) != ticket.getEspacio()) {
        throw new IllegalArgumentException(
            "El espacio del ticket no está registrado o no es el mismo objeto registrado");
        }
        // Impide que dos estancias pendientes compartan el mismo espacio.
        for (TicketParqueo existente : tickets.values()) {
            if (existente.estaPendiente()
                    && existente.getEspacio() == ticket.getEspacio()) {

                throw new IllegalStateException(
                        "El espacio ya tiene una estancia pendiente");
            }
        }

        // ServicioIngreso debe ocupar el espacio antes de registrar el ticket.
        if (ticket.getEspacio().getEstado() != EstadoEspacio.OCUPADO) {
            throw new IllegalStateException(
                    "El espacio debe estar ocupado antes de registrar el ticket");
        }

        // Verifica compatibilidad incluso si alguien registra el ticket directamente.
        TipoVehiculo tipoRequerido = switch (ticket.getEspacio().getTipo()) {
            case AUTOMOVIL -> TipoVehiculo.AUTOMOVIL;
            case MOTOCICLETA -> TipoVehiculo.MOTOCICLETA;
            case CARGA -> TipoVehiculo.VEHICULO_CARGA;
        };

        if (ticket.getVehiculo().getTipoVehiculo() != tipoRequerido) {
            throw new IllegalArgumentException(
                    "El espacio no es compatible con el vehículo");
        }
        tickets.put(ticket.getNumero(), ticket);
    }
    
    // Guarda un pago si el ticket está CERRADO,
    //no tiene pago previo y el monto coincide con el del ticket.

    public void registrarPago(Pago pago) {
        if (pago == null) {
            throw new IllegalArgumentException("El pago es obligatorio");
        }
        
        TicketParqueo ticket = pago.getTicket();
           if (pago.getMonto() != ticket.getMontoFinal()){
       throw new IllegalArgumentException("El monto del pago no coincide con el monto final del ticket");
           }
        if (tickets.get(ticket.getNumero()) != ticket) {
            throw new IllegalArgumentException("El ticket del pago no está registrado");
        }
        if (ticket.getEstado() != EstadoTicket.CERRADO) {
            throw new IllegalStateException(
                    "Solo se puede pagar un ticket CERRADO (estado: " + ticket.getEstado() + ")");
        }
        // Impide registrar un pago con fecha anterior al cierre del ticket.
        if (pago.getFechaHora().isBefore(ticket.getFechaHoraSalida())) {
            throw new IllegalArgumentException(
                    "El pago no puede ser anterior al cierre del ticket");
        }
         for (Pago existente : pagos) {
            if (existente.getId() == pago.getId()) {
                throw new IllegalArgumentException("Ya existe el pago " + pago.getId());
            }
            if (existente.getTicket() == ticket) {
                throw new IllegalArgumentException("El ticket " + ticket.getNumero() + " ya tiene un pago");
            }
        }
        pagos.add(pago);
    }

    // ------------------------------------------------------------------
    // Búsquedas
    // ------------------------------------------------------------------
    
    public Optional<Vehiculo> buscarVehiculo(String placa) {
        return Optional.ofNullable(vehiculos.get(normalizar(placa)));
    }

    public Optional<EspacioParqueo> buscarEspacio(String numero) {
    return Optional.ofNullable(espacios.get(numero == null ? null : numero.strip()));
    }

    public Optional<TicketParqueo> buscarTicket(int numero) {
        return Optional.ofNullable(tickets.get(numero));
    }

    /** Ticket en estado ACTIVO de esa placa, si existe. */
    public Optional<TicketParqueo> buscarTicketActivo(String placa) {
        String p = normalizar(placa);
        for (TicketParqueo t : tickets.values()) {
            if (t.estaActivo() && normalizar(t.getVehiculo().getPlaca()).equals(p)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    /** Estancia pendiente = ticket ACTIVO o CERRADO (sin pagar) de esa placa. */
    public boolean tieneEstanciaPendiente(String placa) {
        String p = normalizar(placa);
        for (TicketParqueo t : tickets.values()) {
            if (t.estaPendiente() && normalizar(t.getVehiculo().getPlaca()).equals(p)) {
                return true;
            }
        }
        return false;
    }

    // ------------------------------------------------------------------
    // Consultas
    // ------------------------------------------------------------------

    public List<Vehiculo> consultarVehiculos() {
        return Collections.unmodifiableList(new ArrayList<>(vehiculos.values()));
    }

    public List<EspacioParqueo> consultarEspacios() {
        return Collections.unmodifiableList(new ArrayList<>(espacios.values()));
    }

    public List<TicketParqueo> consultarTickets() {
        return Collections.unmodifiableList(new ArrayList<>(tickets.values()));
    }

    public List<Pago> consultarPagos() {
        return Collections.unmodifiableList(new ArrayList<>(pagos));
    }

    public String getNombre() { return nombre; }

    private static String normalizar(String placa) {
        return placa == null ? "" : placa.trim().toUpperCase();
    }
}
