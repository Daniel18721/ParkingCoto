/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uml_pruebacorta2;

/**
 *
 * @author mcfra
 */
import java.time.Duration;
import java.time.LocalDateTime;

public class ParkingTicket {

    private Car car;
    private LocalDateTime fechaHoraEntrada;
    private LocalDateTime fechaHoraSalida;
    private TarifaParqueo tarifaParqueo;

    private int horasCobradas;
    private double tarifaAplicada;
    private double subtotal;
    private double ajustePago;
    private double totalCancelado;

    private Payment pago;
    private boolean cerrado;

    public ParkingTicket(Car car, LocalDateTime fechaHoraEntrada) {
        this.car = car;
        this.fechaHoraEntrada = fechaHoraEntrada;
        this.cerrado = false;
    }

    public int calcularHorasCobradas() {

        long minutos = Duration.between(
            fechaHoraEntrada,
            fechaHoraSalida
        ).toMinutes();

        horasCobradas =
            (int) Math.ceil(minutos / 60.0);

        return horasCobradas;
    }

    public double calcularSubtotal() {
        tarifaAplicada = tarifaParqueo.obtenerTarifa(car.getType());

        subtotal = calcularHorasCobradas() * tarifaAplicada;

        return subtotal;
    }

    public boolean cerrarTicket(
        LocalDateTime fechaHoraSalida,
        Payment pago
    ) {

        this.fechaHoraSalida = fechaHoraSalida;

        calcularSubtotal();

        if (pago.procesar(subtotal)) {

            this.pago = pago;

            ajustePago =
                pago.calcularAjuste(subtotal);

            totalCancelado =
                pago.calcularTotal(subtotal);

            cerrado = true;

            return true;
        }

        return false;
    }

    public double getTotalCancelado() {
        return totalCancelado;
    }
}
