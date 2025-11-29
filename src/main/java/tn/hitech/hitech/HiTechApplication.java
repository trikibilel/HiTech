package tn.hitech.hitech;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.hitech.Database.ArticleRepo;
import tn.hitech.Models.Article;

import java.io.IOException;

public class HiTechApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        ArticleRepo articleRepo = new ArticleRepo();
//        Article article = new Article(1,"eau","C://",20.1,60,10);
//        Article article2 = new Article(2,"biscuit","C://",20.1,60,10);
//        Article article3 = new Article(3,"tomate","C://",20.1,60,10);
//        articleRepo.insert(article);
//        articleRepo.insert(article2);
//        articleRepo.insert(article3);

        FXMLLoader fxmlLoader = new FXMLLoader(HiTechApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}
