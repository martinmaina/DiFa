package controller;

import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

public class AdminController implements Initializable {

    ObservableList<CollectionPoint> list = FXCollections.observableArrayList();
    @FXML
    private BorderPane adminMotherPane;
    @FXML
    private Label userNameLabel;
    @FXML
    private StackPane motherPane;
    @FXML
    private MenuBar menubar;
    @FXML
    private TableView<CollectionPoint> table;
    @FXML
    private TableColumn<CollectionPoint, String> nameCol;
    @FXML
    private TableColumn<CollectionPoint, String> addressCol;
    @FXML
    private TableColumn<CollectionPoint, String> commissionCol;
    @FXML
    private TableColumn<CollectionPoint, String> ordersCol;
    @FXML
    private TableColumn<CollectionPoint, String> statusCol;
    @FXML
    private ImageView logo;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTableCollectionPoint();
        loadDataCollectionPoint();
        table.setVisible(false);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
            @Override
            public void run() {
                loadDataCollectionPoint();
            }
        },
                500
        );
    }

    private void initTableCollectionPoint() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        commissionCol.setCellValueFactory(new PropertyValueFactory<>("commission"));
        ordersCol.setCellValueFactory(new PropertyValueFactory<>("orders"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadDataCollectionPoint() {
        list.clear();

        String query1 = "SELECT * FROM collectionpoints ORDER BY name ASC";

        ResultSet rs = database.DatabaseFunctions.execQuery(query1);
        try {
            while (rs.next()) {
                list.add(new CollectionPoint(
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("commision"),
                        "",
                        ""
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        table.getItems().setAll(list);
    }

    @FXML
    private void editConfigarations(ActionEvent event) {
    }

    @FXML
    private void purchasesRecord(ActionEvent event) {
    }

    @FXML
    private void customersRecord(ActionEvent event) {
    }

    @FXML
    private void farmersRecord(ActionEvent event) {
    }

    @FXML
    private void addNewCollectionPoint(ActionEvent event) {
        Dialog<Pair<String, String>> newCollectionPoint = new Dialog();
        newCollectionPoint.setTitle("Add new Collection Point | DIFA");

        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        newCollectionPoint.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(20, 150, 10, 10));

        Label collectionName = new Label("Name");
        JFXTextField collectionNameField = new JFXTextField();
        collectionNameField.setPromptText("Enter Collection point Name");

        Label address = new Label("Address");
        JFXTextField addressField = new JFXTextField();
        addressField.setPromptText("Enter Address");

        gp.add(collectionName, 0, 0);
        gp.add(collectionNameField, 0, 1);
        gp.add(address, 0, 2);
        gp.add(addressField, 0, 3);

        newCollectionPoint.getDialogPane().setContent(gp);

        newCollectionPoint.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                if (database.DatabaseFunctions.addNewCollectionPoint(collectionNameField.getText().toUpperCase(), addressField.getText().toUpperCase())) {
                    loadDataCollectionPoint();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("New Collection Point Added Successfully");
                    alert.setHeaderText("Success!");
                    alert.showAndWait();
                } else {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setContentText("Some Error Occurred");
                    alert2.setHeaderText("Error!");
                    alert2.showAndWait();
                }
            }
            return null;
        });
        Optional<Pair<String, String>> resut = newCollectionPoint.showAndWait();

        resut.ifPresent(pair -> {
            System.out.println("Later Please");
        });

    }

    @FXML
    private void collectionPoints(ActionEvent event) {
        adminMotherPane.getChildren().remove(logo);
        table.setVisible(true);
        loadDataCollectionPoint();
    }

    @FXML
    private void logoutAction(ActionEvent event) {

    }

    @FXML
    private void closeSystem(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void importFarmers(ActionEvent event) throws FileNotFoundException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File To import");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Document", "*.txt"));

        File file = fileChooser.showOpenDialog((Stage) adminMotherPane.getScene().getWindow());
        FileInputStream fi = new FileInputStream(file.getAbsolutePath());

        database.DatabaseFunctions.insertDataIntoDatabase("farmers", fi);
    }

    @FXML
    private void collectionImport(ActionEvent event) throws FileNotFoundException {
                FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File To import");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Document", "*.txt"));

        File file = fileChooser.showOpenDialog((Stage) adminMotherPane.getScene().getWindow());
        FileInputStream fi = new FileInputStream(file.getAbsolutePath());

        database.DatabaseFunctions.insertDataIntoDatabase("collectionpoint", fi);
    }

    @FXML
    private void customerImport(ActionEvent event) throws FileNotFoundException {
                FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File To import");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Document", "*.txt"));

        File file = fileChooser.showOpenDialog((Stage) adminMotherPane.getScene().getWindow());
        FileInputStream fi = new FileInputStream(file.getAbsolutePath());

        database.DatabaseFunctions.insertDataIntoDatabase("customers", fi);
    }

    @FXML
    private void productsImport(ActionEvent event) throws FileNotFoundException {
                FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File To import");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Document", "*.txt"));

        File file = fileChooser.showOpenDialog((Stage) adminMotherPane.getScene().getWindow());
        FileInputStream fi = new FileInputStream(file.getAbsolutePath());

        database.DatabaseFunctions.insertDataIntoDatabase("products", fi);
    }

    @FXML
    private void farmersExport(ActionEvent event) {
    }

    @FXML
    private void collectionPointsExport(ActionEvent event) {
    }

    @FXML
    private void customerExport(ActionEvent event) {
    }

    @FXML
    private void exportProducts(ActionEvent event) {
    }

    public static class CollectionPoint {

        private final SimpleStringProperty name;
        private final SimpleStringProperty address;
        private final SimpleStringProperty commission;
        private final SimpleStringProperty orders;
        private final SimpleStringProperty status;

        public String getName() {
            return name.get();
        }

        public String getAddress() {
            return address.get();
        }

        public String getCommission() {
            return commission.get();
        }

        public String getOrders() {
            return orders.get();
        }

        public String getStatus() {
            return status.get();
        }

        public CollectionPoint(String name, String address, String commission, String orders, String status) {
            this.name = new SimpleStringProperty(name);
            this.address = new SimpleStringProperty(address);
            this.commission = new SimpleStringProperty(commission);
            this.orders = new SimpleStringProperty(orders);
            this.status = new SimpleStringProperty(status);
        }
    }

}
