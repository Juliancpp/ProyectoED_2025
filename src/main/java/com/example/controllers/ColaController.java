package com.example.controllers;

import com.example.models.Paciente;
import com.example.services.HospitalService;
import com.example.TDAs.SinglyLinkedList;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ColaController {

    @FXML private TableView<Paciente> tablaCola;
    @FXML private TableColumn<Paciente, String> colNombre;
    @FXML private TableColumn<Paciente, Number> colEdad;
    @FXML private TableColumn<Paciente, String> colPrioridad;

    @FXML private Label lblId;
    @FXML private Label lblNombre;
    @FXML private Label lblEdad;
    @FXML private Label lblSintomas;
    @FXML private Label lblPrioridad;
    @FXML private Label lblConsultas;

    private HospitalService hospitalService;

    private final ObservableList<Paciente> dataCola = FXCollections.observableArrayList();

    public void setHospitalService(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
        cargarCola();
    }

    @FXML
    public void initialize() {

        colNombre.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getNombre())
        );

        colEdad.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getEdad())
        );

        colPrioridad.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getPrioridad().name())
        );

        tablaCola.setItems(dataCola);

        tablaCola.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> mostrarDetalles(newSel)
        );
    }

    private void cargarCola() {
        if (hospitalService == null) return;

        dataCola.clear();

        SinglyLinkedList<Paciente> cola = hospitalService.obtenerColaPrioridad();
        if (cola != null && !cola.isEmpty()) {

            for (Paciente p : cola.traverse()) {
                dataCola.add(p);
            }
        }
    }

    private void mostrarDetalles(Paciente p) {
        if (p == null) {
            clear();
            return;
        }

        lblId.setText(String.valueOf(p.getId()));
        lblNombre.setText(p.getNombre());
        lblEdad.setText(String.valueOf(p.getEdad()));
        lblSintomas.setText(p.getSintomas());
        lblPrioridad.setText(p.getPrioridad().name());

        try {
            lblConsultas.setText(String.valueOf(p.getNumeroConsultas()));
        } catch (Throwable e) {
            lblConsultas.setText("-");
        }
    }

    private void clear() {
        lblId.setText("-");
        lblNombre.setText("-");
        lblEdad.setText("-");
        lblSintomas.setText("-");
        lblPrioridad.setText("-");
        lblConsultas.setText("-");
    }
}
