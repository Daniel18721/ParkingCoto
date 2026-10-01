package parkingcoto.servicio;

import java.util.ArrayList;
import java.util.List;
import parkingcoto.espacios.EspacioParqueo;
import parkingcoto.espacios.EstadoEspacio;
import parkingcoto.espacios.TipoEspacio;
import parkingcoto.pagos.Pago;
import parkingcoto.tickets.TicketParqueo;
import parkingcoto.vehiculos.Vehiculo;


 //Consultas de solo lectura sobre el parqueo
public class ServicioConsulta {

    private final Parqueo parqueo;

    public ServicioConsulta(Parqueo parqueo) {
        this.parqueo = parqueo;
    }

    //Espacios en estado DISPONIBLE.
    public List<EspacioParqueo> consultarEspaciosDisponibles() {
        List<EspacioParqueo> resultado = new ArrayList<>();
        for (EspacioParqueo e : parqueo.consultarEspacios()) {
            if (e.getEstado() == EstadoEspacio.DISPONIBLE) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    //Filtrando por tipo de espacio.
    public List<EspacioParqueo> consultarEspaciosDisponibles(TipoEspacio tipo) {
        List<EspacioParqueo> resultado = new ArrayList<>();
        for (EspacioParqueo e : consultarEspaciosDisponibles()) {
            if (e.getTipo() == tipo) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    /**
     * Vehículos dentro del parqueo, un vehículo sigue
     * "dentro" mientras su estancia esté pendiente: ticket ACTIVO, o CERRADO sin pagar.
     */
    public List<Vehiculo> consultarVehiculosDentro() {
        List<Vehiculo> dentro = new ArrayList<>();
        for (TicketParqueo t : parqueo.consultarTickets()) {
            if (t.estaPendiente()) {
                dentro.add(t.getVehiculo());
            }
        }
        return dentro;
    }

    //tickets en estado ACTIVO.
    public List<TicketParqueo> consultarTicketsActivos() {
        List<TicketParqueo> activos = new ArrayList<>();
        for (TicketParqueo t : parqueo.consultarTickets()) {
            if (t.estaActivo()) {
                activos.add(t);
            }
        }
        return activos;
    }

    //Un resumen por cada tipo de espacio.
    public List<ResumenOcupacion> consultarOcupacionPorTipo() {
        List<ResumenOcupacion> resumen = new ArrayList<>();
        for (TipoEspacio tipo : TipoEspacio.values()) {
            int total = 0;
            int ocupados = 0;
            int disponibles = 0;
            int fuera = 0;
            for (EspacioParqueo e : parqueo.consultarEspacios()) {
                if (e.getTipo() != tipo) {
                    continue;
                }
                total++;
                if (e.getEstado() == EstadoEspacio.OCUPADO) {
                    ocupados++;
                } else if (e.getEstado() == EstadoEspacio.DISPONIBLE) {
                    disponibles++;
                } else {
                    fuera++;
                }
            }
            resumen.add(new ResumenOcupacion(tipo, total, ocupados, disponibles, fuera));
        }
        return resumen;
    }

    //Suma de los montos de los pagos registrados.
    public long calcularIngresosTotales() {
        long total = 0;
        for (Pago p : parqueo.consultarPagos()) {
            total += p.getMonto();
        }
        return total;
    }
}

