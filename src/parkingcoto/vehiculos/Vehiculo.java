package parkingcoto.vehiculos;

import parkingcoto.espacios.TipoEspacio;
import parkingcoto.tarifas.PoliticaTarifa;

public abstract class Vehiculo {

    private final String placa;
    private final String marca;
    private final String modelo;
    private final String color;
    private final TipoVehiculo tipoVehiculo;
    private final PoliticaTarifa politicaTarifa;

    protected Vehiculo(String placa, String marca, String modelo, String color,
                       TipoVehiculo tipoVehiculo, PoliticaTarifa politicaTarifa) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.tipoVehiculo = tipoVehiculo;
        this.politicaTarifa = politicaTarifa;
    }

    public abstract TipoEspacio getTipoEspacioRequerido();

    public double calcularMonto(int horasCobradas) {
        throw new UnsupportedOperationException("TODO");
    }

    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getColor() { return color; }
    public TipoVehiculo getTipoVehiculo() { return tipoVehiculo; }
    protected PoliticaTarifa getPoliticaTarifa() { return politicaTarifa; }
}
