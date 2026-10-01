package parkingcoto.asignacion;

import java.util.List;
import java.util.Optional;
import parkingcoto.espacios.EspacioParqueo;
import parkingcoto.vehiculos.Vehiculo;

/** [PERSONA 3] Selecciona el primer espacio que pueda recibir al vehículo (puedeAsignarseA). */
public class AsignadorPrimerEspacioCompatible implements AsignadorEspacio {

    @Override
    public Optional<EspacioParqueo> seleccionarEspacio(Vehiculo vehiculo, List<EspacioParqueo> espacios) {
        // TODO (P3)
        throw new UnsupportedOperationException("TODO");
    }
}
