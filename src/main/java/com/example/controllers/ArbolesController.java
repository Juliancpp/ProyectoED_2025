package com.example.controllers;

import com.example.models.Paciente;
import com.example.services.HospitalService;
import com.example.utils.TreeVisualizer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

public class ArbolesController {

    @FXML private Canvas canvasABB;
    @FXML private Canvas canvasAVL;
    @FXML private Canvas canvasSplay;

    private HospitalService hospitalService;

    public void setHospitalService(HospitalService service) {
        this.hospitalService = service;
        
        dibujarArboles();

        if (this.hospitalService != null) {
            this.hospitalService.setOnArbolesChanged(() -> {
                Platform.runLater(this::dibujarArboles);
            });
        }
    }
    

    @FXML
    public void initialize() {
        configurarZoom(canvasABB);
        configurarZoom(canvasAVL);
        configurarZoom(canvasSplay);
    }

    private void dibujarArboles() {
        System.out.println("DEBUG: 3. dibujarArboles() ejecutándose en ArbolesController.");
        if (hospitalService == null) return;

        resetCanvas(canvasABB);
        resetCanvas(canvasAVL);
        resetCanvas(canvasSplay);

        TreeVisualizer<Integer, Paciente> visualABB = new TreeVisualizer<>(Color.DARKBLUE, Color.BLACK);
        TreeVisualizer<Integer, Paciente> visualAVL = new TreeVisualizer<>(Color.DARKGREEN, Color.BLACK);
        TreeVisualizer<Integer, Paciente> visualSplay = new TreeVisualizer<>(Color.DARKRED, Color.BLACK);

        visualABB.draw(hospitalService.getABB(), canvasABB);
        visualAVL.draw(hospitalService.getAVL(), canvasAVL);
        visualSplay.draw(hospitalService.getSplay(), canvasSplay);
    }

    private void resetCanvas(Canvas canvas) {
        canvas.setScaleX(1.0);
        canvas.setScaleY(1.0);
        canvas.setTranslateX(0);
        canvas.setTranslateY(0);
    }

    private void configurarZoom(Canvas canvas) {
        canvas.setOnScroll((ScrollEvent event) -> {
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();

            if (deltaY < 0) {
                zoomFactor = 0.95;
            }

            double newScaleX = canvas.getScaleX() * zoomFactor;
            double newScaleY = canvas.getScaleY() * zoomFactor;

            if (newScaleX >= 0.1 && newScaleX <= 10) {
                canvas.setScaleX(newScaleX);
                canvas.setScaleY(newScaleY);
            }
            event.consume();
        });
    }
}