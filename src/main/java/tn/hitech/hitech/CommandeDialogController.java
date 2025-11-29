package tn.hitech.hitech;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.hitech.Database.ArticleRepo;
import tn.hitech.Database.CommandeRepo;
import tn.hitech.Database.LigneCmdRepo;
import tn.hitech.Models.Article;
import tn.hitech.Models.Client;
import tn.hitech.Models.Commande;
import tn.hitech.Models.LigneCmd;

import java.text.DecimalFormat;
import java.time.LocalDate;

public class CommandeDialogController {

    @FXML
    private DatePicker dateCommande;
    @FXML
    private DatePicker dateLivraison;
    @FXML
    private TextField txtAdresseLivraison;

    @FXML
    private ComboBox<Article> comboArticle;
    @FXML
    private TextField txtPrixUnitaire;
    @FXML
    private Spinner<Integer> spinnerQuantite;
    @FXML
    private TextField txtTotalLigne;
    @FXML
    private Button btnAddArticle;

    @FXML
    private TableView<LigneCmd> tableArticles;
    @FXML
    private TableColumn<LigneCmd, String> colArticleNom;
    @FXML
    private TableColumn<LigneCmd, String> colArticleReference;
    @FXML
    private TableColumn<LigneCmd, Double> colPrixUnitaire;
    @FXML
    private TableColumn<LigneCmd, Integer> colQuantite;
    @FXML
    private TableColumn<LigneCmd, Double> colTotal;
    @FXML
    private TableColumn<LigneCmd, Void> colActions;

    @FXML
    private Label lblNbArticles;
    @FXML
    private Label lblMontantTotal;

    @FXML
    private Button btnAnnuler;
    @FXML
    private Button btnEnregistrer;

    private ObservableList<LigneCmd> lignesCommande;
    private ObservableList<Article> articlesList;
    private Client currentClient;
    private Commande commandeResult;
    private boolean isConfirmed = false;
    private ArticleRepo articleRepo = new ArticleRepo();
    private CommandeRepo commandeRepo = new CommandeRepo();
    private LigneCmdRepo ligneCmdRepo = new LigneCmdRepo();

    private final DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");

    @FXML
    public void initialize() {
        lignesCommande = FXCollections.observableArrayList();
        articlesList = FXCollections.observableArrayList();
        commandeResult = new Commande();

        setupArticleComboBox();
        setupSpinner();
        setupTableColumns();
        setupEventHandlers();

        // Set default date to today
        dateCommande.setValue(LocalDate.now());

        updateSummary();
    }

    /**
     * Set the client for this order
     */
    public void setClient(Client client) {
        this.currentClient = client;
        // Pre-fill delivery address with client's address
        if (client != null && client.getAdresse() != null) {
            txtAdresseLivraison.setText(client.getAdresse());
        }
    }

    private void setupArticleComboBox() {
        // Load articles from database
        // Assuming you have an ArticleRepo
        // articlesList.setAll(articleRepo.findAll());

        // Sample data for demonstration
        articlesList.setAll(articleRepo.findAll());

        comboArticle.setItems(articlesList);

        // Display article name in combo box
        comboArticle.setCellFactory(lv -> new ListCell<Article>() {
            @Override
            protected void updateItem(Article article, boolean empty) {
                super.updateItem(article, empty);
                setText(empty || article == null ? null : article.getDesignation() + " (" + article.getRefArticle() + ")");
            }
        });

        comboArticle.setButtonCell(new ListCell<Article>() {
            @Override
            protected void updateItem(Article article, boolean empty) {
                super.updateItem(article, empty);
                setText(empty || article == null ? null : article.getDesignation() + " (" + article.getRefArticle() + ")");
            }
        });
    }

    private void setupSpinner() {
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1);
        spinnerQuantite.setValueFactory(valueFactory);
    }

    private void setupTableColumns() {
        // Article Name
        colArticleNom.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getArticle().getDesignation())
        );

        // Reference
        colArticleReference.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getArticle().getRefArticle() + "")
        );

        // Prix Unitaire
        colPrixUnitaire.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(
                        cellData.getValue().getArticle().getPrixHt()
                ).asObject()
        );


        colPrixUnitaire.setCellFactory(column -> new TableCell<LigneCmd, Double>() {
            @Override
            protected void updateItem(Double prix, boolean empty) {
                super.updateItem(prix, empty);
                if (empty || prix == null) {
                    setText(null);
                } else {
                    setText(moneyFormat.format(prix) + " DT");
                    setStyle("-fx-alignment: CENTER-RIGHT;");
                }
            }
        });

        // Quantité
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("qte"));
        colQuantite.setStyle("-fx-alignment: CENTER;");

        // Total
        colTotal.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        cellData.getValue().getTotalTtcLigne()
                )
        );
        colTotal.setCellFactory(column -> new TableCell<LigneCmd, Double>() {
            @Override
            protected void updateItem(Double total, boolean empty) {
                super.updateItem(total, empty);
                if (empty || total == null) {
                    setText(null);
                } else {
                    setText(moneyFormat.format(total) + " DT");
                    setStyle("-fx-alignment: CENTER-RIGHT; -fx-font-weight: bold;");
                }
            }
        });

        // Actions (Delete button)
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnDelete = new Button("Supprimer");

            {
                btnDelete.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 11px; -fx-cursor: hand;");
                btnDelete.setOnAction(event -> {
                    LigneCmd ligne = getTableView().getItems().get(getIndex());
                    handleDeleteLigne(ligne);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnDelete);
            }
        });

        tableArticles.setItems(lignesCommande);
    }

    private void setupEventHandlers() {
        // Article selection - update price
        comboArticle.setOnAction(event -> {
            Article selectedArticle = comboArticle.getValue();
            if (selectedArticle != null) {
                txtPrixUnitaire.setText(moneyFormat.format(selectedArticle.getPrixHt()));
                updateLineTotalCalculation();
            }
        });

        // Quantity change - update line total
        spinnerQuantite.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateLineTotalCalculation();
        });

        // Add article button
        btnAddArticle.setOnAction(event -> handleAddArticle());

        // Save button
        btnEnregistrer.setOnAction(event -> handleSave());

        // Cancel button
        btnAnnuler.setOnAction(event -> handleCancel());
    }

    private void updateLineTotalCalculation() {
        Article selectedArticle = comboArticle.getValue();
        if (selectedArticle != null) {
            int quantite = spinnerQuantite.getValue();
            double total = selectedArticle.getPrixHt() * (1 - (double) selectedArticle.getPromo() / 100) * quantite;
            txtTotalLigne.setText(moneyFormat.format(total) + " DT");
        }
    }

    private void handleAddArticle() {
        Article selectedArticle = comboArticle.getValue();

        if (selectedArticle == null) {
            showWarning("Article requis", "Veuillez sélectionner un article.");
            return;
        }

        int quantite = spinnerQuantite.getValue();

        // Check if article already exists in the list
        for (LigneCmd ligne : lignesCommande) {
            if (ligne.getArticle().getRefArticle() == selectedArticle.getRefArticle()) {
                // Update quantity instead of adding duplicate
                ligne.setQte(ligne.getQte() + quantite);
                tableArticles.refresh();
                updateSummary();
                clearArticleForm();
                return;
            }
        }

        // Create new ligne commande
        LigneCmd ligne = new LigneCmd(1, quantite, selectedArticle, commandeResult);
        ligne.setArticle(selectedArticle);
        ligne.setQte(quantite);

        lignesCommande.add(ligne);
        updateSummary();
        clearArticleForm();
    }

    private void handleDeleteLigne(LigneCmd ligne) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation");
        confirmAlert.setHeaderText("Supprimer l'article");
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet article de la commande ?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            lignesCommande.remove(ligne);
            updateSummary();
        }
    }

    private void clearArticleForm() {
        comboArticle.setValue(null);
        txtPrixUnitaire.clear();
        spinnerQuantite.getValueFactory().setValue(1);
        txtTotalLigne.clear();
    }

    private void updateSummary() {
        int nbArticles = lignesCommande.stream()
                .mapToInt(LigneCmd::getQte)
                .sum();

        double montantTotal = lignesCommande.stream()
                .mapToDouble(ligne -> ligne.getTotalTtcLigne())
                .sum();

        lblNbArticles.setText(String.valueOf(nbArticles));
        lblMontantTotal.setText(moneyFormat.format(montantTotal) + " DT");
    }

    private void handleSave() {
        // Validate required fields
        if (dateCommande.getValue() == null) {
            showWarning("Champ requis", "Veuillez sélectionner une date de commande.");
            return;
        }

        if (txtAdresseLivraison.getText().trim().isEmpty()) {
            showWarning("Champ requis", "Veuillez saisir une adresse de livraison.");
            return;
        }

        if (lignesCommande.isEmpty()) {
            showWarning("Articles requis", "Veuillez ajouter au moins un article à la commande.");
            return;
        }

        // Calculate total amount
        double montantTotal = lignesCommande.stream()
                .mapToDouble(ligne -> ligne.getTotalTtcLigne())
                .sum();

        // Create Commande object
        commandeResult.setClient(currentClient);
        commandeResult.setDateCde(String.valueOf(dateCommande.getValue()));
        commandeResult.setDateLiv(String.valueOf(dateLivraison.getValue()));
        commandeResult.setLigneCmds(FXCollections.observableArrayList(lignesCommande));
        commandeResult.setMoyenPayement(Commande.PayMethode.CARTE);
        commandeRepo.insert(commandeResult);
        commandeResult.getLigneCmds().forEach(ligneCmdRepo::insert);

        isConfirmed = true;
        closeDialog();
    }

    private void handleCancel() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation");
        confirmAlert.setHeaderText("Annuler la commande");
        confirmAlert.setContentText("Êtes-vous sûr de vouloir annuler ? Toutes les modifications seront perdues.");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            isConfirmed = false;
            commandeResult = null;
            closeDialog();
        }
    }

    private void closeDialog() {
        Stage stage = (Stage) btnEnregistrer.getScene().getWindow();
        stage.close();
    }

    private void showWarning(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Getters for the result
    public Commande getCommandeResult() {
        return commandeResult;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }
}
