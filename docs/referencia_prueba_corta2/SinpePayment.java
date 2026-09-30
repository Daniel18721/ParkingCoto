/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uml_pruebacorta2;

/**
 *
 * @author mcfra
 */
public class SinpePayment implements Payment {

    private String telefonoOrigen;
    private String numeroReferencia;
    private boolean aprobado;

    public SinpePayment(
        String telefonoOrigen,
        String numeroReferencia
    ) {
        this.telefonoOrigen = telefonoOrigen;
        this.numeroReferencia = numeroReferencia;
    }

    public boolean validarReferencia() {
        return numeroReferencia != null
            && !numeroReferencia.isEmpty();
    }

    @Override
    public double calcularAjuste(double subtotal) {
        return subtotal * 0.01;
    }

    @Override
    public double calcularTotal(double subtotal) {
        return subtotal - calcularAjuste(subtotal);
    }

    @Override
    public boolean procesar(double subtotal) {

        aprobado = validarReferencia();

        return aprobado;
    }

    public boolean isAprobado() {
        return aprobado;
    }
}
