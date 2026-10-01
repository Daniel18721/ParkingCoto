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

    // ------------------------------------------------------------------
    // Identificadores (acuerdo pendiente del PDF: se generan aquí)
    // ------------------------------------------------------------------

    /** Siguiente número de ticket. Cada llamada consume un número. */
    public int generarNumeroTicket() {
        return ++ultimoNumeroTicket;
    }

    /** Siguiente identificador de pago. Cada llamada consume un número. */
    public int generarIdPago() {
        return ++ultimoIdPago;
    }

    // ------------------------------------------------------------------
    // Registros
    // ------------------------------------------------------------------

    /** Funcionalidad 1. @throws IllegalArgumentException si es nulo o la placa ya existe */
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

    /** Funcionalidad 2. @throws IllegalArgumentException si es nulo o el número ya existe */
    public void registrarEspacio(EspacioParqueo espacio) {
        if (espacio == null) {
            throw new IllegalArgumentException("El espacio es obligatorio");
        }
        if (espacios.containsKey(espacio.getNumero())) {
            throw new IllegalArgumentException("Ya existe el espacio " + espacio.getNumero());
        }
        espacios.put(espacio.getNumero(), espacio);
    }

    /**
     * Conserva un ticket nuevo (ACTIVO). Valida la consistencia del conjunto:
     * vehículo y espacio registrados, número no repetido y vehículo sin estancia pendiente
     * (un vehículo no puede tener dos tickets activos; un CERRADO sin pagar también cuenta).
     *
     * @throws IllegalArgumentException si el ticket es nulo, repetido o referencia objetos no registrados
     * @throws IllegalStateException    si el ticket no está ACTIVO o el vehículo ya tiene una estancia pendiente
     */
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
        tickets.put(ticket.getNumero(), ticket);
    }

    /**
     * Conserva un pago. El ticket debe estar registrado y CERRADO, sin pago previo,
     * y el identificador no puede repetirse. (ServicioPago marca el ticket como PAGADO después.)
     *
     * @throws IllegalArgumentException si es nulo, duplicado o el ticket no está registrado
     * @throws IllegalStateException    si el ticket no está CERRADO
     */
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

    public Optional<EspacioParqueo> buscarEspacio(String numero) {               // antes (int numero)
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
    // Consultas (copias inmodificables)
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
