package tn.hitech.hitech;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.hitech.Database.ClientPMRepo;
import tn.hitech.Database.ClientPPRepo;
import tn.hitech.Models.Client;
import tn.hitech.Models.ClientPM;
import tn.hitech.Models.ClientPP;

public class ClientDialogController {

    public RadioButton radioPP;

    public RadioButton radioPM;

    public ToggleGroup clientTypeGroup;

    public TextField txtId;

    public TextField txtAdresse;

    public TextField txtEmail;

    public TextField txtTelephone;

    public VBox vboxPP;

    public TextField txtNom;

    public TextField txtPrenom;

    public VBox vboxPM;

    public TextField txtMatricule;

    public TextField txtRaisonSociale;

    public Button btnAjouter;

    public Button btnAnnuler;

    private Client clientResult;
    private boolean isConfirmed = false;


    public void initialize() {
        // Add listener to toggle between PP and PM forms
        clientTypeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == radioPP) {
                showPersonPhysicalFields();
            } else if (newValue == radioPM) {
                showPersonMoralFields();
            }
        });

        // Set button actions
        btnAjouter.setOnAction(event -> handleAjouter());
        btnAnnuler.setOnAction(event -> handleAnnuler());

        // Initialize with PP fields visible
        showPersonPhysicalFields();
    }

    private void showPersonPhysicalFields() {
        vboxPP.setVisible(true);
        vboxPP.setManaged(true);
        vboxPM.setVisible(false);
        vboxPM.setManaged(false);

        // Clear PM fields
        txtMatricule.clear();
        txtRaisonSociale.clear();
    }

    private void showPersonMoralFields() {
        vboxPM.setVisible(true);
        vboxPM.setManaged(true);
        vboxPP.setVisible(false);
        vboxPP.setManaged(false);

        // Clear PP fields
        txtNom.clear();
        txtPrenom.clear();
    }

    private void handleAjouter() {
        // Validate common fields
        if (!validateCommonFields()) {
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String adresse = txtAdresse.getText().trim();
            String email = txtEmail.getText().trim();
            int telephone = Integer.parseInt(txtTelephone.getText().trim());

            if (radioPP.isSelected()) {
                // Create ClientPP
                if (!validatePersonPhysicalFields()) {
                    return;
                }

                String nom = txtNom.getText().trim();
                String prenom = txtPrenom.getText().trim();

                clientResult = new ClientPP(id, adresse, email, telephone, nom, prenom);
                ClientPPRepo pp = new ClientPPRepo();
                pp.insert((ClientPP) clientResult);

            } else if (radioPM.isSelected()) {
                // Create ClientPM
                if (!validatePersonMoralFields()) {
                    return;
                }

                String matricule = txtMatricule.getText().trim();
                String raisonSociale = txtRaisonSociale.getText().trim();

                clientResult = new ClientPM(id, adresse, email, telephone, matricule, raisonSociale);
                ClientPMRepo pm = new ClientPMRepo();
                pm.insert((ClientPM) clientResult);
            }

            isConfirmed = true;
            closeDialog();

        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "Veuillez vérifier que l'ID et le téléphone sont des nombres valides.", Alert.AlertType.ERROR);
        }
    }

    private void handleAnnuler() {
        isConfirmed = false;
        clientResult = null;
        closeDialog();
    }

    private boolean validateCommonFields() {
        if (txtId.getText().trim().isEmpty()) {
            showAlert("Champ requis", "Veuillez saisir l'ID du client.", Alert.AlertType.WARNING);
            txtId.requestFocus();
            return false;
        }

        if (txtAdresse.getText().trim().isEmpty()) {
            showAlert("Champ requis", "Veuillez saisir l'adresse du client.", Alert.AlertType.WARNING);
            txtAdresse.requestFocus();
            return false;
        }

        if (txtEmail.getText().trim().isEmpty()) {
            showAlert("Champ requis", "Veuillez saisir l'email du client.", Alert.AlertType.WARNING);
            txtEmail.requestFocus();
            return false;
        }

        if (txtTelephone.getText().trim().isEmpty()) {
            showAlert("Champ requis", "Veuillez saisir le téléphone du client.", Alert.AlertType.WARNING);
            txtTelephone.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validatePersonPhysicalFields() {
        if (txtNom.getText().trim().isEmpty()) {
            showAlert("Champ requis", "Veuillez saisir le nom du client.", Alert.AlertType.WARNING);
            txtNom.requestFocus();
            return false;
        }

        if (txtPrenom.getText().trim().isEmpty()) {
            showAlert("Champ requis", "Veuillez saisir le prénom du client.", Alert.AlertType.WARNING);
            txtPrenom.requestFocus();
            return false;
        }

        return true;
    }

    private boolean validatePersonMoralFields() {
        if (txtMatricule.getText().trim().isEmpty()) {
            showAlert("Champ requis", "Veuillez saisir le matricule de l'entreprise.", Alert.AlertType.WARNING);
            txtMatricule.requestFocus();
            return false;
        }

        if (txtRaisonSociale.getText().trim().isEmpty()) {
            showAlert("Champ requis", "Veuillez saisir la raison sociale de l'entreprise.", Alert.AlertType.WARNING);
            txtRaisonSociale.requestFocus();
            return false;
        }

        return true;
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeDialog() {
        Stage stage = (Stage) btnAjouter.getScene().getWindow();
        stage.close();
    }

    public Client getClientResult() {
        return clientResult;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    // Optional: Method to pre-fill form for editing
    public void setClient(Client client) {
        if (client instanceof ClientPP clientPP) {
            radioPP.setSelected(true);
            showPersonPhysicalFields();

            txtId.setText(String.valueOf(clientPP.getId()));
            txtAdresse.setText(clientPP.getAdresse());
            txtEmail.setText(clientPP.getEmail());
            txtTelephone.setText(String.valueOf(clientPP.getTelephone()));
            txtNom.setText(clientPP.getNom());
            txtPrenom.setText(clientPP.getPrenom());

        } else if (client instanceof ClientPM clientPM) {
            radioPM.setSelected(true);
            showPersonMoralFields();

            txtId.setText(String.valueOf(clientPM.getId()));
            txtAdresse.setText(clientPM.getAdresse());
            txtEmail.setText(clientPM.getEmail());
            txtTelephone.setText(String.valueOf(clientPM.getTelephone()));
            txtMatricule.setText(clientPM.getMatricule());
            txtRaisonSociale.setText(clientPM.getRaisonSociale());
        }
    }
}