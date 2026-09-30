package parkingcoto.tarifas;


public class TarifaPorHoraConTope implements PoliticaTarifa {

    public static final int HORAS_MINIMAS_PARA_TOPE = 10;
   
    public static final int HORAS_POR_DIA = 24;

    private final double tarifaPorHora;
    private final double tarifaMaximaDiaria;

    public TarifaPorHoraConTope(double tarifaPorHora, double tarifaMaximaDiaria) {
        this.tarifaPorHora = tarifaPorHora;
        this.tarifaMaximaDiaria = tarifaMaximaDiaria;
    }

    @Override
    public double calcularMonto(int horasCobradas) {
        
        throw new UnsupportedOperationException(".");
    }

    @Override
    public double getTarifaPorHora() {
        return tarifaPorHora;
    }

    public double getTarifaMaximaDiaria() {
        return tarifaMaximaDiaria;
    }
}
