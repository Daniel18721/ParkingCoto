/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uml_pruebacorta2;

/**
 *
 * @author mcfra
 */
public class CardPayment implements Payment {

    private String marca;
    private String ultimos4Digitos;
    private String autorizacion;
    private boolean aprobado;

    public CardPayment(
        String marca,
        String ultimos4Digitos,
        String autorizacion
    ) {
        this.marca = marca;
        this.ultimos4Digitos = ultimos4Digitos;
        this.autorizacion = autorizacion;
    }

    @Override
    public double calcularAjuste(double subtotal) {
        return subtotal * 0.02;
    }

    @Override
    public double calcularTotal(double subtotal) {
        return subtotal + calcularAjuste(subtotal);
    }

    @Override
    public boolean procesar(double subtotal) {

        aprobado =
            autorizacion != null &&
            !autorizacion.isEmpty();

        return aprobado;
    }

    public boolean isAprobado() {
        return aprobado;
    }
}
