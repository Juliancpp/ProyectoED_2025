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
        resetLabels();
    }

    private void refrescar() {
        if (hospitalService == null) return;

        try {
            lblTotalPacientes.setText(String.valueOf(hospitalService.totalPacientes()));
            lblEnEspera.setText(String.valueOf(hospitalService.totalEnEspera()));

            double prom = hospitalService.tiempoPromedioEspera();
            lblPromedioEspera.setText(String.format("%.2f ms", prom));

            try {
                Paciente masConsultado = hospitalService.getMasConsultado();
                lblMasConsultado.setText(
                        masConsultado != null ? masConsultado.getNombre() : "Ninguno"
                );
            } catch (Exception e) {
                System.err.println("Error obteniendo mas consultado: " + e.getMessage());
                lblMasConsultado.setText("Error");
            }

            lblTotalConsultas.setText(String.valueOf(hospitalService.getTotalConsultas()));
            lblTotalInserciones.setText(String.valueOf(hospitalService.getTotalInserciones()));
            lblTotalEliminaciones.setText(String.valueOf(hospitalService.getTotalEliminaciones()));

            lblNivel1.setText(String.valueOf(hospitalService.countByTriageLevel(TriageLevel.NIVEL_1_RESUCITACION)));
            lblNivel2.setText(String.valueOf(hospitalService.countByTriageLevel(TriageLevel.NIVEL_2_EMERGENCIA)));
            lblNivel3.setText(String.valueOf(hospitalService.countByTriageLevel(TriageLevel.NIVEL_3_URGENTE)));
            lblNivel4.setText(String.valueOf(hospitalService.countByTriageLevel(TriageLevel.NIVEL_4_MENOR)));
            lblNivel5.setText(String.valueOf(hospitalService.countByTriageLevel(TriageLevel.NIVEL_5_NO_URGENTE)));

        } catch (Exception e) {
            System.err.println("Error general en estadísticas:");
            e.printStackTrace();
        }
    }
    
    private void resetLabels() {
        String dash = "-";
        lblTotalPacientes.setText(dash);
        lblEnEspera.setText(dash);
        lblPromedioEspera.setText(dash);
        lblMasConsultado.setText(dash);
        lblTotalConsultas.setText(dash);
        lblTotalInserciones.setText(dash);
        lblTotalEliminaciones.setText(dash);
        lblNivel1.setText(dash);
        lblNivel2.setText(dash);
        lblNivel3.setText(dash);
        lblNivel4.setText(dash);
        lblNivel5.setText(dash);
    }
}