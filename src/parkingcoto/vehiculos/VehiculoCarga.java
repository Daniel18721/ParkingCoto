package parkingcoto.vehiculos;

import parkingcoto.espacios.TipoEspacio;
import parkingcoto.tarifas.TarifaPorHoraConTope;

public class VehiculoCarga extends Vehiculo {

    public static final double TARIFA_POR_HORA = 1500;
    public static final double TARIFA_MAXIMA_DIARIA = 11000;

    public VehiculoCarga(String placa, String marca, String modelo, String color) {
        super(placa, marca, modelo, color, TipoVehiculo.VEHICULO_CARGA,
              new TarifaPorHoraConTope(TARIFA_POR_HORA, TARIFA_MAXIMA_DIARIA));
    }

    @Override
    public TipoEspacio getTipoEspacioRequerido() {
        return TipoEspacio.CARGA;
    }
}
