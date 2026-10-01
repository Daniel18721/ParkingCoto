# ParkingCoto

Aplicación de consola en Java para controlar un parqueo privado con cobro por hora.
Maneja vehículos, espacios, ingresos, salidas, pagos y consultas, aplicando
programación orientada a objetos (herencia, polimorfismo, encapsulamiento,
interfaces, enumeraciones y colecciones).


## Requisitos

- Java 21 (JDK)
- Apache NetBeans (el proyecto es de tipo Java with Ant)
- Para las pruebas: las librerías JUnit 4.13.2 y Hamcrest 1.3

## Cómo ejecutar el programa

1. Clic derecho sobre el proyecto → Run.
2. La clase principal es `parkingcoto.app.Main`.

Al iniciar se cargan datos de ejemplo para poder probar sin registrar nada antes:

| Tipo | Identificador |
|---|---|
| Automóvil | placa `ABC123` |
| Motocicleta | placa `MOTO1` |
| Vehículo de carga | placa `CARGA1` |
| Espacios | `A1` (automóvil), `M1` (motocicleta), `C1` (carga) |

## Menú

1. Registrar ingreso
2. Registrar salida
3. Registrar pago
4. Ver espacios disponibles
5. Ver vehículos dentro
6. Ver tickets activos
7. Ver ingresos totales
8. Registrar vehículo
9. Registrar espacio
10. Ver ocupación por tipo
0. Salir

Flujo típico: ingreso (1) → salida (2), que calcula el monto y cierra el ticket →
pago (3), que registra el cobro y libera el espacio.

El menú usa la fecha y hora reales del sistema, por lo que los cobros de varias
horas se verifican con las pruebas automáticas y no desde la consola.

## Tarifas

| Vehículo | Tarifa por hora | Máximo por período diario |
|---|---|---|
| Motocicleta | ₡500 | ₡4 000 |
| Automóvil | ₡900 | ₡7 000 |
| Vehículo de carga | ₡1 500 | ₡11 000 |

- Toda fracción de hora se cobra como hora completa.
- El máximo se aplica por cada período de 24 horas cuando la permanencia es de
  10 horas o más.

## Cómo ejecutar las pruebas

Las pruebas usan JUnit 4 y están en la carpeta `test/JUnitTest`.

1. Agregar las librerías al proyecto, si NetBeans muestra el aviso de que no las
   encuentra: Tools → Libraries → New Library, con los nombres exactos `junit_4`
   (JAR `junit-4.13.2.jar`) y `hamcrest` (JAR `hamcrest-core-1.3.jar`).
2. Clic derecho sobre el proyecto → Test.
3. Revisar la ventana Test Results: deben aparecer todas las pruebas en verde.

Archivos de pruebas:

- `AxtonTest`: espacios ocupados y fuera de servicio, y tarifas (casos 4, 5, 8, 9 y 11).
- `GaudyTests`: ingresos, espacio incompatible y liberación (casos 1, 2, 3, 6 y 14).
- `DanielTest`: tickets, pagos e ingresos (casos 7, 10, 12, 13 y 15).

## Estructura del proyecto

| Paquete | Contenido |
|---|---|
| `parkingcoto.app` | `Main` y el menú de consola |
| `parkingcoto.vehiculos` | `Vehiculo` (abstracta), `Automovil`, `Motocicleta`, `VehiculoCarga`, `TipoVehiculo` |
| `parkingcoto.espacios` | `EspacioParqueo`, `TipoEspacio`, `EstadoEspacio` |
| `parkingcoto.tarifas` | `PoliticaTarifa`, `TarifaPorHoraConTope`, `ConfiguracionTarifa` |
| `parkingcoto.tickets` | `TicketParqueo`, `EstadoTicket` |
| `parkingcoto.pagos` | `Pago`, `TipoPago` |
| `parkingcoto.servicio` | `Parqueo`, `ServicioIngreso`, `ServicioSalida`, `ServicioPago`, `ServicioConsulta`, `ResumenOcupacion` |
| `parkingcoto.asignacion` | `AsignadorEspacio`, `AsignadorPrimerEspacioCompatible` |

## Notas

- El espacio de un vehículo se libera hasta que el ticket se paga, no al cerrarse.
