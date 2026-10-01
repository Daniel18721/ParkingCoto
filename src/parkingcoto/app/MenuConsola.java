package parkingcoto.app;

import parkingcoto.servicio.ServicioConsulta;
import parkingcoto.servicio.ServicioIngreso;
import parkingcoto.servicio.ServicioPago;
import parkingcoto.servicio.ServicioSalida;

/**
 * [PERSONA 3] Muestra opciones, lee entradas e informa resultados y errores.
 * Invoca servicios; no contiene las reglas del parqueo.
 */
public class MenuConsola {

    private final ServicioIngreso ingreso;
    private final ServicioSalida salida;
    private final ServicioPago pago;
    private final ServicioConsulta consulta;

    public MenuConsola(ServicioIngreso ingreso, ServicioSalida salida,
                       ServicioPago pago, ServicioConsulta consulta) {
        this.ingreso = ingreso;
        this.salida = salida;
        this.pago = pago;
        this.consulta = consulta;
    }

    public void iniciar() {
        // TODO (P3)
        throw new UnsupportedOperationException("TODO");
    }
}
