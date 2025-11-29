package tn.hitech.hitech;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.hitech.Database.ClientPMRepo;
import tn.hitech.Database.ClientPPRepo;
import tn.hitech.Database.ClientRepo;
import tn.hitech.Models.Client;
import tn.hitech.Models.ClientPM;
import tn.hitech.Models.ClientPP;

public class ClientTableController {

    @FXML
    private Button btnRefresh;

    @FXML
    private RadioButton radioAll;
    @FXML
    private RadioButton radioPP;
    @FXML
    private RadioButton radioPM;
    @FXML
    private ToggleGroup filterGroup;

    @FXML
    private TableView<Client> tableClients;
    @FXML
    private TableColumn<Client, Integer> colId;
    @FXML
    private TableColumn<Client, String> colType;
    @FXML
    private TableColumn<Client, String> colName;
    @FXML
    private TableColumn<Client, String> colAdresse;
    @FXML
    private TableColumn<Client, String> colEmail;
    @FXML
    private TableColumn<Client, Integer> colTelephone;
    @FXML
    private TableColumn<Client, String> colDetails;

    @FXML
    private Label lblStatus;

    private ObservableList<Client> clientList;
    private ObservableList<Client> filteredList;
    private ClientRepo clientRepo;
    private ClientPPRepo clientPPRepo;
    private ClientPMRepo clientPMRepo;

    @FXML
    public void initialize() {
        clientRepo = new ClientRepo();
        clientPPRepo = new ClientPPRepo();
        clientPMRepo = new ClientPMRepo();
        clientList = FXCollections.observableArrayList();
        filteredList = FXCollections.observableArrayList();

        setupTableColumns();
        setupEventHandlers();
        loadClients();
    }

    private void setupTableColumns() {
        // ID Column
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Type Column - determines if PP or PM
        colType.setCellValueFactory(cellData -> {
            Client client = cellData.getValue();
            String type = client instanceof ClientPP ? "Personne Physique" :
                    client instanceof ClientPM ? "Personne Morale" : "Client";
            return new SimpleStringProperty(type);
        });

        // Name Column - shows nom/prenom for PP, raison sociale for PM
        colName.setCellValueFactory(cellData -> {
            Client client = cellData.getValue();
            String name = "";
            if (client instanceof ClientPP) {
                ClientPP pp = (ClientPP) client;
                name = pp.getNom() + " " + pp.getPrenom();
            } else if (client instanceof ClientPM) {
                ClientPM pm = (ClientPM) client;
                name = pm.getRaisonSociale();
            }
            return new SimpleStringProperty(name);
        });

        // Address Column
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        // Email Column
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Phone Column
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));

        // Details Column - shows additional info
        colDetails.setCellValueFactory(cellData -> {
            Client client = cellData.getValue();
            String details = "";
            if (client instanceof ClientPP) {
                details = "Individuel";
            } else if (client instanceof ClientPM) {
                ClientPM pm = (ClientPM) client;
                details = "Mat: " + pm.getMatricule();
            }
            return new SimpleStringProperty(details);
        });

        // Center align ID and Phone columns
        colId.setStyle("-fx-alignment: CENTER;");
        colTelephone.setStyle("-fx-alignment: CENTER;");
    }

    private void setupEventHandlers() {
        // Refresh button
        btnRefresh.setOnAction(event -> loadClients());

        // Filter radio buttons
        filterGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> applyFilter());
    }

    private void loadClients() {
        clientList.setAll(clientRepo.findAll());
        applyFilter();
        updateStatus();
    }

    private void applyFilter() {
        filteredList.clear();

        if (radioAll.isSelected()) {
            filteredList.addAll(clientList);
        } else if (radioPP.isSelected()) {
            clientList.stream()
                    .filter(client -> client instanceof ClientPP)
                    .forEach(filteredList::add);
        } else if (radioPM.isSelected()) {
            clientList.stream()
                    .filter(client -> client instanceof ClientPM)
                    .forEach(filteredList::add);
        }

        tableClients.setItems(filteredList);
        updateStatus();
    }

    private void updateStatus() {
        int total = tableClients.getItems().size();
        lblStatus.setText("Total: " + total + " client" + (total > 1 ? "s" : ""));
    }
}