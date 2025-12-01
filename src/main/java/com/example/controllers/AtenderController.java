package com.example.controllers;

import com.example.models.Paciente;
import com.example.services.HospitalService;
import com.example.TDAs.SinglyLinkedList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AtenderController {

    @FXML private Label lblId;
    @FXML private Label lblNombre;
    @FXML private Label lblEdad;
    @FXML private Label lblSintomas;
    @FXML private Label lblPrioridad;
    @FXML private Label lblConsultas;
    @FXML private Label lblMensaje;

    @FXML private Button btnVerSiguiente;
    @FXML private Button btnAtenderSiguiente;

    private HospitalService hospitalService;
    private Paciente pacienteActual;

    public void setHospitalService(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
        mostrarSiguienteEnCola();
    }

    @FXML
    public void initialize() {
        clearView();
    }

    @FXML
    private void onAtenderSiguiente() {
        if (hospitalService == null) {
            lblMensaje.setText("Servicio no inicializado.");
            return;
        }

        Paciente p = hospitalService.atenderSiguiente();
        if (p == null) {
            lblMensaje.setText("No hay pacientes en la cola.");
            clearView();
            return;
        }

        this.pacienteActual = p;
        mostrarPaciente(p);
        lblMensaje.setText("Paciente atendido correctamente (ID: " + p.getId() + ").");
    }

    @FXML
    private void onVerSiguiente() {
        mostrarSiguienteEnCola();
    }

    private void mostrarSiguienteEnCola() {
        if (hospitalService == null) {
            lblMensaje.setText("Servicio no inicializado.");
            return;
        }
        SinglyLinkedList<Paciente> cola = hospitalService.obtenerColaPrioridad();
        Paciente siguiente = (cola == null) ? null : cola.first();
        if (siguiente == null) {
            lblMensaje.setText("No hay pacientes en la cola.");
            clearView();
        } else {
            lblMensaje.setText("Siguiente en cola (sin atender):");
            mostrarPaciente(siguiente);
        }
    }

    private void mostrarPaciente(Paciente p) {
        lblId.setText(String.valueOf(p.getId()));
        lblNombre.setText(p.getNombre());
        lblEdad.setText(String.valueOf(p.getEdad()));
        lblSintomas.setText(p.getSintomas());
        lblPrioridad.setText(p.getPrioridad().name());
        try {
            lblConsultas.setText(String.valueOf(p.getNumeroConsultas()));
        } catch (Throwable t) {
            try { lblConsultas.setText(String.valueOf(p.getNumeroConsultas())); } catch (Throwable t2) {
                lblConsultas.setText("-");
            }
        }
    }

    private void clearView() {
        lblId.setText("-");
        lblNombre.setText("-");
        lblEdad.setText("-");
        lblSintomas.setText("-");
        lblPrioridad.setText("-");
        lblConsultas.setText("-");
    }
}
