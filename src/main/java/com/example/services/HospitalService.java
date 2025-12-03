package com.example.services;

import com.example.models.Paciente;
import com.example.models.Priority;
import com.example.models.TriageLevel;
import com.example.TDAs.TreeMap;

import java.time.Duration;
import java.time.LocalDateTime;

import com.example.TDAs.AVLTreeMap;
import com.example.TDAs.SplayTreeMap;
import com.example.TDAs.Entry;
import com.example.TDAs.SinglyLinkedList;

public class HospitalService {

    private final TreeMap<Integer, Paciente> abbPacientes;
    private final AVLTreeMap<Integer, Paciente> avlPacientes;
    private final SplayTreeMap<Integer, Paciente> splayPacientes;
    private Runnable onArbolesChanged;

    private final TriageService triageService;

    private int totalInserciones = 0;
    private int totalEliminaciones = 0;
    private int totalConsultas = 0;
    private Paciente ultimoAtendido = null;
    private long tiempoAcumuladoEspera = 0; 
    private long pacientesConTiempo = 0;    

    public HospitalService() {
        this.abbPacientes = new TreeMap<>();
        this.avlPacientes = new AVLTreeMap<>();
        this.splayPacientes = new SplayTreeMap<>();
        this.triageService = new TriageService();
    }

    public void setOnArbolesChanged(Runnable listener) {
        this.onArbolesChanged = listener;
    }

    public void registrarPaciente(Paciente p) {

        abbPacientes.put(p.getId(), p);
        avlPacientes.put(p.getId(), p);
        splayPacientes.put(p.getId(), p);

        triageService.addPaciente(p);
        notificarCambios();

        totalInserciones++;
    }

    public boolean eliminarPaciente(int id) {
        Paciente p = abbPacientes.get(id);
        if (p == null) return false;

        abbPacientes.remove(id);
        avlPacientes.remove(id);
        splayPacientes.remove(id);

        triageService.removeById(id);

        notificarCambios();
        totalEliminaciones++;
        return true;
    }

    public Paciente buscarPaciente(int id) {
        totalConsultas++;
        return splayPacientes.get(id);
    }

    public Paciente atenderSiguiente() {
    Paciente p = triageService.pollNext();
    if (p == null) return null;

    ultimoAtendido = p;

    long tiempoEspera = Duration.between(p.getFechaIngreso(), LocalDateTime.now()).toMillis();

    tiempoAcumuladoEspera += tiempoEspera;
    pacientesConTiempo++;
    p.registrarConsulta();
    
    return p;
    }

    public boolean actualizarPrioridad(int id, Priority nueva) {
        Paciente p = abbPacientes.get(id);
        if (p == null) return false;

        triageService.updatePriority(id, nueva);
        p.setPrioridad(nueva);

        abbPacientes.put(id, p);
        avlPacientes.put(id, p);
        splayPacientes.put(id, p);
        notificarCambios();

        return true;
    }

    public SinglyLinkedList<Paciente> obtenerPacientesOrdenados() {
        SinglyLinkedList<Paciente> list = new SinglyLinkedList<>();
        for (Entry<Integer, Paciente> e : abbPacientes.entrySet()) {
            list.addLast(e.getValue());
        }
        return list;
    }

    public SinglyLinkedList<Paciente> obtenerColaPrioridad() {
        return triageService.snapshotOrdered();
    }

    public int totalPacientes() { return abbPacientes.size(); }
    public int totalEnEspera() { return triageService.size(); }

    public double tiempoPromedioEspera() {
        if (pacientesConTiempo == 0) return 0;
        return (double) tiempoAcumuladoEspera / pacientesConTiempo;
    }

    public Paciente getUltimoAtendido() { return ultimoAtendido; }

    public Paciente getMasConsultado() {
        if (splayPacientes.isEmpty()) return null;
        return splayPacientes.root().getElement().getValue();
    }

    public int getTotalConsultas() {
        return totalConsultas;
    }

    public int getTotalInserciones() {
        return totalInserciones;
    }

    public int getTotalEliminaciones() {
        return totalEliminaciones;
    }

    public int countByTriageLevel(TriageLevel level) {
    return triageService.countByLevel(level);
    }

    public Paciente verSiguiente() {
    return triageService.peekNext();
    }

    private void notificarCambios() {
        System.out.println("DEBUG: 1. notificarCambios() llamado en el Servicio.");
        if (onArbolesChanged != null) {
            System.out.println("DEBUG: 2. Ejecutando el listener (El controlador está escuchando).");
            onArbolesChanged.run();
        } else {
            System.out.println("DEBUG: ERROR - onArbolesChanged es NULL. Nadie está escuchando.");
        }
    }

    public TreeMap<Integer, Paciente> getABB() { return abbPacientes; }
    public AVLTreeMap<Integer, Paciente> getAVL() { return avlPacientes; }
    public SplayTreeMap<Integer, Paciente> getSplay() { return splayPacientes; }

}
