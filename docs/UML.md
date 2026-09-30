# Borrador del diagrama UML (para pasar a EasyUML)

Este diagrama es solo una referencia en texto. El entregable final debe hacerse en EasyUML
(o la herramienta que indique el profesor) mostrando: clases, atributos, métodos principales,
herencia, interfaces, asociaciones, composición/agregación, multiplicidades y enumeraciones.

Para verlo renderizado: pegar el bloque en https://mermaid.live

```mermaid
classDiagram
    direction TB

    class Vehiculo {
        <<abstract>>
        -String placa
        -String marca
        -String modelo
        -String color
        -TipoVehiculo tipoVehiculo
        -PoliticaTarifa politicaTarifa
        +getTipoEspacioRequerido() TipoEspacio*
        +calcularMonto(int horasCobradas) double
    }
    class Automovil
    class Motocicleta
    class VehiculoCarga
    Vehiculo <|-- Automovil
    Vehiculo <|-- Motocicleta
    Vehiculo <|-- VehiculoCarga

    class PoliticaTarifa {
        <<interface>>
        +calcularMonto(int horasCobradas) double
        +getTarifaPorHora() double
    }
    class TarifaPorHoraConTope {
        -double tarifaPorHora
        -double tarifaMaximaDiaria
        +calcularMonto(int horasCobradas) double
    }
    PoliticaTarifa <|.. TarifaPorHoraConTope
    Vehiculo "1" *-- "1" PoliticaTarifa : usa

    class EspacioParqueo {
        -int numero
        -TipoEspacio tipo
        -EstadoEspacio estado
        +esCompatibleCon(Vehiculo) boolean
        +estaDisponible() boolean
        +ocupar(Vehiculo) void
        +liberar() void
        +ponerFueraDeServicio() void
        +habilitar() void
    }

    class TicketParqueo {
        -int numero
        -LocalDateTime fechaHoraEntrada
        -LocalDateTime fechaHoraSalida
        -EstadoTicket estado
        -int horasCobradas
        -double montoFinal
        +calcularHorasCobradas(LocalDateTime) int
        +cerrar(LocalDateTime) void
        +marcarPagado(Pago) void
        +estaActivo() boolean
    }

    class Pago {
        -int id
        -LocalDateTime fechaHora
        -double monto
        -TipoPago tipoPago
    }

    class Parqueo {
        -String nombre
        +registrarVehiculo(Vehiculo)
        +registrarEspacio(EspacioParqueo)
        +registrarIngreso(String, LocalDateTime) TicketParqueo
        +registrarIngresoEnEspacio(String, int, LocalDateTime) TicketParqueo
        +registrarSalida(String, LocalDateTime) TicketParqueo
        +registrarPago(int, TipoPago, LocalDateTime) Pago
        +calcularIngresosTotales() double
        +consultarEspaciosDisponibles() List
        +consultarVehiculosDentro() List
        +consultarTicketsActivos() List
        +consultarOcupacionPorTipo() List
    }
    class ResumenOcupacion

    class TipoVehiculo { <<enumeration>> MOTOCICLETA AUTOMOVIL VEHICULO_CARGA }
    class TipoEspacio { <<enumeration>> MOTOCICLETA AUTOMOVIL CARGA }
    class EstadoEspacio { <<enumeration>> DISPONIBLE OCUPADO FUERA_DE_SERVICIO }
    class EstadoTicket { <<enumeration>> ACTIVO CERRADO PAGADO }
    class TipoPago { <<enumeration>> EFECTIVO TARJETA SINPE_MOVIL }

    Vehiculo --> TipoVehiculo
    Vehiculo ..> TipoEspacio : requiere
    EspacioParqueo --> TipoEspacio
    EspacioParqueo --> EstadoEspacio
    TicketParqueo --> EstadoTicket
    Pago --> TipoPago

    TicketParqueo "0..*" --> "1" Vehiculo
    TicketParqueo "0..1" --> "1" EspacioParqueo
    TicketParqueo "1" --> "0..1" Pago
    Pago "1" --> "1" TicketParqueo

    Parqueo "1" o-- "0..*" Vehiculo
    Parqueo "1" *-- "0..*" EspacioParqueo
    Parqueo "1" *-- "0..*" TicketParqueo
    Parqueo "1" *-- "0..*" Pago
    Parqueo ..> ResumenOcupacion
```
