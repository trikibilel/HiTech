package tn.hitech.hitech;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.hitech.Database.ClientRepo;
import tn.hitech.Models.Client;

import java.io.IOException;

public class LoginController {

    public TextField clientId;

    @FXML
    protected void onLoginButtonClick() {
        ClientRepo cr = new ClientRepo();
        Client c = cr.findByEmail(clientId.getText());
        if (c == null) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("commandes.fxml"));
            Parent root = loader.load();

            CommandeTableController controller = loader.getController();
            controller.setClient(c); // Passer le client connecté

            Stage stage = new Stage();
            stage.setTitle("Mes Commandes");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void onNewClientClick(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("addClientDialog.fxml")
            );
            Parent root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter Client");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Stage owner = (Stage) clientId.getScene().getWindow();
            dialogStage.initOwner(owner);
            dialogStage.setScene(new Scene(root));
            dialogStage.show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error loading client dialog").show();
        }
    }

    public void onClientsTableClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("clientsTable.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
