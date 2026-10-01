package parkingcoto.tarifas;

import java.time.LocalDateTime;
import parkingcoto.vehiculos.Vehiculo;

public interface PoliticaTarifa {

    public long calcularHorasCobradas(LocalDateTime fechaHoraEntrada, LocalDateTime salida);

    public long calcularMonto(Vehiculo vehiculo, LocalDateTime fechaHoraEntrada, LocalDateTime salida);

}