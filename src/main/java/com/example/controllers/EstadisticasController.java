package com.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import com.example.services.HospitalService;
import com.example.models.Paciente;
import com.example.models.TriageLevel;

public class EstadisticasController {

    @FXML private Label lblTotalPacientes;
    @FXML private Label lblEnEspera;
    @FXML private Label lblPromedioEspera;
    @FXML private Label lblMasConsultado;
    @FXML private Label lblTotalConsultas;
    @FXML private Label lblTotalInserciones;
    @FXML private Label lblTotalEliminaciones;

    @FXML private Label lblNivel1;
    @FXML private Label lblNivel2;
    @FXML private Label lblNivel3;
    @FXML private Label lblNivel4;
    @FXML private Label lblNivel5;

    private HospitalService hospitalService;

    public void setHospitalService(HospitalService hs) {
        this.hospitalService = hs;
        refrescar();
    }

    @FXML
    public void initialize() {
    }

    private void refrescar() {
        if (hospitalService == null) return;

        lblTotalPacientes.setText(String.valueOf(hospitalService.totalPacientes()));
        lblEnEspera.setText(String.valueOf(hospitalService.totalEnEspera()));

        double prom = hospitalService.tiempoPromedioEspera();
        lblPromedioEspera.setText(String.format("%.2f ms", prom));

        Paciente masConsultado = hospitalService.getMasConsultado();
        lblMasConsultado.setText(
                masConsultado != null ? masConsultado.toString() : "Ninguno"
        );

        lblTotalConsultas.setText(String.valueOf(hospitalService.getTotalConsultas()));
        lblTotalInserciones.setText(String.valueOf(hospitalService.getTotalInserciones()));
        lblTotalEliminaciones.setText(String.valueOf(hospitalService.getTotalEliminaciones()));

        // Conteos por triage
        lblNivel1.setText(String.valueOf(hospitalService.countByTriageLevel(TriageLevel.NIVEL_1_RESUCITACION)));
        lblNivel2.setText(String.valueOf(hospitalService.countByTriageLevel(TriageLevel.NIVEL_2_EMERGENCIA)));
        lblNivel3.setText(String.valueOf(hospitalService.countByTriageLevel(TriageLevel.NIVEL_3_URGENTE)));
        lblNivel4.setText(String.valueOf(hospitalService.countByTriageLevel(TriageLevel.NIVEL_4_MENOR)));
        lblNivel5.setText(String.valueOf(hospitalService.countByTriageLevel(TriageLevel.NIVEL_5_NO_URGENTE)));
    }
}
