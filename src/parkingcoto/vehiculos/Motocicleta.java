package parkingcoto.vehiculos;

import parkingcoto.espacios.TipoEspacio;
import parkingcoto.tarifas.TarifaPorHoraConTope;

public class Motocicleta extends Vehiculo {

    public static final double TARIFA_POR_HORA = 500;
    public static final double TARIFA_MAXIMA_DIARIA = 4000;

    public Motocicleta(String placa, String marca, String modelo, String color) {
        super(placa, marca, modelo, color, TipoVehiculo.MOTOCICLETA,
              new TarifaPorHoraConTope(TARIFA_POR_HORA, TARIFA_MAXIMA_DIARIA));
    }

    @Override
    public TipoEspacio getTipoEspacioRequerido() {
        return TipoEspacio.MOTOCICLETA;
    }
}
