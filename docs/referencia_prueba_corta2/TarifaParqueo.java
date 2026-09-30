/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uml_pruebacorta2;

/**
 *
 * @author mcfra
 */
public class TarifaParqueo {

    public double obtenerTarifa(CarType tipo) {

        switch (tipo) {
            case COMPACT:
                return 700;

            case STANDARD:
                return 800;

            case LUXURIOUS:
                return 1200;

            case SPORT:
                return 1500;

            case SUV:
                return 1000;

            default:
                return 0;
        }
    }
}
