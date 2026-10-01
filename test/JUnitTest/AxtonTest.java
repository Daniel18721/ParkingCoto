package JUnitTest;

import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import parkingcoto.espacios.EspacioParqueo;
import parkingcoto.espacios.EstadoEspacio;
import parkingcoto.espacios.TipoEspacio;
import parkingcoto.tarifas.PoliticaTarifa;
import parkingcoto.tarifas.TarifaPorHoraConTope;
import parkingcoto.vehiculos.Automovil;
import parkingcoto.vehiculos.Motocicleta;
import parkingcoto.vehiculos.Vehiculo;
import parkingcoto.vehiculos.VehiculoCarga;

import static org.junit.Assert.*;

public class AxtonTest {

    private Vehiculo automovil;
    private PoliticaTarifa politica;
    private LocalDateTime entrada;

    // Prepara objetos nuevos antes de cada prueba para evitar dependencias entre ellas.
    @Before
    public void preparar() {
        automovil = new Automovil("AUTO1", "Toyota", "Corolla", "Blanco");

        politica = new TarifaPorHoraConTope();

        entrada = LocalDateTime.of(2026, 10, 1, 8, 0);
    }

    // Caso 4: rechaza una segunda ocupación y conserva el estado del espacio.
    @Test
    public void noDebeOcuparEspacioOcupado() {

        EspacioParqueo espacio = new EspacioParqueo("A1", TipoEspacio.AUTOMOVIL);

        Vehiculo segundoAutomovil = new Automovil("AUTO2", "Ford", "Focus", "Azul");

        espacio.ocupar(automovil);

        IllegalStateException error = assertThrows(IllegalStateException.class,
            () -> espacio.ocupar(segundoAutomovil)
        );

        assertEquals("El espacio ya está ocupado.", error.getMessage());

        assertEquals(EstadoEspacio.OCUPADO, espacio.getEstado());

        assertFalse(espacio.puedeAsignarseA(segundoAutomovil));
    }

    // Caso 5: rechaza la ocupación de un espacio fuera de servicio.
    @Test
    public void noDebeOcuparEspacioFueraDeServicio() {

        EspacioParqueo espacio = new EspacioParqueo("A1", TipoEspacio.AUTOMOVIL);

        espacio.ponerFueraDeServicio();

        IllegalStateException error = assertThrows(IllegalStateException.class,
            () -> espacio.ocupar(automovil)
        );

        assertEquals("El espacio está fuera de servicio.", error.getMessage());

        assertEquals(EstadoEspacio.FUERA_DE_SERVICIO, espacio.getEstado());

        assertFalse(espacio.puedeAsignarseA(automovil));
    }

    // Caso 8: una permanencia de un minuto debe cobrar una hora.
    @Test
    public void permanenciaDeUnMinuto() {

        LocalDateTime salida = entrada.plusMinutes(1);

        assertEquals(1L, politica.calcularHorasCobradas(entrada, salida));
        assertEquals(900L, politica.calcularMonto(automovil, entrada, salida));
    }

    // Caso 9: sesenta minutos exactos deben cobrar una sola hora.
    @Test
    public void permanenciaDeSesentaMinutos() {

        LocalDateTime salida = entrada.plusMinutes(60);

        assertEquals(1L, politica.calcularHorasCobradas(entrada, salida));

        assertEquals(900L, politica.calcularMonto(automovil, entrada, salida));
    }

    // Caso 11: once horas deben aplicar el máximo diario de cada vehículo.
    @Test
    public void permanenciaMayorDeDiezHorasAplicaMaximo() {

        LocalDateTime salida = entrada.plusHours(11);

        Vehiculo motocicleta = new Motocicleta("MOTO1", "Honda", "CB190", "Rojo");

        Vehiculo carga = new VehiculoCarga("CARGA1", "Isuzu", "NPR", "Blanco");

        assertEquals(11L, politica.calcularHorasCobradas(entrada, salida));

        assertEquals(7000L, politica.calcularMonto(automovil, entrada, salida));

        assertEquals(4000L, politica.calcularMonto(motocicleta, entrada, salida));

        assertEquals(11000L, politica.calcularMonto(carga, entrada, salida));
    }
}