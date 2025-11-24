package com.example.models;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Paciente {
    
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    private final int id;
    private String nombre;
    private int edad;
    private String sintomas;
    private Priority prioridad;
    private final LocalDateTime fechaIngreso;
    private int accessCount = 0;

    public Paciente(String nombre, int edad, String sintomas, Priority prioridad) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.nombre = nombre;
        this.edad = edad;
        this.sintomas = sintomas;
        this.prioridad = prioridad;
        this.fechaIngreso = LocalDateTime.now();
    }

    // getters y setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }
    public Priority getPrioridad() { return prioridad; }
    public void setPrioridad(Priority prioridad) { this.prioridad = prioridad; }
    public LocalDateTime getFechaIngreso() { return fechaIngreso; }
    public int getAccessCount() { return accessCount; }
    public void incrementAccess() { accessCount++; }

    @Override
    public String toString() {
        return "[" + prioridad + "] ID:" + id + " - " + nombre + " (" + edad + ") - " + sintomas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paciente)) return false;
        Paciente p = (Paciente) o;
        return id == p.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
