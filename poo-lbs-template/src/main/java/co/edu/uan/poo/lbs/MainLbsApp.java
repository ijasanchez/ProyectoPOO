package co.edu.uan.poo.lbs;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainLbsApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
    	URL url = Thread.currentThread().getContextClassLoader().getResource("lbsServicesScene.fxml");
    	Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        stage.setTitle("Servicios basados en localización");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
