package parkingcoto.vehiculos;

import java.time.Duration;
import parkingcoto.tarifas.ConfiguracionTarifa;

public final class Automovil extends Vehiculo {
    
    /*Define la configuración de la tarifa establecida
    en el enunciado para este vehículo en específico
    */
    private static final ConfiguracionTarifa TARIFA = new ConfiguracionTarifa(
            900,
            7000,
            Duration.ofHours(24),
            Duration.ofHours(10));

    public Automovil(
            String placa,
            String marca,
            String modelo,
            String color) {

        super(placa, marca, modelo, color);
    }

    @Override
    public TipoVehiculo getTipoVehiculo() {
        return TipoVehiculo.AUTOMOVIL;
    }

    @Override
    public ConfiguracionTarifa getConfiguracionTarifa() {
        return TARIFA;
    }
}