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

    // Campos de detalle
    @FXML private Label lblId;
    @FXML private Label lblNombre;
    @FXML private Label lblEdad;
    @FXML private Label lblSintomas;
    @FXML private Label lblPrioridad;
    @FXML private Label lblConsultas;

    // NUEVO: Campo de texto para búsqueda
    @FXML private TextField txtBuscarId;

    private HospitalService hospitalService;

    private final ObservableList<Paciente> dataCola = FXCollections.observableArrayList();

    public void setHospitalService(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
        cargarCola();
        
        // Listener para refrescar la tabla si hay cambios externos
        this.hospitalService.setOnArbolesChanged(() -> {
            javafx.application.Platform.runLater(this::cargarCola);
        });
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

        // Al hacer clic en la tabla, mostrar detalles
        tablaCola.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> mostrarDetalles(newSel)
        );
        
        clear();
    }

    // --- NUEVA LÓGICA DE BÚSQUEDA ---
    @FXML
    private void onBuscarPaciente() {
        if (hospitalService == null) return;
        
        String textoId = txtBuscarId.getText().trim();
        if (textoId.isEmpty()) {
            mostrarAlerta("Atención", "Por favor ingrese un ID.");
            return;
        }

        try {
            int id = Integer.parseInt(textoId);

            // 1. Usar la función del servicio (esto dispara el Splay y cuenta la consulta)
            Paciente p = hospitalService.buscarPaciente(id);

            if (p != null) {
                // 2. Mostrar sus detalles en el panel inferior
                mostrarDetalles(p);
                
                // 3. (Opcional) Intentar seleccionarlo visualmente si está en la tabla de cola
                boolean encontradoEnTabla = false;
                for (Paciente enCola : dataCola) {
                    if (enCola.getId() == p.getId()) {
                        tablaCola.getSelectionModel().select(enCola);
                        tablaCola.scrollTo(enCola);
                        encontradoEnTabla = true;
                        break;
                    }
                }
                
                if (!encontradoEnTabla) {
                    // Si el paciente existe pero ya no está en la cola (ya fue atendido)
                    // Deseleccionamos la tabla para no confundir
                    tablaCola.getSelectionModel().clearSelection();
                }

            } else {
                mostrarAlerta("No encontrado", "No existe ningún paciente con el ID " + id);
                clear();
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "El ID debe ser un número entero.");
        }
    }

    @FXML
    private void onLimpiarBusqueda() {
        txtBuscarId.setText("");
        tablaCola.getSelectionModel().clearSelection();
        clear();
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
    // --------------------------------

    private void cargarCola() {
        if (hospitalService == null) return;

        // Guardar selección actual si es posible para restaurarla tras recargar
        Paciente seleccionadoPrevio = tablaCola.getSelectionModel().getSelectedItem();

        dataCola.clear();
        SinglyLinkedList<Paciente> cola = hospitalService.obtenerColaPrioridad();
        if (cola != null && !cola.isEmpty()) {
            for (Paciente p : cola.traverse()) {
                dataCola.add(p);
            }
        }
        
        // Restaurar selección si sigue en la lista
        if (seleccionadoPrevio != null && dataCola.contains(seleccionadoPrevio)) {
            tablaCola.getSelectionModel().select(seleccionadoPrevio);
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
            // Esto mostrará el número actualizado de consultas gracias al buscarPaciente()
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