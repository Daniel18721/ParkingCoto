package parkingcoto.asignacion;

import java.util.List;
import java.util.Optional;
import parkingcoto.espacios.EspacioParqueo;
import parkingcoto.vehiculos.Vehiculo;

/** [PERSONA 3] Selecciona el primer espacio que pueda recibir al vehículo. */
public class AsignadorPrimerEspacioCompatible implements AsignadorEspacio {

    @Override
    public Optional<EspacioParqueo> seleccionarEspacio(
            Vehiculo vehiculo,
            List<EspacioParqueo> espacios) {

        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo es obligatorio");
        }

        if (espacios == null) {
            throw new IllegalArgumentException("La lista de espacios es obligatoria");
        }

        for (EspacioParqueo espacio : espacios) {
            if (espacio != null && espacio.puedeAsignarseA(vehiculo)) {
                return Optional.of(espacio);
            }
        }

        return Optional.empty();
    }
}