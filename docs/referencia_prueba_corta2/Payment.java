/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uml_pruebacorta2;

/**
 *
 * @author mcfra
 */
public interface Payment {

    double calcularAjuste(double subtotal);

    double calcularTotal(double subtotal);

    boolean procesar(double subtotal);
}