package parkingcoto.tarifas;

import java.time.Duration;
import java.util.Objects;

/* Esta clase recibe, valida y agrupa los parámetros de la tarifa,
 como el precio por hora, el máximo diario y los períodos de tiempo,
 para que la política de cobro pueda utilizarlos al calcular el monto.
 */
public final class ConfiguracionTarifa {

    private final long tarifaPorHora;
    private final long tarifaMaximaDiaria;
    private final Duration duracionPeriodoDiario;
    private final Duration umbralTarifaMaxima;

    public ConfiguracionTarifa(long tarifaPorHora, long tarifaMaximaDiaria,
            Duration duracionPeriodoDiario, Duration umbralTarifaMaxima) {

        if (tarifaPorHora <= 0)
            throw new IllegalArgumentException("La tarifa por hora debe ser mayor que cero.");

        if (tarifaMaximaDiaria <= 0) 
            throw new IllegalArgumentException("La tarifa máxima diaria debe ser mayor que cero.");
        
        Objects.requireNonNull(duracionPeriodoDiario, "La duración del período diario es obligatoria.");

        Objects.requireNonNull(umbralTarifaMaxima, "El umbral de tarifa máxima es obligatorio.");

        if (duracionPeriodoDiario.isZero() || duracionPeriodoDiario.isNegative()) 
            throw new IllegalArgumentException("El período diario debe tener una duración positiva.");
        
        if (umbralTarifaMaxima.isZero() || umbralTarifaMaxima.isNegative()) 
            throw new IllegalArgumentException("El umbral debe tener una duración positiva.");
        
        if (umbralTarifaMaxima.compareTo(duracionPeriodoDiario) > 0) 
            throw new IllegalArgumentException("El umbral no puede superar el período diario.");
        
        this.tarifaPorHora = tarifaPorHora;
        this.tarifaMaximaDiaria = tarifaMaximaDiaria;
        this.duracionPeriodoDiario = duracionPeriodoDiario;
        this.umbralTarifaMaxima = umbralTarifaMaxima;
    }

    public long getTarifaPorHora() {
        return tarifaPorHora;
    }

    public long getTarifaMaximaDiaria() {
        return tarifaMaximaDiaria;
    }

    public Duration getDuracionPeriodoDiario() {
        return duracionPeriodoDiario;
    }

    public Duration getUmbralTarifaMaxima() {
        return umbralTarifaMaxima;
    }
}
