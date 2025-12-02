package tn.hitech.hitech;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.hitech.Database.ArticleRepo;
import tn.hitech.Database.CommandeRepo;
import tn.hitech.Database.LigneCmdRepo;
import tn.hitech.Models.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class CommandeTableController {

    private final DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");
    @FXML
    private Label lblClientInfo;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnView;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnRefresh;
    @FXML
    private RadioButton radioAll;
    @FXML
    private RadioButton radioEnCours;
    @FXML
    private RadioButton radioLivree;
    @FXML
    private RadioButton radioAnnulee;
    @FXML
    private ToggleGroup filterGroup;
    @FXML
    private TableView<Commande> tableCommandes;
    @FXML
    private TableColumn<Commande, String> colNumero;
    @FXML
    private TableColumn<Commande, String> colDate;
    @FXML
    private TableColumn<Commande, String> colStatut;
    @FXML
    private TableColumn<Commande, Double> colMontantTotal;
    @FXML
    private TableColumn<Commande, Integer> colNbProduits;
    @FXML
    private TableColumn<Commande, String> colDateLivraison;
    @FXML
    private Label lblStatus;
    private ObservableList<Commande> commandeList;
    private ObservableList<Commande> filteredList;
    private Client currentClient;
    private CommandeRepo commandeRepo;
    private LigneCmdRepo ligneCmdRepo;
    private ArticleRepo  articleRepo;

    @FXML
    public void initialize() {
        commandeRepo = new CommandeRepo();
        ligneCmdRepo = new LigneCmdRepo();
        articleRepo = new ArticleRepo();
        commandeList = FXCollections.observableArrayList();
        filteredList = FXCollections.observableArrayList();

        setupTableColumns();
        setupEventHandlers();
    }

    public void setClient(Client client) {
        this.currentClient = client;
        updateClientInfo();
        loadCommandes();
    }

    private void updateClientInfo() {
        if (currentClient != null) {
            String clientName = "";
            if (currentClient instanceof ClientPP pp) {
                clientName = pp.getPrenom() + " " + pp.getNom();
            } else if (currentClient instanceof ClientPM pm) {
                clientName = pm.getRaisonSociale();
            }
            lblClientInfo.setText(clientName + " (" + currentClient.getEmail() + ")");
        }
    }

    private void setupTableColumns() {
        // Numéro de commande
        colNumero.setCellValueFactory(cellData ->
                new SimpleStringProperty("CMD-" + String.format("%04d", cellData.getValue().getId()))
        );

        // Date de commande
        colDate.setCellValueFactory(cellData -> {
            if (cellData.getValue().getDateCde() != null) {
                return new SimpleStringProperty(
                        cellData.getValue().getDateCde().toString()
                );
            }
            return new SimpleStringProperty("");
        });

        // Statut
        colStatut.setCellValueFactory(new PropertyValueFactory<>("etatCde"));

        // Montant total
        colMontantTotal.setCellValueFactory(new PropertyValueFactory<>("commandeTotal"));
        colMontantTotal.setCellFactory(column -> new TableCell<Commande, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                if (empty || montant == null) {
                    setText(null);
                } else {
                    setText(moneyFormat.format(montant) + " DT");
                    setStyle("-fx-alignment: CENTER-RIGHT; -fx-font-weight: bold;");
                }
            }
        });

        // Nombre de produits
        colNbProduits.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        cellData.getValue().getLigneCmds() != null ?
                                cellData.getValue().getLigneCmds().size() : 0
                )
        );
        colNbProduits.setStyle("-fx-alignment: CENTER;");

        // Date de livraison
        colDateLivraison.setCellValueFactory(cellData -> {
            if (cellData.getValue().getDateLiv() != null) {
                return new SimpleStringProperty(cellData.getValue().getDateLiv().toString());
            }
            return new SimpleStringProperty("-");
        });

        // Center align certain columns
        colNumero.setStyle("-fx-alignment: CENTER;");
        colDate.setStyle("-fx-alignment: CENTER;");
        colStatut.setStyle("-fx-alignment: CENTER;");
        colDateLivraison.setStyle("-fx-alignment: CENTER;");
    }

    private void setupEventHandlers() {
        // Add button
        btnAdd.setOnAction(event -> handleAddCommande());

        // View button
        btnView.setOnAction(event -> handleViewCommande());

        // Delete button
        btnDelete.setOnAction(event -> handleDeleteCommande());

        // Refresh button
        btnRefresh.setOnAction(event -> loadCommandes());

        // Filter radio buttons
        filterGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> applyFilter());

        // Double-click to view details
        tableCommandes.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tableCommandes.getSelectionModel().getSelectedItem() != null) {
                handleViewCommande();
            }
        });
    }

    private void loadCommandes() {
        if (currentClient == null) {
            return;
        }
        commandeList.setAll(commandeRepo.findByClientId(currentClient.getId()));
        commandeList.forEach(commande -> {
            commande.getLigneCmds().addAll(ligneCmdRepo.findByCommandeId(commande.getId()));
        });

        applyFilter();
        updateStatus();
    }

    private void applyFilter() {
        filteredList.clear();

        if (radioAll.isSelected()) {
            filteredList.addAll(commandeList);
        } else if (radioEnCours.isSelected()) {
            commandeList.stream()
                    .filter(cmd -> "cree".equalsIgnoreCase(cmd.getEtatCde().name()))
                    .forEach(filteredList::add);
        } else if (radioLivree.isSelected()) {
            commandeList.stream()
                    .filter(cmd -> "livree".equalsIgnoreCase(cmd.getEtatCde().name()))
                    .forEach(filteredList::add);
        } else if (radioAnnulee.isSelected()) {
            commandeList.stream()
                    .filter(cmd -> "annulee".equalsIgnoreCase(cmd.getEtatCde().name()))
                    .forEach(filteredList::add);
        }

        tableCommandes.setItems(filteredList);
        updateStatus();
    }

    private void handleAddCommande() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addCommandeDialog.fxml"));
            Parent root = loader.load();

            CommandeDialogController controller = loader.getController();
            controller.setClient(currentClient); // Passer le client connecté

            Stage stage = new Stage();
            stage.setTitle("ajouter Commande");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void handleViewCommande() {
        Commande selectedCommande = tableCommandes.getSelectionModel().getSelectedItem();

        if (selectedCommande == null) {
            showWarning("Aucune sélection", "Veuillez sélectionner une commande à consulter.");
            return;
        }

        // Open dialog to view order details
        showInfo("Détails de la Commande", "Affichage des détails de la commande #" + selectedCommande.getId());
    }

    private void handleDeleteCommande() {
        Commande selectedCommande = tableCommandes.getSelectionModel().getSelectedItem();

        if (selectedCommande == null) {
            showWarning("Aucune sélection", "Veuillez sélectionner une commande à annuler.");
            return;
        }

        // Confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation");
        confirmAlert.setHeaderText("Annuler la commande");
        confirmAlert.setContentText("Êtes-vous sûr de vouloir annuler cette commande ?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            commandeRepo.updateStatus(selectedCommande.getId(), Commande.Etat.ANNULEE.name());
            selectedCommande.getLigneCmds().forEach(ligneCmd -> {
                articleRepo.updateStock(ligneCmd.getArticle().getRefArticle(),ligneCmd.getArticle().getQteStock()+ligneCmd.getQte());
            });
            applyFilter();
        }
    }

    private void updateStatus() {
        int total = tableCommandes.getItems().size();
        lblStatus.setText("Total: " + total + " commande" + (total > 1 ? "s" : ""));
    }

    private void showWarning(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}