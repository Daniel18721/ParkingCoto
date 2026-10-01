package parkingcoto.app;

import java.time.LocalDateTime;
import java.util.Scanner;
import parkingcoto.pagos.TipoPago;
import parkingcoto.servicio.ServicioConsulta;
import parkingcoto.servicio.ServicioIngreso;
import parkingcoto.servicio.ServicioPago;
import parkingcoto.servicio.ServicioSalida;
import parkingcoto.tickets.TicketParqueo;

/**
 * [PERSONA 3]
 * Menu de consola para interactuar con los servicios del parqueo.
 */
public class MenuConsole {

    private final ServicioIngreso servicioIngreso;
    private final ServicioSalida servicioSalida;
    private final ServicioPago servicioPago;
    private final ServicioConsulta servicioConsulta;
    private final Scanner scanner;

    public MenuConsole(
            ServicioIngreso servicioIngreso,
            ServicioSalida servicioSalida,
            ServicioPago servicioPago,
            ServicioConsulta servicioConsulta) {

        this.servicioIngreso = servicioIngreso;
        this.servicioSalida = servicioSalida;
        this.servicioPago = servicioPago;
        this.servicioConsulta = servicioConsulta;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {

        int opcion;

        do {
            mostrarMenu();

            try {
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1 -> registrarIngreso();
                    case 2 -> registrarSalida();
                    case 3 -> registrarPago();
                    case 4 -> mostrarEspaciosDisponibles();
                    case 5 -> mostrarVehiculosDentro();
                    case 6 -> mostrarTicketsActivos();
                    case 7 -> mostrarIngresosTotales();
                    case 0 -> System.out.println("Saliendo del sistema...");
                    default -> System.out.println("Opcion invalida.");
                }

            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                opcion = -1;
            }

            System.out.println();

        } while (opcion != 0);
    }

    private void mostrarMenu() {

        System.out.println("================================");
        System.out.println("        PARKING COTO");
        System.out.println("================================");
        System.out.println("1. Registrar ingreso");
        System.out.println("2. Registrar salida");
        System.out.println("3. Registrar pago");
        System.out.println("4. Ver espacios disponibles");
        System.out.println("5. Ver vehiculos dentro");
        System.out.println("6. Ver tickets activos");
        System.out.println("7. Ver ingresos totales");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    private void registrarIngreso() {

        System.out.print("Placa del vehiculo: ");
        String placa = scanner.nextLine();

        TicketParqueo ticket = servicioIngreso.ingresar(
                placa,
                LocalDateTime.now()
        );

        System.out.println("Ingreso registrado correctamente.");
        System.out.println(ticket);
    }

    private void registrarSalida() {

        System.out.print("Placa del vehiculo: ");
        String placa = scanner.nextLine();

        TicketParqueo ticket = servicioSalida.salir(
                placa,
                LocalDateTime.now()
        );

        System.out.println("Salida registrada correctamente.");
        System.out.println("Ticket #" + ticket.getNumero());
        System.out.println("Horas cobradas: " + ticket.getHorasCobradas());
        System.out.println("Monto a pagar: C" + ticket.getMontoFinal());
    }

    private void registrarPago() {

        System.out.print("Numero de ticket: ");
        int numeroTicket = Integer.parseInt(scanner.nextLine());

        System.out.println("Tipo de pago:");
        System.out.println("1. Efectivo");
        System.out.println("2. Tarjeta");
        System.out.println("3. SINPE Movil");
        System.out.print("Seleccione: ");

        int opcionPago = Integer.parseInt(scanner.nextLine());

        TipoPago tipoPago = switch (opcionPago) {
            case 1 -> TipoPago.EFECTIVO;
            case 2 -> TipoPago.TARJETA;
            case 3 -> TipoPago.SINPE_MOVIL;
            default -> throw new IllegalArgumentException(
                    "Tipo de pago invalido");
        };

        var pago = servicioPago.pagar(
                numeroTicket,
                tipoPago,
                LocalDateTime.now()
        );

        System.out.println("Pago registrado correctamente.");
        System.out.println(pago);
    }

    private void mostrarEspaciosDisponibles() {

        System.out.println("Espacios disponibles:");

        var espacios = servicioConsulta.consultarEspaciosDisponibles();

        if (espacios.isEmpty()) {
            System.out.println("No hay espacios disponibles.");
            return;
        }

        for (var espacio : espacios) {
            System.out.println(espacio);
        }
    }

    private void mostrarVehiculosDentro() {

        System.out.println("Vehiculos dentro:");

        var vehiculos = servicioConsulta.consultarVehiculosDentro();

        if (vehiculos.isEmpty()) {
            System.out.println("No hay vehiculos dentro.");
            return;
        }

        for (var vehiculo : vehiculos) {
            System.out.println(vehiculo);
        }
    }

    private void mostrarTicketsActivos() {

        System.out.println("Tickets activos:");

        var tickets = servicioConsulta.consultarTicketsActivos();

        if (tickets.isEmpty()) {
            System.out.println("No hay tickets activos.");
            return;
        }

        for (TicketParqueo ticket : tickets) {
            System.out.println(ticket);
        }
    }

    private void mostrarIngresosTotales() {

        long total = servicioConsulta.calcularIngresosTotales();

        System.out.println("Ingresos totales: C" + total);
    }
}