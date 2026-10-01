package parkingcoto.app;

import java.time.LocalDateTime;
import java.util.Scanner;
import parkingcoto.pagos.TipoPago;
import parkingcoto.servicio.ServicioConsulta;
import parkingcoto.servicio.ServicioIngreso;
import parkingcoto.servicio.ServicioPago;
import parkingcoto.servicio.ServicioSalida;
import parkingcoto.tickets.TicketParqueo;
import parkingcoto.espacios.EspacioParqueo;
import parkingcoto.espacios.TipoEspacio;
import parkingcoto.servicio.Parqueo;
import parkingcoto.vehiculos.Automovil;
import parkingcoto.vehiculos.Motocicleta;
import parkingcoto.vehiculos.Vehiculo;
import parkingcoto.vehiculos.VehiculoCarga;

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
    private final Parqueo parqueo;

    // Recibe los servicios y el parqueo que utilizará el menú.
    public MenuConsole(
            Parqueo parqueo,
            ServicioIngreso servicioIngreso,
            ServicioSalida servicioSalida,
            ServicioPago servicioPago,
            ServicioConsulta servicioConsulta) {

        this.parqueo = parqueo;
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
                    case 8 -> registrarVehiculo();
                    case 9 -> registrarEspacio();
                    case 10 -> mostrarOcupacionPorTipo();
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
        System.out.println("8. Registrar vehiculo");
        System.out.println("9. Registrar espacio");
        System.out.println("10. Ver ocupacion por tipo");
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
    
    // Lee los datos, construye el tipo de vehículo elegido y lo registra.
    private void registrarVehiculo() {

        System.out.println("Tipo de vehiculo:");
        System.out.println("1. Automovil");
        System.out.println("2. Motocicleta");
        System.out.println("3. Vehiculo de carga");
        System.out.print("Seleccione: ");

        int tipo = Integer.parseInt(scanner.nextLine());

        if (tipo < 1 || tipo > 3) {
            throw new IllegalArgumentException(
                    "Tipo de vehiculo invalido");
        }

        System.out.print("Placa: ");
        String placa = scanner.nextLine();

        System.out.print("Marca: ");
        String marca = scanner.nextLine();

        System.out.print("Modelo: ");
        String modelo = scanner.nextLine();

        System.out.print("Color: ");
        String color = scanner.nextLine();

        Vehiculo vehiculo = switch (tipo) {
            case 1 -> new Automovil(placa, marca, modelo, color);
            case 2 -> new Motocicleta(placa, marca, modelo, color);
            case 3 -> new VehiculoCarga(placa, marca, modelo, color);
            default -> throw new IllegalArgumentException(
                    "Tipo de vehiculo invalido");
        };

        parqueo.registrarVehiculo(vehiculo);

        System.out.println("Vehiculo registrado correctamente.");
        System.out.println(vehiculo);
    }

    // Lee el identificador y el tipo del espacio y lo registra disponible.
    private void registrarEspacio() {

        System.out.print("Numero o identificador del espacio: ");
        String numero = scanner.nextLine();

        System.out.println("Tipo de espacio:");
        System.out.println("1. Automovil");
        System.out.println("2. Motocicleta");
        System.out.println("3. Carga");
        System.out.print("Seleccione: ");

        int opcion = Integer.parseInt(scanner.nextLine());

        TipoEspacio tipo = switch (opcion) {
            case 1 -> TipoEspacio.AUTOMOVIL;
            case 2 -> TipoEspacio.MOTOCICLETA;
            case 3 -> TipoEspacio.CARGA;
            default -> throw new IllegalArgumentException(
                    "Tipo de espacio invalido");
        };

        EspacioParqueo espacio = new EspacioParqueo(numero, tipo);

        parqueo.registrarEspacio(espacio);

        System.out.println("Espacio registrado correctamente.");
        System.out.println(espacio);
    }

    // Muestra el resumen calculado por el servicio para cada tipo de espacio.
    private void mostrarOcupacionPorTipo() {

        System.out.println("Ocupacion por tipo:");

        for (var resumen : servicioConsulta.consultarOcupacionPorTipo()) {
            System.out.println(
                    resumen.getTipo()
                    + " | Total: " + resumen.getTotal()
                    + " | Ocupados: " + resumen.getOcupados()
                    + " | Disponibles: " + resumen.getDisponibles()
                    + " | Fuera de servicio: " + resumen.getFueraDeServicio()
            );
        }
    }
}