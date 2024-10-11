package com.arieldavidoss.actividades.ui.home;

public class Tarea {
    private String nombre;
    private String descripcion;
    private String fechaTerminacion;

    public Tarea(String nombre, String descripcion, String fechaTerminacion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaTerminacion = fechaTerminacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFechaTerminacion() {
        return fechaTerminacion;
    }
}
