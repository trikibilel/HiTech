package tn.hitech.hitech;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.hitech.Database.config.DbInitialiser;

import java.io.IOException;

public class HiTechApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DbInitialiser.initialise();
        FXMLLoader fxmlLoader = new FXMLLoader(HiTechApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}
