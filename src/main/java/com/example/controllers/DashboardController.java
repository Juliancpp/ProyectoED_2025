package com.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
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
        hospitalService.setOnArbolesChanged(this::refrescarDashboard);

        mostrarRegistro();
        refrescarDashboard();
    }

    @FXML private void mostrarRegistro() { cargarVistaEnContentPane("/com/example/registro.fxml"); }
    @FXML private void mostrarAtender() { cargarVistaEnContentPane("/com/example/atender.fxml"); }
    @FXML private void mostrarCola() { cargarVistaEnContentPane("/com/example/cola.fxml"); }
    @FXML private void mostrarArboles() { cargarVistaEnContentPane("/com/example/arboles.fxml"); }
    @FXML private void mostrarEstadisticas() { cargarVistaEnContentPane("/com/example/estadisticas.fxml"); }

    @FXML
    private void refrescarDashboard() {

        javafx.application.Platform.runLater(() -> {
            lblTotalPacientes.setText(String.valueOf(hospitalService.totalPacientes()));
            lblEnEspera.setText(String.valueOf(hospitalService.totalEnEspera()));

            lblUrgente.setText(String.valueOf(hospitalService.countByTriageLevel(TriageLevel.NIVEL_2_EMERGENCIA)));
            lblMedio.setText(String.valueOf(hospitalService.countByTriageLevel(TriageLevel.NIVEL_3_URGENTE)));
            lblLeve.setText(String.valueOf(hospitalService.countByTriageLevel(TriageLevel.NIVEL_5_NO_URGENTE)));

            if (hospitalService.getUltimoAtendido() != null) {
                lblUltimoAtendido.setText("Último: " + hospitalService.getUltimoAtendido().getNombre());
            } else {
                lblUltimoAtendido.setText("Último: -");
            }
        });
    }

    private void cargarVistaEnContentPane(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent vista = loader.load(); 

            Object controller = loader.getController();
            
            if (controller != null) {
                try {
                    var method = controller.getClass().getMethod("setHospitalService", HospitalService.class);
                    method.invoke(controller, hospitalService);
                    System.out.println("DEBUG: Servicio inyectado correctamente en " + controller.getClass().getSimpleName());
                } catch (NoSuchMethodException nsme) {
                    System.err.println("ADVERTENCIA: El controlador " + controller.getClass().getSimpleName() + " no tiene el método setHospitalService.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            contentPane.getChildren().setAll(vista);

            AnchorPane.setTopAnchor(vista, 0.0);
            AnchorPane.setBottomAnchor(vista, 0.0);
            AnchorPane.setLeftAnchor(vista, 0.0);
            AnchorPane.setRightAnchor(vista, 0.0);

        } catch (IOException e) {
            System.err.println("CRITICAL ERROR: No se pudo cargar el FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }
    
    public HospitalService getHospitalService() {
        return hospitalService;
    }
}