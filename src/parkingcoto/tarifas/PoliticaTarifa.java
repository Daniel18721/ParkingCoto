package parkingcoto.tarifas;

import java.time.LocalDateTime;
import parkingcoto.vehiculos.Vehiculo;

public interface PoliticaTarifa {

    long calcularHorasCobradas(LocalDateTime entrada, LocalDateTime salida);

    long calcularMonto(Vehiculo vehiculo, LocalDateTime entrada, LocalDateTime salida);
}