package tn.hitech.hitech;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.hitech.Database.ArticleRepo;
import tn.hitech.Database.ImprimanteRepo;
import tn.hitech.Database.SmartphoneRepo;
import tn.hitech.Models.Article;
import tn.hitech.Models.Imprimante;
import tn.hitech.Models.Smartphone;

import java.io.IOException;

public class HiTechApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HiTechApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}
