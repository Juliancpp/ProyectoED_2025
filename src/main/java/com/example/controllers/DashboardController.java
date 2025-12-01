package com.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import com.example.services.HospitalService;
import com.example.models.TriageLevel;
import java.io.IOException;

public class DashboardController {

    @FXML private AnchorPane contentPane;

    @FXML private Label lblTotalPacientes;
    @FXML private Label lblEnEspera;
    @FXML private Label lblUrgente;
    @FXML private Label lblMedio;
    @FXML private Label lblLeve;
    @FXML private Label lblUltimoAtendido;

    private final HospitalService hospitalService = new HospitalService();

    @FXML
    public void initialize() {
        mostrarRegistro();
        refrescarDashboard();
    }

    @FXML
    private void mostrarRegistro() {
        cargarVistaEnContentPane("/views/registro.fxml");
    }

    @FXML
    private void mostrarAtender() {
        cargarVistaEnContentPane("/views/atender.fxml");
    }

    @FXML
    private void mostrarCola() {
        cargarVistaEnContentPane("/views/cola.fxml");
    }

    @FXML
    private void mostrarArboles() {
        cargarVistaEnContentPane("/views/arboles.fxml");
    }

    @FXML
    private void mostrarEstadisticas() {
        cargarVistaEnContentPane("/views/estadisticas.fxml");
    }

    @FXML
    private void refrescarDashboard() {

        lblTotalPacientes.setText(String.valueOf(hospitalService.totalPacientes()));
        lblEnEspera.setText(String.valueOf(hospitalService.totalEnEspera()));

        lblUrgente.setText(String.valueOf(hospitalService.countByTriageLevel(TriageLevel.NIVEL_2_EMERGENCIA)));
        lblMedio.setText(String.valueOf(hospitalService.countByTriageLevel(TriageLevel.NIVEL_3_URGENTE)));
        lblLeve.setText(String.valueOf(hospitalService.countByTriageLevel(TriageLevel.NIVEL_5_NO_URGENTE)));

        if (hospitalService.getUltimoAtendido() != null) {
            lblUltimoAtendido.setText("Último atendido: " + hospitalService.getUltimoAtendido().toString());
        } else {
            lblUltimoAtendido.setText("Último atendido: -");
        }
    }

    private void cargarVistaEnContentPane(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane vista = loader.load();

            Object controller = loader.getController();
            if (controller != null) {
                try {
                    var m = controller.getClass().getMethod("setHospitalService", HospitalService.class);
                    if (m != null) {
                        m.invoke(controller, hospitalService);
                    }
                } catch (NoSuchMethodException nsme) {
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

            contentPane.getChildren().setAll(vista);
            AnchorPane.setTopAnchor(vista, 0.0);
            AnchorPane.setBottomAnchor(vista, 0.0);
            AnchorPane.setLeftAnchor(vista, 0.0);
            AnchorPane.setRightAnchor(vista, 0.0);

            refrescarDashboard();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void solicitarRefresco() {
        refrescarDashboard();
    }

    public HospitalService getHospitalService() {
        return hospitalService;
    }
}
