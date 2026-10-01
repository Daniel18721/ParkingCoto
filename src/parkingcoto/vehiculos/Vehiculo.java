package parkingcoto.vehiculos;

import java.util.Locale;
import parkingcoto.tarifas.ConfiguracionTarifa;

public abstract class Vehiculo {
    private final String placa;
    private final String marca;
    private final String modelo;
    private final String color;

    protected Vehiculo(String placa, String marca, String modelo, String color) {
        this.placa = validarTexto(placa, "La placa").toUpperCase(Locale.ROOT);
        this.marca = validarTexto(marca, "La marca");
        this.modelo = validarTexto(modelo, "El modelo");
        this.color = validarTexto(color, "El color");
    }

    private String validarTexto(String valor, String nombreCampo) {
        if (valor == null || valor.isBlank()) 
            throw new IllegalArgumentException(nombreCampo + " es obligatorio.");
        
        return valor.strip();
    }

    public final String getPlaca() {
        return placa;
    }

    public final String getMarca() {
        return marca;
    }

    public final String getModelo() {
        return modelo;
    }

    public final String getColor() {
        return color;
    }

    public abstract TipoVehiculo getTipoVehiculo();

    public abstract ConfiguracionTarifa getConfiguracionTarifa();

    @Override
    public String toString() {
        return getTipoVehiculo() + " | Placa: " + placa + " | Marca: " + marca + " | Modelo: " + modelo + " | Color: " + color;
    }
}