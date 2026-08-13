package com.t14.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GestorTareas {

    public static class TituloInvalidoException extends RuntimeException {
        public TituloInvalidoException() { super("El titulo no puede estar vacio."); }
    }

    public static class PrioridadInvalidaException extends RuntimeException {
        public PrioridadInvalidaException() { super("Prioridad invalida. Use 1 (alta), 2 (media) o 3 (baja)."); }
    }

    public static class TareaNoEncontradaException extends RuntimeException {
        public TareaNoEncontradaException(String id) { super("Tarea no encontrada: " + id); }
    }

    public static class TransicionInvalidaException extends RuntimeException {
        public TransicionInvalidaException(Tarea.Estado actual, Tarea.Estado destino) {
            super("Transicion invalida de " + actual + " a " + destino);
        }
    }

    private final List<Tarea> tareas = new ArrayList<>();
    private int contadorId = 1;

    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new TituloInvalidoException();
        }
    }

    private void validarPrioridad(int prioridad) {
        if (prioridad < 1 || prioridad > 3) {
            throw new PrioridadInvalidaException();
        }
    }

    private String generarId() {
        return "T-" + contadorId++;
    }

    private Tarea buscarPorId(String id) {
        return tareas.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TareaNoEncontradaException(id));
    }

    private boolean transicionPermitida(Tarea.Estado actual, Tarea.Estado destino) {
        return switch (destino) {
            case EN_PROGRESO -> actual == Tarea.Estado.PENDIENTE;
            case COMPLETADA  -> actual == Tarea.Estado.EN_PROGRESO;
            case CANCELADA   -> actual != Tarea.Estado.COMPLETADA;
            default          -> false;
        };
    }

    public Tarea registrarTarea(String titulo, String descripcion, int prioridad) {
        validarTitulo(titulo);
        validarPrioridad(prioridad);
        Tarea tarea = new Tarea(generarId(), titulo.trim(), descripcion, prioridad);
        tareas.add(tarea);
        return tarea;
    }

    public Tarea cambiarEstado(String id, Tarea.Estado nuevoEstado) {
        Tarea tarea = buscarPorId(id);
        if (!transicionPermitida(tarea.getEstado(), nuevoEstado)) {
            throw new TransicionInvalidaException(tarea.getEstado(), nuevoEstado);
        }
        tarea.setEstado(nuevoEstado);
        return tarea;
    }

    public List<Tarea> obtenerPendientes() {
        return tareas.stream()
                .filter(t -> t.getEstado() == Tarea.Estado.PENDIENTE)
                .collect(Collectors.toList());
    }

    public List<Tarea> obtenerTodas() {
        return new ArrayList<>(tareas);
    }

    public Optional<Tarea> buscar(String id) {
        return tareas.stream().filter(t -> t.getId().equals(id)).findFirst();
    }
}
