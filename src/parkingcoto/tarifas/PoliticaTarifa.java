package parkingcoto.tarifas;


public interface PoliticaTarifa {

  
    double calcularMonto(int horasCobradas);

    double getTarifaPorHora();
}
