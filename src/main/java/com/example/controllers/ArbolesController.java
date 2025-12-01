package com.example.controllers;

import com.example.models.Paciente;
import com.example.services.HospitalService;
import com.example.utils.TreeVisualizer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class ArbolesController {

    @FXML private Canvas canvasABB;
    @FXML private Canvas canvasAVL;
    @FXML private Canvas canvasSplay;

    private HospitalService hospitalService;

    public void setHospitalService(HospitalService service) {
        this.hospitalService = service;
        dibujarArboles();
    }

    @FXML
    public void initialize() {}

    private void dibujarArboles() {
        if (hospitalService == null) return;

        // ⬅⬅⬅ AQUÍ EL CAMBIO IMPORTANTE
        TreeVisualizer<Integer, Paciente> visualABB =
                new TreeVisualizer<>(Color.DARKBLUE, Color.BLACK);

        TreeVisualizer<Integer, Paciente> visualAVL =
                new TreeVisualizer<>(Color.DARKGREEN, Color.BLACK);

        TreeVisualizer<Integer, Paciente> visualSplay =
                new TreeVisualizer<>(Color.DARKRED, Color.BLACK);

        visualABB.draw(hospitalService.getABB(), canvasABB);
        visualAVL.draw(hospitalService.getAVL(), canvasAVL);
        visualSplay.draw(hospitalService.getSplay(), canvasSplay);
    }
}
