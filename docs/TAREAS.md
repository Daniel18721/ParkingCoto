# Reparto de trabajo, trazabilidad y pendientes

Los nombres de clases, atributos y **firmas de métodos ya están definidos**. Si cambian una firma,
avísense entre ustedes: es el "contrato" que permite trabajar en paralelo.
Cada `TODO` lanza `UnsupportedOperationException`; al implementarlo, quiten esa línea.

## 1. Qué ya está y qué falta

**Ya está:** enums, jerarquía `Vehiculo` con sus tarifas del enunciado, `PoliticaTarifa`,
constructores y getters, colecciones de `Parqueo`, y `TicketParqueo.calcularHorasCobradas`
(reutilizado de la prueba corta 2).

**Falta (todo lo marcado `TODO`):** lógica de `TarifaPorHoraConTope`, `Vehiculo.calcularMonto`,
`EspacioParqueo`, `TicketParqueo` (cerrar, marcarPagado, estaActivo), `Parqueo` (las 15 funcionalidades),
`Main`.

**Paquetes que NO se crearon a propósito (decidirlos en equipo):**
- **Manejo de errores / excepciones:** cómo reportar las reglas violadas (excepciones propias, booleanos, mensajes…).
- **Pruebas:** dónde y cómo implementar las 15 pruebas obligatorias y la tabla de pruebas.

## 2. Reparto sugerido

| | **Gaudy** | **Kenneth** |
|---|---|---|
| Clases | `TarifaPorHoraConTope`, `Vehiculo.calcularMonto`, `TicketParqueo.cerrar()`, `Main` | `EspacioParqueo`, `TicketParqueo.marcarPagado()` y `estaActivo()` |
| `Parqueo` | `registrarSalida`, `registrarPago`, `calcularIngresosTotales`, `consultarTicketsActivos` | `registrarVehiculo`, `registrarEspacio`, `consultarEspaciosDisponibles` (x2), `registrarIngreso`, `registrarIngresoEnEspacio`, `consultarVehiculosDentro`, `consultarOcupacionPorTipo`, los 4 `buscar*` |
| Pruebas (cuando se definan) | Casos 8–15 (tarifas, cierre, pago, liberación, ingresos) | Casos 1–7 (ingresos y reglas de espacios/tickets) |
| Informe | Descripción, requerimientos, herencia y polimorfismo (incluye tarifa máxima) | UML, reglas de negocio, pruebas |
| Juntos | Portada, justificación de decisiones de diseño, revisión cruzada | |
| Individual | Conclusión propia (mín. 200 palabras) | Conclusión propia (mín. 200 palabras) |

## 3. Orden sugerido (por dependencias)

1. **En paralelo:** `TarifaPorHoraConTope` + `Vehiculo.calcularMonto` (Gaudy) y `EspacioParqueo` (Kenneth).
2. **Decidir juntos** cómo se manejan los errores de reglas (antes de tocar `Parqueo`).
3. `TicketParqueo.cerrar` (Gaudy) y `marcarPagado` (Kenneth).
4. Métodos de `Parqueo` de cada uno. Empiecen por `registrarVehiculo` y `registrarEspacio`.
5. Pruebas, UML final en EasyUML (base en `docs/UML.md`), informe y README final.

## 4. Trazabilidad: funcionalidades obligatorias (sección 7)

| # | Funcionalidad | Dónde está |
|---|---|---|
| 1 | Registrar vehículos | `Parqueo.registrarVehiculo` |
| 2 | Registrar espacios | `Parqueo.registrarEspacio` |
| 3 | Consultar espacios disponibles | `Parqueo.consultarEspaciosDisponibles` |
| 4 | Registrar ingreso | `Parqueo.registrarIngreso` / `registrarIngresoEnEspacio` |
| 5 | Asignar espacio compatible automáticamente | `Parqueo.registrarIngreso` + `EspacioParqueo.esCompatibleCon` |
| 6 | Generar ticket | `new TicketParqueo(...)` dentro de `registrarIngreso` |
| 7 | Vehículos dentro | `Parqueo.consultarVehiculosDentro` |
| 8 | Registrar salida | `Parqueo.registrarSalida` |
| 9 | Calcular horas cobradas | `TicketParqueo.calcularHorasCobradas` |
| 10 | Calcular monto | `Vehiculo.calcularMonto` -> `PoliticaTarifa` |
| 11 | Registrar pago | `Parqueo.registrarPago` + `TicketParqueo.marcarPagado` |
| 12 | Liberar espacio | `TicketParqueo.cerrar` -> `EspacioParqueo.liberar` |
| 13 | Ingresos totales | `Parqueo.calcularIngresosTotales` |
| 14 | Tickets activos | `Parqueo.consultarTicketsActivos` |
| 15 | Ocupación por tipo | `Parqueo.consultarOcupacionPorTipo` |

## 5. Trazabilidad: reglas de negocio (sección 8 y sección 4)

| Regla | Quién debería aplicarla |
|---|---|
| Espacio ocupado / fuera de servicio no se asigna | `EspacioParqueo.ocupar` |
| Espacio compatible con el tipo del vehículo | `EspacioParqueo.esCompatibleCon` |
| Un vehículo no puede tener dos tickets activos | `Parqueo.registrarIngreso*` |
| No hay salida sin ticket activo | `Parqueo.registrarSalida` |
| No se paga un ticket activo | `TicketParqueo.marcarPagado` |
| Monto según el tipo real, sin if/switch externo | `Vehiculo.calcularMonto` (polimorfismo + `PoliticaTarifa`) |
| El espacio se libera al completar la salida | `TicketParqueo.cerrar` |
| Tarifa máxima diaria, evolutiva | `TarifaPorHoraConTope` (o una nueva `PoliticaTarifa`) |

## 6. Decisiones respecto a la prueba corta 2

| Elemento | Decisión |
|---|---|
| `Car`, `Ford`, `BMW`, `CarType`, `Vehicle`, velocidades | Reemplazados por `Vehiculo` + `Automovil`/`Motocicleta`/`VehiculoCarga` |
| `TarifaParqueo` (switch por `CarType`) | Eliminada: viola la restricción 4. Cada vehículo trae su `PoliticaTarifa` |
| `Payment`, `CashPayment`, `CardPayment`, `SinpePayment` (comisión 2 % / descuento 1 %) | Eliminados: el nuevo enunciado no pide ajustes. `Pago` + `TipoPago` |
| `ParkingTicket.calcularHorasCobradas` (ceil) | **Conservado** en `TicketParqueo` (con mínimo de 1 hora) |
| `LocalDateTime` como parámetro de entrada/salida | **Conservado** |
| `boolean cerrado` | Reemplazado por `EstadoTicket` |

El código original queda en `docs/referencia_prueba_corta2/` (no se compila).

## 7. Dudas para el profesor

1. **Tope diario:** el enunciado lo aplica desde 10 h, pero un automóvil paga ₡8 100 por 9 h y ₡7 000 por 10 h.
   ¿Se aplica desde 10 h exactas, o siempre que el monto supere el tope?
2. **Estadías de más de 24 h:** ¿tope por cada bloque de 24 h? (Sugerencia: sí.)
3. **0 minutos de permanencia:** `calcularHorasCobradas` cobra mínimo 1 hora. ¿Correcto?
4. **Lista de entregables:** las capturas cortan la sección 13 en el punto 6. Verifiquen si hay más ítems.

## 8. Checklist de entregables (sección 13)

- [ ] Código fuente Java completo y ejecutable
- [ ] Diagrama UML final
- [ ] README con instrucciones de ejecución (ya tiene la base)
- [ ] Informe PDF de 6 a 10 páginas, sin contar portada (estructura de la sección 14)
- [ ] Tabla de pruebas: entrada, esperado, obtenido
- [ ] Conclusión individual de cada integrante, mínimo 200 palabras

## 9. Restricciones del proyecto: autoverificación al terminar

- [ ] Ninguna clase concentra toda la lógica (`Parqueo` solo coordina)
- [ ] Ningún `if`/`switch` pregunta por el tipo concreto del vehículo para calcular tarifas
- [ ] Todos los atributos son `private`
- [ ] Se usan colecciones (`Map`, `List` en `Parqueo`)
- [ ] Herencia y polimorfismo: `Vehiculo` y `PoliticaTarifa`
- [ ] Pruebas suficientes para demostrar el cumplimiento de las reglas
