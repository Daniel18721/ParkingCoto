package parkingcoto.app;

import parkingcoto.asignacion.AsignadorEspacio;
import parkingcoto.asignacion.AsignadorPrimerEspacioCompatible;
import parkingcoto.espacios.EspacioParqueo;
import parkingcoto.espacios.TipoEspacio;
import parkingcoto.servicio.Parqueo;
import parkingcoto.servicio.ServicioConsulta;
import parkingcoto.servicio.ServicioIngreso;
import parkingcoto.servicio.ServicioPago;
import parkingcoto.servicio.ServicioSalida;
import parkingcoto.tarifas.PoliticaTarifa;
import parkingcoto.tarifas.TarifaPorHoraConTope;
import parkingcoto.vehiculos.Automovil;
import parkingcoto.vehiculos.Motocicleta;
import parkingcoto.vehiculos.VehiculoCarga;

/**
 * [PERSONA 3]
 * Punto de entrada de la aplicación.
 */
public class Main {

    public static void main(String[] args) {

        Parqueo parqueo = new Parqueo("Parking Coto");

        cargarDatosIniciales(parqueo);

        PoliticaTarifa politicaTarifa =
                new TarifaPorHoraConTope();

        AsignadorEspacio asignador =
                new AsignadorPrimerEspacioCompatible();

        ServicioIngreso servicioIngreso =
                new ServicioIngreso(parqueo, asignador);

        ServicioSalida servicioSalida =
                new ServicioSalida(parqueo, politicaTarifa);

        ServicioPago servicioPago =
                new ServicioPago(parqueo);

        ServicioConsulta servicioConsulta =
                new ServicioConsulta(parqueo);

        MenuConsole menu = new MenuConsole(
                servicioIngreso,
                servicioSalida,
                servicioPago,
                servicioConsulta
        );

        menu.iniciar();
    }

    private static void cargarDatosIniciales(Parqueo parqueo) {

        parqueo.registrarVehiculo(
                new Automovil(
                        "ABC123",
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
    }
}