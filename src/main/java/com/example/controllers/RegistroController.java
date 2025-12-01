package com.example.controllers;

import com.example.models.Paciente;
import com.example.models.Priority;
import com.example.services.HospitalService;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegistroController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtEdad;
    @FXML private TextArea txtSintomas;
    @FXML private ComboBox<Priority> comboPrioridad;
    @FXML private Label lblEstado;

    private HospitalService hospitalService;

    @FXML
    public void initialize() {
        comboPrioridad.getItems().setAll(Priority.values());
    }

    // Este método lo llama DashboardController automáticamente
    public void setHospitalService(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @FXML
    private void registrarPaciente() {
        try {
            String nombre = txtNombre.getText().trim();
            String edadStr = txtEdad.getText().trim();
            String sintomas = txtSintomas.getText().trim();
            Priority prioridad = comboPrioridad.getValue();

            // VALIDACIONES
            if (nombre.isEmpty() || edadStr.isEmpty() || sintomas.isEmpty() || prioridad == null) {
                lblEstado.setText("❌ Complete todos los campos.");
                return;
            }

            int edad = Integer.parseInt(edadStr);

            // CREAR PACIENTE
            Paciente nuevo = new Paciente(nombre, edad, sintomas, prioridad);

            // Registrar en HospitalService
            hospitalService.registrarPaciente(nuevo);

            lblEstado.setText("✔ Paciente registrado con ID: " + nuevo.getId());

            // Limpiar campos
            txtNombre.clear();
            txtEdad.clear();
            txtSintomas.clear();
            comboPrioridad.getSelectionModel().clearSelection();

        } catch (NumberFormatException e) {
            lblEstado.setText("❌ La edad debe ser un número válido.");
        } catch (Exception ex) {
            lblEstado.setText("⚠ Error inesperado al registrar.");
            ex.printStackTrace();
        }
    }
}
