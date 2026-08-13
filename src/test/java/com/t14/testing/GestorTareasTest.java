package com.t14.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.t14.tasks.GestorTareas;
import com.t14.tasks.Tarea;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GestorTareas — Suite Completa de Pruebas Unitarias")
class GestorTareasTest {

    private GestorTareas gestor;

    @BeforeEach
    void setUp() {
        gestor = new GestorTareas();
    }

    @Test
    @DisplayName("1a. Registrar tarea valida retorna la tarea con estado PENDIENTE")
    void registrarTarea_conDatosValidos_retornaTareaEnEstadoPendiente() {
        String titulo      = "Implementar login";
        String descripcion = "Modulo de autenticacion de usuarios";
        int prioridad      = 1;

        Tarea tarea = gestor.registrarTarea(titulo, descripcion, prioridad);

        assertNotNull(tarea,                               "La tarea no debe ser nula");
        assertEquals(titulo,      tarea.getTitulo(),       "El titulo debe coincidir");
        assertEquals(descripcion, tarea.getDescripcion(),  "La descripcion debe coincidir");
        assertEquals(1,           tarea.getPrioridad(),    "La prioridad debe ser 1");
        assertEquals(Tarea.Estado.PENDIENTE, tarea.getEstado(), "El estado inicial debe ser PENDIENTE");
        assertTrue(tarea.getId().startsWith("T-"),         "El ID debe comenzar con 'T-'");
    }

    @Test
    @DisplayName("1b. Cambiar estado de PENDIENTE a EN_PROGRESO es una transicion valida")
    void cambiarEstado_dePendienteAEnProgreso_actualizaEstadoCorrectamente() {
        Tarea tarea = gestor.registrarTarea("Disenhar base de datos", "Modelo ER inicial", 2);

        Tarea actualizada = gestor.cambiarEstado(tarea.getId(), Tarea.Estado.EN_PROGRESO);

        assertEquals(Tarea.Estado.EN_PROGRESO, actualizada.getEstado(),
                "El estado debe ser EN_PROGRESO tras la transicion");
        assertEquals(tarea.getId(), actualizada.getId(),
                "La tarea retornada debe ser la misma (mismo ID)");
    }

    @Test
    @DisplayName("1c. Flujo completo: PENDIENTE -> EN_PROGRESO -> COMPLETADA")
    void flujoCompleto_pendienteACompletada_todosLosEstadosSonValidos() {
        Tarea tarea = gestor.registrarTarea("Crear informe", "Informe mensual de ventas", 3);

        gestor.cambiarEstado(tarea.getId(), Tarea.Estado.EN_PROGRESO);
        Tarea completada = gestor.cambiarEstado(tarea.getId(), Tarea.Estado.COMPLETADA);

        assertEquals(Tarea.Estado.COMPLETADA, completada.getEstado(),
                "El estado final debe ser COMPLETADA");
    }

    @Test
    @DisplayName("1d. Obtener tareas pendientes retorna solo las que estan en PENDIENTE")
    void obtenerPendientes_variasTareas_retornaSoloPendientes() {
        Tarea t1 = gestor.registrarTarea("Tarea A", "Descripcion A", 1);
        Tarea t2 = gestor.registrarTarea("Tarea B", "Descripcion B", 2);
        Tarea t3 = gestor.registrarTarea("Tarea C", "Descripcion C", 3);
        gestor.cambiarEstado(t1.getId(), Tarea.Estado.EN_PROGRESO);

        List<Tarea> pendientes = gestor.obtenerPendientes();

        assertEquals(2, pendientes.size(),
                "Solo deben aparecer 2 tareas pendientes (B y C)");
        assertTrue(pendientes.stream().allMatch(t -> t.getEstado() == Tarea.Estado.PENDIENTE),
                "Todas las tareas retornadas deben estar en estado PENDIENTE");
    }

    @Test
    @DisplayName("2a. Cancelar una tarea en estado PENDIENTE es una transicion valida")
    void cambiarEstado_cancelarTareaPendiente_estadoCambiaCorrecto() {
        Tarea tarea = gestor.registrarTarea("Reunion semanal", "Revision de avances", 2);

        Tarea cancelada = gestor.cambiarEstado(tarea.getId(), Tarea.Estado.CANCELADA);

        assertEquals(Tarea.Estado.CANCELADA, cancelada.getEstado(),
                "El estado debe cambiar a CANCELADA");
    }

    @Test
    @DisplayName("2b. Tarea cancelada no aparece en la lista de pendientes")
    void obtenerPendientes_tareasCanceladas_noAparecenEnLaLista() {
        Tarea t1 = gestor.registrarTarea("Tarea X", "Desc X", 1);
        Tarea t2 = gestor.registrarTarea("Tarea Y", "Desc Y", 2);
        gestor.cambiarEstado(t1.getId(), Tarea.Estado.CANCELADA);

        List<Tarea> pendientes = gestor.obtenerPendientes();

        assertEquals(1, pendientes.size(),
                "Solo debe quedar 1 tarea pendiente tras cancelar la primera");
        assertEquals("Tarea Y", pendientes.get(0).getTitulo(),
                "La tarea pendiente restante debe ser 'Tarea Y'");
    }

    @Test
    @DisplayName("2c. Buscar tarea por ID existente retorna Optional con la tarea")
    void buscar_idExistente_retornaOptionalPresente() {
        Tarea registrada = gestor.registrarTarea("Refactorizar modulo", "Limpiar codigo legacy", 1);

        Optional<Tarea> resultado = gestor.buscar(registrada.getId());

        assertTrue(resultado.isPresent(),    "El Optional debe tener valor");
        assertEquals(registrada.getId(), resultado.get().getId(),
                "El ID de la tarea encontrada debe coincidir");
    }

    @Test
    @DisplayName("3a. Registrar tarea con titulo vacio lanza TituloInvalidoException")
    void registrarTarea_tituloVacio_lanzaExcepcion() {
        String tituloVacio = "   ";

        assertThrows(GestorTareas.TituloInvalidoException.class,
                () -> gestor.registrarTarea(tituloVacio, "Descripcion", 1),
                "Debe lanzar TituloInvalidoException con titulo en blanco");
    }

    @Test
    @DisplayName("3b. Registrar tarea con prioridad 0 (fuera de rango) lanza PrioridadInvalidaException")
    void registrarTarea_prioridadCero_lanzaExcepcion() {
        int prioridadInvalida = 0;

        assertThrows(GestorTareas.PrioridadInvalidaException.class,
                () -> gestor.registrarTarea("Tarea valida", "Descripcion", prioridadInvalida),
                "Debe lanzar PrioridadInvalidaException con prioridad = 0");
    }

    @Test
    @DisplayName("3c. Registrar tarea con prioridad 4 (fuera de rango superior) lanza PrioridadInvalidaException")
    void registrarTarea_prioridadCuatro_lanzaExcepcion() {
        int prioridadFueraDeRango = 4;

        assertThrows(GestorTareas.PrioridadInvalidaException.class,
                () -> gestor.registrarTarea("Tarea valida", "Descripcion", prioridadFueraDeRango),
                "Debe lanzar PrioridadInvalidaException con prioridad = 4");
    }

    @Test
    @DisplayName("3d. Cambiar estado de PENDIENTE a COMPLETADA (saltar EN_PROGRESO) lanza TransicionInvalidaException")
    void cambiarEstado_dePendienteACompletada_transicionInvalida_lanzaExcepcion() {
        Tarea tarea = gestor.registrarTarea("Subir reporte", "Reporte trimestral", 2);

        assertThrows(GestorTareas.TransicionInvalidaException.class,
                () -> gestor.cambiarEstado(tarea.getId(), Tarea.Estado.COMPLETADA),
                "No debe permitirse saltar de PENDIENTE directamente a COMPLETADA");
    }

    @Test
    @DisplayName("3e. Buscar tarea con ID inexistente retorna Optional vacio")
    void buscar_idInexistente_retornaOptionalVacio() {
        Optional<Tarea> resultado = gestor.buscar("T-999");

        assertFalse(resultado.isPresent(),
                "El Optional debe estar vacio para un ID que no existe");
    }

    @Test
    @DisplayName("3f. Cambiar estado de tarea con ID inexistente lanza TareaNoEncontradaException")
    void cambiarEstado_idInexistente_lanzaExcepcion() {
        assertThrows(GestorTareas.TareaNoEncontradaException.class,
                () -> gestor.cambiarEstado("T-999", Tarea.Estado.EN_PROGRESO),
                "Debe lanzar TareaNoEncontradaException para un ID inexistente");
    }

    @Test
    @DisplayName("3g. Sistema sin tareas: obtenerPendientes retorna lista vacia (no nula)")
    void obtenerPendientes_sinTareas_retornaListaVaciaNoNula() {
        List<Tarea> pendientes = gestor.obtenerPendientes();

        assertNotNull(pendientes,        "La lista no debe ser nula");
        assertTrue(pendientes.isEmpty(), "La lista debe estar vacia si no hay tareas");
    }
}
