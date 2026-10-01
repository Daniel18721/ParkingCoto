package parkingcoto.tarifas;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import parkingcoto.vehiculos.Vehiculo;

public final class TarifaPorHoraConTope implements PoliticaTarifa {

    // Obtiene la permanencia y devuelve las horas cobradas,
    // redondeando cualquier fracción hacia arriba.
    @Override
    public long calcularHorasCobradas(LocalDateTime entrada, LocalDateTime salida) {
        Duration permanencia = obtenerPermanencia(entrada, salida);

        return redondearHoras(permanencia);
    }

    // Obtiene la configuración del vehículo, divide la estancia en períodos
    // diarios y suma sus cobros con el correspondiente al tiempo restante.
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

    // Valida las fechas y devuelve el tiempo transcurrido entre ellas,
    // rechazando una salida anterior a la entrada.
    private Duration obtenerPermanencia(LocalDateTime entrada, LocalDateTime salida) {

        Objects.requireNonNull(entrada, "La fecha de entrada es obligatoria.");

        Objects.requireNonNull(salida, "La fecha de salida es obligatoria.");

        Duration permanencia = Duration.between(entrada, salida);

        if (permanencia.isNegative()) 
            throw new IllegalArgumentException("La salida no puede ser anterior a la entrada.");
        
        return permanencia;
    }

    // Convierte la duración en horas enteras, agregando una hora
    // si existe alguna fracción, incluso de segundos o nanosegundos.
    private long redondearHoras(Duration permanencia) {
        long segundos = permanencia.getSeconds();

        long horasCompletas = segundos / 3600;

        boolean tieneFraccion = segundos % 3600 != 0 || permanencia.getNano() != 0;

        return tieneFraccion ? horasCompletas + 1 : horasCompletas;
    }

    // Calcula el cobro de un período y lo limita al máximo diario
    // cuando su duración alcanza o supera el umbral configurado.
    private long calcularMontoPeriodo( Duration permanencia, ConfiguracionTarifa configuracion) {

        long horas = redondearHoras(permanencia);

        long montoPorHoras = Math.multiplyExact( horas, configuracion.getTarifaPorHora());

        boolean aplicaMaximo = permanencia.compareTo(configuracion.getUmbralTarifaMaxima()) >= 0;

        if (aplicaMaximo) 
            return Math.min(montoPorHoras, configuracion.getTarifaMaximaDiaria());

        return montoPorHoras;
    }
}