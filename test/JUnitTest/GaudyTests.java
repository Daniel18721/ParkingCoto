package JUnitTest;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import parkingcoto.asignacion.AsignadorEspacio;
import parkingcoto.asignacion.AsignadorPrimerEspacioCompatible;
import parkingcoto.espacios.EspacioParqueo;
import parkingcoto.espacios.EstadoEspacio;
import parkingcoto.espacios.TipoEspacio;
import parkingcoto.pagos.TipoPago;
import parkingcoto.servicio.Parqueo;
import parkingcoto.servicio.ServicioIngreso;
import parkingcoto.servicio.ServicioPago;
import parkingcoto.servicio.ServicioSalida;
import parkingcoto.tarifas.PoliticaTarifa;
import parkingcoto.tarifas.TarifaPorHoraConTope;
import parkingcoto.tickets.EstadoTicket;
import parkingcoto.tickets.TicketParqueo;
import parkingcoto.vehiculos.Automovil;
import parkingcoto.vehiculos.Motocicleta;
import parkingcoto.vehiculos.VehiculoCarga;

import static org.junit.Assert.*;

public class GaudyTests {

    private Parqueo parqueo;
    private ServicioIngreso servicioIngreso;
    private ServicioSalida servicioSalida;
    private ServicioPago servicioPago;

    @Before
    public void preparar() {

        parqueo = new Parqueo("Parking Coto");

        parqueo.registrarVehiculo(
                new Automovil(
                        "AUTO1",
                        "Toyota",
                        "Corolla",
                        "Blanco"
                )
        );

        parqueo.registrarVehiculo(
                new Motocicleta(
                        "MOTO1",
                        "Honda",
                        "CB190",
                        "Rojo"
                )
        );

        parqueo.registrarVehiculo(
                new VehiculoCarga(
                        "CARGA1",
                        "Isuzu",
                        "NPR",
                        "Blanco"
                )
        );

        parqueo.registrarEspacio(
                new EspacioParqueo(
                        "A1",
                        TipoEspacio.AUTOMOVIL
                )
        );

        parqueo.registrarEspacio(
                new EspacioParqueo(
                        "M1",
                        TipoEspacio.MOTOCICLETA
                )
        );

        parqueo.registrarEspacio(
                new EspacioParqueo(
                        "C1",
                        TipoEspacio.CARGA
                )
        );

        AsignadorEspacio asignador =
                new AsignadorPrimerEspacioCompatible();

        PoliticaTarifa politica =
                new TarifaPorHoraConTope();

        servicioIngreso =
                new ServicioIngreso(parqueo, asignador);

        servicioSalida =
                new ServicioSalida(parqueo, politica);

        servicioPago =
                new ServicioPago(parqueo);
    }

    @Test
    public void ingresoCorrectoAutomovil() {

        TicketParqueo ticket = servicioIngreso.ingresar(
                "AUTO1",
                LocalDateTime.of(2026, 10, 1, 8, 0)
        );

        assertNotNull(ticket);
        assertEquals(EstadoTicket.ACTIVO, ticket.getEstado());
        assertEquals("A1", ticket.getEspacio().getNumero());
        assertEquals(
                EstadoEspacio.OCUPADO,
                ticket.getEspacio().getEstado()
        );
    }

    @Test
    public void ingresoCorrectoMotocicleta() {

        TicketParqueo ticket = servicioIngreso.ingresar(
                "MOTO1",
                LocalDateTime.of(2026, 10, 1, 8, 0)
        );

        assertNotNull(ticket);
        assertEquals("M1", ticket.getEspacio().getNumero());
        assertEquals(
                EstadoEspacio.OCUPADO,
                ticket.getEspacio().getEstado()
        );
    }

    @Test
    public void ingresoCorrectoVehiculoCarga() {

        TicketParqueo ticket = servicioIngreso.ingresar(
                "CARGA1",
                LocalDateTime.of(2026, 10, 1, 8, 0)
        );

        assertNotNull(ticket);
        assertEquals("C1", ticket.getEspacio().getNumero());
        assertEquals(
                EstadoEspacio.OCUPADO,
                ticket.getEspacio().getEstado()
        );
    }

    @Test
    public void noDebeAsignarEspacioIncompatible() {

        Parqueo parqueoPrueba =
                new Parqueo("Prueba");

        parqueoPrueba.registrarVehiculo(
                new Automovil(
                        "AUTO2",
                        "Toyota",
                        "Yaris",
                        "Gris"
                )
        );

        parqueoPrueba.registrarEspacio(
                new EspacioParqueo(
                        "M2",
                        TipoEspacio.MOTOCICLETA
                )
        );

        ServicioIngreso ingreso =
                new ServicioIngreso(
                        parqueoPrueba,
                        new AsignadorPrimerEspacioCompatible()
                );

        try {

            ingreso.ingresar(
                    "AUTO2",
                    LocalDateTime.of(2026, 10, 1, 8, 0)
            );

            fail("Se esperaba un error por espacio incompatible");

        } catch (IllegalStateException ex) {

            assertNotNull(ex.getMessage());
        }
    }

    @Test
    public void liberarEspacioDespuesDelPago() {

        LocalDateTime entrada =
                LocalDateTime.of(2026, 10, 1, 8, 0);

        LocalDateTime salida =
                LocalDateTime.of(2026, 10, 1, 10, 0);

        TicketParqueo ticket =
                servicioIngreso.ingresar("AUTO1", entrada);

        servicioSalida.salir("AUTO1", salida);

        servicioPago.pagar(
                ticket.getNumero(),
                TipoPago.EFECTIVO,
                salida.plusMinutes(5)
        );

        assertEquals(
                EstadoTicket.PAGADO,
                ticket.getEstado()
        );

        assertEquals(
                EstadoEspacio.DISPONIBLE,
                ticket.getEspacio().getEstado()
        );
    }
}