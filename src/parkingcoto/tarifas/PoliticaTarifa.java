package parkingcoto.tarifas;

import java.time.LocalDateTime;
import parkingcoto.vehiculos.Vehiculo;

public interface PoliticaTarifa {

    // Calcula las horas cobradas entre la entrada y la salida,
    // contando cada fracción de hora como una hora completa.
    public long calcularHorasCobradas(LocalDateTime fechaHoraEntrada, LocalDateTime salida);

    // Calcula el monto según la tarifa del vehículo y las reglas de cobro.
    public long calcularMonto(Vehiculo vehiculo, LocalDateTime fechaHoraEntrada, LocalDateTime salida);
}