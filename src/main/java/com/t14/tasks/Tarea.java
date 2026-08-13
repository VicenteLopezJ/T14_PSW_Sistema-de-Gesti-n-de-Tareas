package com.t14.tasks;

public class Tarea {

    public enum Estado {
        PENDIENTE, EN_PROGRESO, COMPLETADA, CANCELADA
    }

    private String id;
    private String titulo;
    private String descripcion;
    private Estado estado;
    private int prioridad;

    public Tarea(String id, String titulo, String descripcion, int prioridad) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = Estado.PENDIENTE;
    }

    public String getId()          { return id; }
    public String getTitulo()      { return titulo; }
    public String getDescripcion() { return descripcion; }
    public Estado getEstado()      { return estado; }
    public int getPrioridad()      { return prioridad; }

    public void setEstado(Estado estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "[" + id + "] " + titulo + " | Estado: " + estado + " | Prioridad: " + prioridad;
    }
}
