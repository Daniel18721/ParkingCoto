package JUnitTest;
 
import java.time.LocalDateTime;
import java.util.List;
 
import org.junit.Before;
import org.junit.Test;
 
import parkingcoto.asignacion.AsignadorPrimerEspacioCompatible;
import parkingcoto.espacios.EspacioParqueo;
import parkingcoto.espacios.EstadoEspacio;
import parkingcoto.espacios.TipoEspacio;
import parkingcoto.pagos.Pago;
import parkingcoto.pagos.TipoPago;
import parkingcoto.servicio.Parqueo;
import parkingcoto.servicio.ServicioConsulta;
import parkingcoto.servicio.ServicioIngreso;
import parkingcoto.servicio.ServicioPago;
import parkingcoto.servicio.ServicioSalida;
import parkingcoto.tarifas.PoliticaTarifa;
import parkingcoto.tarifas.TarifaPorHoraConTope;
import parkingcoto.tickets.EstadoTicket;
import parkingcoto.tickets.TicketParqueo;
import parkingcoto.vehiculos.Automovil;
import parkingcoto.vehiculos.Motocicleta;
import parkingcoto.vehiculos.Vehiculo;
import parkingcoto.vehiculos.VehiculoCarga;
 
import static org.junit.Assert.*;
 
/**
 * Casos del enunciado: 7, 10, 12, 13 y 15 (más algunas pruebas extra de Parqueo).
 */
public class DanielTest {
 
    private Parqueo parqueo;
    private ServicioIngreso servicioIngreso;
    private ServicioSalida servicioSalida;
    private ServicioPago servicioPago;
    private ServicioConsulta servicioConsulta;
    private PoliticaTarifa politica;
    private LocalDateTime entrada;
 
    // Prepara objetos nuevos antes de cada prueba para evitar dependencias entre ellas.
    @Before
    public void preparar() {
 
        parqueo = new Parqueo("Parking Coto");
 
        parqueo.registrarVehiculo(new Automovil("AUTO1", "Toyota", "Corolla", "Blanco"));
        parqueo.registrarVehiculo(new Motocicleta("MOTO1", "Honda", "CB190", "Rojo"));
        parqueo.registrarVehiculo(new VehiculoCarga("CARGA1", "Isuzu", "NPR", "Blanco"));
 
        parqueo.registrarEspacio(new EspacioParqueo("A1", TipoEspacio.AUTOMOVIL));
        parqueo.registrarEspacio(new EspacioParqueo("A2", TipoEspacio.AUTOMOVIL));
        parqueo.registrarEspacio(new EspacioParqueo("M1", TipoEspacio.MOTOCICLETA));
        parqueo.registrarEspacio(new EspacioParqueo("C1", TipoEspacio.CARGA));
 
        politica = new TarifaPorHoraConTope();
 
        servicioIngreso = new ServicioIngreso(parqueo, new AsignadorPrimerEspacioCompatible());
        servicioSalida = new ServicioSalida(parqueo, politica);
        servicioPago = new ServicioPago(parqueo);
        servicioConsulta = new ServicioConsulta(parqueo);
 
        entrada = LocalDateTime.of(2026, 10, 1, 8, 0);
    }
 
    // Un vehículo con ticket activo no puede ingresar otra vez.
    @Test
    public void noDebeIngresarVehiculoConTicketActivo() {
 
        servicioIngreso.ingresar("AUTO1", entrada);
 
        IllegalStateException error = assertThrows(IllegalStateException.class,
            () -> servicioIngreso.ingresar("AUTO1", entrada.plusMinutes(5))
        );
 
        assertEquals("El vehículo ya tiene una estancia pendiente", error.getMessage());
 
        // No quedó un segundo ticket ni se ocupó otro espacio.
        assertEquals(1, parqueo.consultarTickets().size());
        assertEquals(EstadoEspacio.DISPONIBLE, parqueo.buscarEspacio("A2").get().getEstado());
    }
 
    // 61 minutos se cobran como 2 horas en cada tipo de vehículo.
    @Test
    public void permanenciaDeSesentaYUnMinutos() {
 
        LocalDateTime salida = entrada.plusMinutes(61);
 
        TicketParqueo ticketAuto = servicioIngreso.ingresar("AUTO1", entrada);
        TicketParqueo ticketMoto = servicioIngreso.ingresar("MOTO1", entrada);
        TicketParqueo ticketCarga = servicioIngreso.ingresar("CARGA1", entrada);
 
        servicioSalida.salir("AUTO1", salida);
        servicioSalida.salir("MOTO1", salida);
        servicioSalida.salir("CARGA1", salida);
 
        assertEquals(2L, ticketAuto.getHorasCobradas());
        assertEquals(1800L, ticketAuto.getMontoFinal());
 
        assertEquals(2L, ticketMoto.getHorasCobradas());
        assertEquals(1000L, ticketMoto.getMontoFinal());
 
        assertEquals(2L, ticketCarga.getHorasCobradas());
        assertEquals(3000L, ticketCarga.getMontoFinal());
    }
 
    // El cierre guarda salida, horas y monto; el espacio sigue ocupado hasta pagar.
    @Test
    public void cierreCorrectoDelTicket() {
 
        LocalDateTime salida = entrada.plusHours(2);
 
        TicketParqueo ticket = servicioIngreso.ingresar("AUTO1", entrada);
 
        TicketParqueo cerrado = servicioSalida.salir("AUTO1", salida);
 
        assertSame(ticket, cerrado);
        assertEquals(EstadoTicket.CERRADO, ticket.getEstado());
        assertEquals(salida, ticket.getFechaHoraSalida());
        assertEquals(2L, ticket.getHorasCobradas());
        assertEquals(1800L, ticket.getMontoFinal());
        assertEquals(EstadoEspacio.OCUPADO, ticket.getEspacio().getEstado());
    }
 
    // Un ticket cerrado no puede cerrarse otra vez.
    @Test
    public void noDebeCerrarUnTicketDosVeces() {
 
        TicketParqueo ticket = servicioIngreso.ingresar("AUTO1", entrada);
 
        ticket.cerrar(entrada.plusHours(1), politica);
 
        assertThrows(IllegalStateException.class,
            () -> ticket.cerrar(entrada.plusHours(2), politica)
        );
 
        assertEquals(900L, ticket.getMontoFinal());
    }
 
    // El pago correcto guarda el monto del ticket y lo marca como PAGADO.
    @Test
    public void pagoCorrecto() {
 
        LocalDateTime salida = entrada.plusHours(2);
 
        TicketParqueo ticket = servicioIngreso.ingresar("AUTO1", entrada);
        servicioSalida.salir("AUTO1", salida);
 
        Pago pago = servicioPago.pagar(ticket.getNumero(), TipoPago.TARJETA, salida.plusMinutes(1));
 
        assertEquals(1800L, pago.getMonto());
        assertEquals(TipoPago.TARJETA, pago.getTipoPago());
        assertSame(ticket, pago.getTicket());
        assertEquals(EstadoTicket.PAGADO, ticket.getEstado());
        assertEquals(1, parqueo.consultarPagos().size());
    }
 
    // No se puede pagar un ticket que todavía está activo.
    @Test
    public void noDebePagarTicketActivo() {
 
        TicketParqueo ticket = servicioIngreso.ingresar("AUTO1", entrada);
 
        assertThrows(IllegalStateException.class,
            () -> servicioPago.pagar(ticket.getNumero(), TipoPago.EFECTIVO, entrada.plusHours(1))
        );
 
        assertEquals(EstadoTicket.ACTIVO, ticket.getEstado());
        assertTrue(parqueo.consultarPagos().isEmpty());
    }
 
    // Un ticket ya pagado no admite un segundo pago.
    @Test
    public void noDebePagarDosVecesElMismoTicket() {
 
        LocalDateTime salida = entrada.plusHours(1);
 
        TicketParqueo ticket = servicioIngreso.ingresar("AUTO1", entrada);
        servicioSalida.salir("AUTO1", salida);
        servicioPago.pagar(ticket.getNumero(), TipoPago.EFECTIVO, salida);
 
        assertThrows(IllegalStateException.class,
            () -> servicioPago.pagar(ticket.getNumero(), TipoPago.EFECTIVO, salida.plusMinutes(1))
        );
 
        assertEquals(1, parqueo.consultarPagos().size());
    }
 
    //los ingresos son la suma de los pagos; un ticket cerrado sin pagar no suma.
    @Test
    public void calculoDeIngresosTotales() {
 
        assertEquals(0L, servicioConsulta.calcularIngresosTotales());
 
        TicketParqueo ticketAuto = servicioIngreso.ingresar("AUTO1", entrada);
        TicketParqueo ticketMoto = servicioIngreso.ingresar("MOTO1", entrada);
        servicioIngreso.ingresar("CARGA1", entrada);
 
        servicioSalida.salir("AUTO1", entrada.plusHours(2));   // 1800
        servicioSalida.salir("MOTO1", entrada.plusMinutes(30)); // 500
        servicioSalida.salir("CARGA1", entrada.plusHours(1));   // 1500, queda sin pagar
 
        servicioPago.pagar(ticketAuto.getNumero(), TipoPago.EFECTIVO, entrada.plusHours(3));
        servicioPago.pagar(ticketMoto.getNumero(), TipoPago.SINPE_MOVIL, entrada.plusHours(3));
 
        assertEquals(2300L, servicioConsulta.calcularIngresosTotales());
    }
 
    // Un vehículo con ticket CERRADO sin pagar sigue contando como dentro del parqueo.
    @Test
    public void vehiculoConTicketCerradoSigueDentro() {
 
        TicketParqueo ticket = servicioIngreso.ingresar("AUTO1", entrada);
 
        servicioSalida.salir("AUTO1", entrada.plusHours(1));
 
        List<Vehiculo> dentro = servicioConsulta.consultarVehiculosDentro();
        assertEquals(1, dentro.size());
        assertEquals("AUTO1", dentro.get(0).getPlaca());
        assertTrue(servicioConsulta.consultarTicketsActivos().isEmpty());
 
        servicioPago.pagar(ticket.getNumero(), TipoPago.EFECTIVO, entrada.plusHours(2));
 
        assertTrue(servicioConsulta.consultarVehiculosDentro().isEmpty());
    }
 
    // Parqueo rechaza vehículos y espacios repetidos.
    @Test
    public void noDebeRegistrarVehiculoNiEspacioDuplicado() {
 
        assertThrows(IllegalArgumentException.class,
            () -> parqueo.registrarVehiculo(new Automovil("auto1", "Ford", "Focus", "Azul"))
        );
 
        assertThrows(IllegalArgumentException.class,
            () -> parqueo.registrarEspacio(new EspacioParqueo("A1", TipoEspacio.AUTOMOVIL))
        );
 
        assertEquals(3, parqueo.consultarVehiculos().size());
        assertEquals(4, parqueo.consultarEspacios().size());
    }
}
 