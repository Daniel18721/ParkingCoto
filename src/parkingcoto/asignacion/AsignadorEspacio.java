package parkingcoto.asignacion;

import java.util.List;
import java.util.Optional;
import parkingcoto.espacios.EspacioParqueo;
import parkingcoto.vehiculos.Vehiculo;

/**
 * [PERSONA 3] Define la selección de un espacio disponible y compatible. Separa el criterio
 * de selección del proceso de ingreso. (Firma propuesta; P3 puede ajustarla avisando al equipo.)
 */
public interface AsignadorEspacio {

    /** @return un espacio que pueda recibir al vehículo, o vacío si no hay disponibilidad */
    Optional<EspacioParqueo> seleccionarEspacio(Vehiculo vehiculo, List<EspacioParqueo> espacios);
}
