package com.t14.tasks;

import java.util.ArrayList;
import java.util.List;

public class GestorTareasOriginal {

    private List<Tarea> tareas = new ArrayList<>();
    private int contadorId = 1;

    public void registrarTarea(String titulo, String descripcion, int prioridad) {
        if (titulo == null || titulo.trim().isEmpty()) {
            System.out.println("Error: el titulo no puede estar vacio.");
            return;
        }
        if (prioridad < 1 || prioridad > 3) {
            System.out.println("Error: prioridad invalida. Use 1 (alta), 2 (media) o 3 (baja).");
            return;
        }
        String id = "T-" + contadorId++;
        Tarea t = new Tarea(id, titulo.trim(), descripcion, prioridad);
        tareas.add(t);
        System.out.println("Tarea registrada: " + t);
    }

    public void cambiarEstado(String id, String nuevoEstado) {
        boolean encontrada = false;
        for (Tarea t : tareas) {
            if (t.getId().equals(id)) {
                encontrada = true;
                if (nuevoEstado.equalsIgnoreCase("EN_PROGRESO")) {
                    if (t.getEstado() == Tarea.Estado.PENDIENTE) {
                        t.setEstado(Tarea.Estado.EN_PROGRESO);
                        System.out.println("Estado cambiado a EN_PROGRESO para: " + t.getTitulo());
                    } else {
                        System.out.println("Transicion no valida desde: " + t.getEstado());
                    }
                } else if (nuevoEstado.equalsIgnoreCase("COMPLETADA")) {
                    if (t.getEstado() == Tarea.Estado.EN_PROGRESO) {
                        t.setEstado(Tarea.Estado.COMPLETADA);
                        System.out.println("Estado cambiado a COMPLETADA para: " + t.getTitulo());
                    } else {
                        System.out.println("Transicion no valida desde: " + t.getEstado());
                    }
                } else if (nuevoEstado.equalsIgnoreCase("CANCELADA")) {
                    t.setEstado(Tarea.Estado.CANCELADA);
                    System.out.println("Tarea cancelada: " + t.getTitulo());
                } else {
                    System.out.println("Estado desconocido: " + nuevoEstado);
                }
                break;
            }
        }
        if (!encontrada) {
            System.out.println("Tarea no encontrada con id: " + id);
        }
    }

    public void listarPendientes() {
        System.out.println("=== TAREAS PENDIENTES ===");
        for (Tarea t : tareas) {
            if (t.getEstado() == Tarea.Estado.PENDIENTE) {
                System.out.println(t);
            }
        }
    }
}
