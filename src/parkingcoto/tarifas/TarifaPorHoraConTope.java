package parkingcoto.tarifas;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import parkingcoto.vehiculos.Vehiculo;

public final class TarifaPorHoraConTope implements PoliticaTarifa {

    @Override
    public long calcularHorasCobradas(LocalDateTime entrada, LocalDateTime salida) {
        Duration permanencia = obtenerPermanencia(entrada, salida);

        return redondearHoras(permanencia);
    }

    @Override
    public long calcularMonto(Vehiculo vehiculo, LocalDateTime entrada, LocalDateTime salida) {

        Objects.requireNonNull(vehiculo, "El vehículo es obligatorio.");

        Duration permanencia = obtenerPermanencia(entrada, salida);

        ConfiguracionTarifa configuracion = 
                Objects.requireNonNull( vehiculo.getConfiguracionTarifa(), 
                        "El vehículo debe proporcionar una tarifa.");

        Duration periodo = configuracion.getDuracionPeriodoDiario();

        long periodosCompletos = permanencia.dividedBy(periodo);

        Duration tiempoRestante = permanencia.minus(periodo.multipliedBy(periodosCompletos));

        long montoPeriodoCompleto = calcularMontoPeriodo(periodo, configuracion);

        long montoPeriodosCompletos = Math.multiplyExact(periodosCompletos, montoPeriodoCompleto);

        long montoRestante = calcularMontoPeriodo(tiempoRestante, configuracion);

        return Math.addExact(montoPeriodosCompletos, montoRestante);
    }

    private Duration obtenerPermanencia(LocalDateTime entrada, LocalDateTime salida) {

        Objects.requireNonNull(entrada, "La fecha de entrada es obligatoria.");

        Objects.requireNonNull(salida, "La fecha de salida es obligatoria.");

        Duration permanencia = Duration.between(entrada, salida);

        if (permanencia.isNegative()) 
            throw new IllegalArgumentException("La salida no puede ser anterior a la entrada.");
        
        return permanencia;
    }

    private long redondearHoras(Duration permanencia) {
        long segundos = permanencia.getSeconds();

        long horasCompletas = segundos / 3600;

        boolean tieneFraccion = segundos % 3600 != 0 || permanencia.getNano() != 0;

        return tieneFraccion ? horasCompletas + 1 : horasCompletas;
    }

    private long calcularMontoPeriodo( Duration permanencia, ConfiguracionTarifa configuracion) {

        long horas = redondearHoras(permanencia);

        long montoPorHoras = Math.multiplyExact( horas, configuracion.getTarifaPorHora());

        boolean aplicaMaximo = permanencia.compareTo(configuracion.getUmbralTarifaMaxima()) >= 0;

        if (aplicaMaximo) 
            return Math.min(montoPorHoras, configuracion.getTarifaMaximaDiaria());

        return montoPorHoras;
    }
}