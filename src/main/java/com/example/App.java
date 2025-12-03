package com.example;

import java.io.IOException;
import java.net.URL;

import com.example.services.HospitalService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage stage;
    private static HospitalService hospitalService = new HospitalService();

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        setRoot("dashboard");

        stage.setTitle("Sistema de Triage - Hospital");
        stage.show();
    }

    public static FXMLLoader loadFXML(String name) {
        try {
            String path = "/com/example/" + name + ".fxml";
            URL fxmlUrl = App.class.getResource(path);

            if (fxmlUrl == null) {
                throw new IllegalStateException("ERROR: No se encontró el FXML → " + path);
            }

            return new FXMLLoader(fxmlUrl);

        } catch (Exception ex) {
            throw new RuntimeException("Error cargando FXML: " + name, ex);
        }
    }

    public static void setRoot(String fxml) {
        try {
            FXMLLoader loader = loadFXML(fxml);

            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            Object controller = loader.getController();
            injectService(controller);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo cargar: " + fxml);
        }
    }

    private static void injectService(Object controller) {
        try {
            controller.getClass()
                    .getMethod("setHospitalService", HospitalService.class)
                    .invoke(controller, hospitalService);
        } catch (Exception ignored) {
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
