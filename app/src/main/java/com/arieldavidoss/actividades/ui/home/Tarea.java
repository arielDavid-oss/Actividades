package com.arieldavidoss.actividades.ui.home;

public class Tarea {
    private int id;
    private String nombre;
    private String descripcion;
    private int completada;
    private String fechaTerminacion;

    public Tarea(int id, String nombre, String descripcion, int completada, String fechaTerminacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.completada= completada;
        this.fechaTerminacion = fechaTerminacion;
    }



    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCompletada() {return completada;}

    public String getFechaTerminacion() {
        return fechaTerminacion;
    }

    public int getId() { return id;}

    public void setId(int id) { this.id = id; }
}
