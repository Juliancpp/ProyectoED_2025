package com.example.controllers;

import com.example.models.Paciente;
import com.example.models.Priority; // Asegúrate de importar tu Enum Priority
import com.example.services.HospitalService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class AtenderController {

    @FXML private Label lblId;
    @FXML private Label lblNombre;
    @FXML private Label lblEdad;
    @FXML private Label lblSintomas;
    @FXML private Label lblPrioridad;
    @FXML private Label lblConsultas;
    @FXML private Label lblMensaje;

    // Nuevos controles inyectados
    @FXML private ComboBox<Priority> cmbNuevaPrioridad;
    @FXML private Button btnActualizar;
    @FXML private Button btnAtenderSiguiente;

    private HospitalService hospitalService;

    public void setHospitalService(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @FXML
    public void initialize() {
        clearView();
        lblMensaje.setText("Listo para atender.");
        
        // Inicializar el ComboBox con los valores del Enum Priority
        // Asumiendo que Priority es un enum, usamos .values()
        cmbNuevaPrioridad.getItems().setAll(Priority.values());
    }

    @FXML
    private void onAtenderSiguiente() {
        if (hospitalService == null) {
            lblMensaje.setText("Error: Servicio no inicializado.");
            return;
        }

        Paciente p = hospitalService.atenderSiguiente();
        
        if (p == null) {
            lblMensaje.setText("No hay pacientes en la cola.");
            clearView();
            return;
        }
        mostrarPaciente(p);
        lblMensaje.setText("Paciente atendido exitosamente.");
    }

    // --- NUEVA LÓGICA AGREGADA ---
    @FXML
    private void onActualizarPrioridad() {
        // 1. Validar que haya un paciente cargado en pantalla
        if (lblId.getText().equals("-") || lblId.getText().isEmpty()) {
            lblMensaje.setText("No hay paciente seleccionado para actualizar.");
            return;
        }

        // 2. Obtener el ID del paciente actual
        int idPaciente;
        try {
            idPaciente = Integer.parseInt(lblId.getText());
        } catch (NumberFormatException e) {
            lblMensaje.setText("Error al leer ID del paciente.");
            return;
        }

        // 3. Obtener la nueva prioridad seleccionada
        Priority nuevaPrioridad = cmbNuevaPrioridad.getValue();
        if (nuevaPrioridad == null) {
            lblMensaje.setText("Seleccione una nueva prioridad.");
            return;
        }

        // 4. Llamar al servicio usando el método solicitado
        boolean exito = hospitalService.actualizarPrioridad(idPaciente, nuevaPrioridad);

        if (exito) {
            lblMensaje.setText("Prioridad actualizada y reencolado.");
            lblPrioridad.setText(nuevaPrioridad.name()); // Actualizamos la vista visualmente
            // Opcional: Limpiar la vista si el paciente "se fue" de la pantalla al reencolarse
            // clearView(); 
        } else {
            lblMensaje.setText("Error: No se encontró al paciente en el sistema.");
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
        } catch (Exception e) {
            lblConsultas.setText("-");
        }
        
        cmbNuevaPrioridad.setValue(null);
    }

    private void clearView() {
        lblId.setText("-");
        lblNombre.setText("-");
        lblEdad.setText("-");
        lblSintomas.setText("-");
        lblPrioridad.setText("-");
        lblConsultas.setText("-");
        cmbNuevaPrioridad.setValue(null);
    }
}