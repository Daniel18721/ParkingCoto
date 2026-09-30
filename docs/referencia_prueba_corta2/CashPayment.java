/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uml_pruebacorta2;

/**
 *
 * @author mcfra
 */
public class CashPayment implements Payment {

    private double montoRecibido;
    private double vuelto;

    public CashPayment(double montoRecibido) {
        this.montoRecibido = montoRecibido;
    }

    @Override
    public double calcularAjuste(double subtotal) {
        return 0;
    }

    @Override
    public double calcularTotal(double subtotal) {
        return subtotal;
    }

    @Override
    public boolean procesar(double subtotal) {

        if (montoRecibido >= subtotal) {
            vuelto = montoRecibido - subtotal;
            return true;
        }

        return false;
    }

    public double getVuelto() {
        return vuelto;
    }
}
