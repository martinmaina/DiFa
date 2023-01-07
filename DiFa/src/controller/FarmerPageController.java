package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import static database.DatabaseFunctions.login;
import functions.functions;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class FarmerPageController implements Initializable {

    ObservableList<Products> listProducts = FXCollections.observableArrayList();
    ObservableList<Orders> listOrders = FXCollections.observableArrayList();
    @FXML
    private Label userNameLabel;
    @FXML
    private StackPane motherPane;
    @FXML
    private TableColumn<Products, String> productNameCol;
    @FXML
    private TableColumn<Products, String> priceCol;
    @FXML
    private TableColumn<Products, String> quantityAvailableCol;
    @FXML
    private TableColumn<Products, String> purchasedCol;
    @FXML
    private VBox farmersHBox;
    @FXML
    private TableView<Products> productsTable;
    @FXML
    private TableView<Orders> ordersTable;
    @FXML
    private TableColumn<Orders, String> customerName;
    @FXML
    private TableColumn<Orders, String> productNameOrderCol;
    @FXML
    private TableColumn<Orders, String> quantityOrderdCol;
    @FXML
    private TableColumn<Orders, String> totalCostCol;
    @FXML
    private TableColumn<Orders, String> statusCol;

    int farmerid;
    private Pane tables;
    @FXML
    private HBox searchBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        farmerid = Integer.parseInt(database.DatabaseFunctions.checkLoggedIn());
        System.out.println(farmerid);
        initTableProducts();
        initTableOrders();
        loadDataProducts();
        loadDataOrders();
        farmersHBox.getChildren().removeAll(searchBox, productsTable, ordersTable);

        ordersTable.setRowFactory((TableView<Orders> tableView1) -> {
            final TableRow<Orders> row = new TableRow<>();
            final ContextMenu contextMenu = new ContextMenu();

            final MenuItem updateMenuItem = new MenuItem("Update Order");
            updateMenuItem.setOnAction((ActionEvent event) -> {
                tableView1.getSelectionModel().getSelectedItem();
                editOrder("" + tableView1.getSelectionModel().getSelectedItem().getId());
            });

            contextMenu.getItems().add(updateMenuItem);

            //Lets make sure a row is not empty
            row.contextMenuProperty().bind(Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu));
            return row;
        });

        userNameLabel.setText(database.DatabaseFunctions.getFamerNameById(database.DatabaseFunctions.checkLoggedIn()));

//        int i = 0;
//        Set<Node> cells = ordersTable.lookupAll(".table-row");
//        for (Node n : cells) {
//            if (n instanceof TableRow) {
//                TableRow row = (TableRow) n;
//                if (ordersTable.getItems().get(i).status.equals("DELIVERED")) {
//                    row.getStyleClass().add("delivered");
//                    System.out.println(ordersTable.getItems().get(i));
//
//                } else if (ordersTable.getItems().get(i).status.equals("PROCESSING")) {
//                    row.getStyleClass().add("processing");
//
//                } else if (ordersTable.getItems().get(i).status.equals("REJECTED")) {
//                    row.getStyleClass().add("rejected");
//                } else if (ordersTable.getItems().get(i).status.equals("ENROUTE")) {
//                    row.getStyleClass().add("enroute");
//                }
//                i++;
//                if (i == ordersTable.getItems().size()) {
//                    break;
//                }
//            }
//        }
    }

    private void initTableProducts() {
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityAvailableCol.setCellValueFactory(new PropertyValueFactory<>("available"));
        purchasedCol.setCellValueFactory(new PropertyValueFactory<>("purchased"));
    }

    private void initTableOrders() {
        customerName.setCellValueFactory(new PropertyValueFactory<>("customername"));
        productNameOrderCol.setCellValueFactory(new PropertyValueFactory<>("productname"));
        quantityOrderdCol.setCellValueFactory(new PropertyValueFactory<>("quantitypurchased"));
        totalCostCol.setCellValueFactory(new PropertyValueFactory<>("totalcost"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadDataProducts() {
        listProducts.clear();

        String query = "SELECT * FROM products WHERE farmerid='" + farmerid + "'";
        ResultSet rs = database.DatabaseFunctions.execQuery(query);
        try {
            while (rs.next()) {
                listProducts.add(new Products(
                        rs.getString(2),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6)
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        productsTable.getItems().setAll(listProducts);
    }

    private void loadDataOrders() {
        listOrders.clear();

        String query = "SELECT * FROM orders WHERE productid IN (SELECT id FROM products where farmerid='" + farmerid + "')";
        ResultSet rs = database.DatabaseFunctions.execQuery(query);
        try {
            while (rs.next()) {
                listOrders.add(new Orders(
                        database.DatabaseFunctions.getProductNameById(rs.getString("productid")),
                        database.DatabaseFunctions.getCustomerNameById(rs.getString("customerid")),
                        rs.getString("quantitypurchased"),
                        rs.getString("totalcost"),
                        rs.getString("orderstatus"),
                        rs.getString("orderid")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        ordersTable.getItems().setAll(listOrders);
    }

    @FXML
    private void logoutAction(ActionEvent event) {
        logout();
    }

    public void logout() {
        Stage stage = (Stage) motherPane.getScene().getWindow();
        loadStage("/view/login.fxml");
        stage.close();

    }

    private void loadStage(String fxml) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
//            stage.getIcons().add(new Image("/Resources/icons/home.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Digital Farmers | Login");

            Scene scene = new Scene(root);
//            stage.setScene(new Scene(root));

            stage.initStyle(StageStyle.DECORATED.UNDECORATED);

            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void closeSystem(ActionEvent event) {
        functions.CloseSystem();
    }

    @FXML
    private void goHomeAction(ActionEvent event) throws IOException {
        farmersHBox.getChildren().removeAll(searchBox, productsTable, ordersTable);
        farmersHBox.getChildren().add(searchBox);
        farmersHBox.getChildren().add(productsTable);
    }

    @FXML
    private void userProfileAction(ActionEvent event) throws IOException {
        motherPane.getChildren().clear();
        motherPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/farmeruserpage.fxml")));

    }

    @FXML
    private void ordersAction(ActionEvent event) throws IOException {
        farmersHBox.getChildren().removeAll(searchBox, productsTable, ordersTable);
        farmersHBox.getChildren().add(searchBox);
        farmersHBox.getChildren().add(ordersTable);
    }

    @FXML
    private void searchProduct(KeyEvent event) {
        System.out.println("Searching");
    }

    @FXML
    private void addNewProduct(ActionEvent event) {
        Dialog<Pair<String, String>> newProduct = new Dialog();
        newProduct.setTitle("Add new Product | DIFA");

        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        newProduct.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(20, 150, 10, 10));

        Label productName = new Label("Name");
        JFXTextField productNameField = new JFXTextField();
        productNameField.setPromptText("Enter Product Name");

        Label userName = new Label("Username");
        JFXTextField userNameField = new JFXTextField();
        userNameField.setPromptText("Enter your username");
        Label password = new Label("Password");
        JFXPasswordField passwordField = new JFXPasswordField();
        userNameField.setPromptText("Enter your password");

        Label price = new Label("Price Per Unit");
        JFXTextField priceField = new JFXTextField();
        priceField.setPromptText("Enter Price per Unit");

        Label quantity = new Label("Quantity");
        JFXTextField quantityField = new JFXTextField();
        quantityField.setPromptText("Quantity");

        gp.add(productName, 0, 0);
        gp.add(productNameField, 1, 0);
        gp.add(price, 0, 1);
        gp.add(priceField, 1, 1);
        gp.add(quantity, 0, 2);
        gp.add(quantityField, 1, 2);
        gp.add(userName, 0, 3);
        gp.add(userNameField, 1, 3);
        gp.add(password, 0, 4);
        gp.add(passwordField, 1, 4);

        newProduct.getDialogPane().setContent(gp);

        newProduct.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                if (login(userNameField.getText(), passwordField.getText())) {
                    if (database.DatabaseFunctions.addNewProduct(productNameField.getText().toUpperCase(), priceField.getText().toUpperCase(), quantityField.getText(), userNameField.getText())) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        loadDataProducts();
                        alert.setContentText("New Product Added Successfully");
                        alert.setHeaderText("Success!");
                        alert.showAndWait();
                    } else {
                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.setContentText("Some Error Occurred");
                        alert2.setHeaderText("Error!");
                        alert2.showAndWait();
                    }
                }
            }
            return null;
        });

        Optional<Pair<String, String>> resut = newProduct.showAndWait();

        resut.ifPresent(pair -> {
            System.out.println("Later Please");
        });

    }

    private void editOrder(String string) {
        final JOptionPane optionPane = new JOptionPane(
                "Update the Order status",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION);

        Object[] options = {"REJECTED", "ENROUTE", "DELIVERED", "PROCESSING"};

        Dialog<Pair<String, String>> dialog = new Dialog();
        dialog.setTitle("Update Order | DIFA");

        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(20, 150, 10, 10));

        Label updateOrder = new Label("Update Order");
        JFXComboBox cb = new JFXComboBox();
        cb.getItems().addAll(options);

        gp.add(updateOrder, 0, 0);
        gp.add(cb, 1, 0);

        dialog.getDialogPane().setContent(gp);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                if (!cb.getSelectionModel().getSelectedItem().equals("")) {
                    String query = "UPDATE orders SET orderstatus='" + cb.getSelectionModel().getSelectedItem().toString() + "' WHERE orderid='" + string + "'";

                    if (database.DatabaseFunctions.execAction(query)) {
                        if (database.DatabaseFunctions.execAction("SELECT * FROM orders WHERE orderstatus='PROCESSING' AND orderid='" + string + "'")) {
                            if (cb.getSelectionModel().getSelectedIndex() == 1 || cb.getSelectionModel().getSelectedIndex() == 2) {
                                database.DatabaseFunctions.execAction("UPDATE collectionpoints SET commision=commision+'" + database.DatabaseFunctions.getTotalSalesByOrderId(string) * 0.1 + "' WHERE id = '" + database.DatabaseFunctions.getCollectionIdByOrder(string) + "'");
                                database.DatabaseFunctions.execAction("UPDATE products SET purchase=purchase+'" + database.DatabaseFunctions.getTotalProductsSoldByOrderId(string) + "' WHERE id='" + database.DatabaseFunctions.getProductIdByOrderId(string) + "'");
                            }
                        }
                        functions.dialogPopUp("INFORMATION", "Updated Successfully");
                        loadDataOrders();
                    } else {

                        functions.dialogPopUp("INFORMATION", "Some Error Occured\nTry again later.");
                    }
                }
            }

            return null;
        });
        Optional<Pair<String, String>> resut = dialog.showAndWait();

        resut.ifPresent(pair -> {
            System.out.println("Later Please");
        });
    }

    public static class Products {

        private final SimpleStringProperty name;
        private final SimpleStringProperty price;
        private final SimpleStringProperty available;
        private final SimpleStringProperty purchased;

        public Products(String productname, String price, String quantityavailable, String quantitypurchased) {
            this.name = new SimpleStringProperty(productname);
            this.price = new SimpleStringProperty(price);
            this.available = new SimpleStringProperty(quantityavailable);
            this.purchased = new SimpleStringProperty(quantitypurchased);
        }

        public String getName() {
            return name.get();
        }

        public String getPrice() {
            return price.get();
        }

        public String getAvailable() {
            return available.get();
        }

        public String getPurchased() {
            return purchased.get();
        }
    }

    public static class Orders {

        private final SimpleStringProperty customername;
        private final SimpleStringProperty productname;
        private final SimpleStringProperty quantitypurchased;
        private final SimpleStringProperty totalcost;
        private final SimpleStringProperty status;
        private final SimpleStringProperty id;

        public Orders(String productname, String customername, String quantitypurchased, String totalcost, String status, String id) {
            this.productname = new SimpleStringProperty(productname);
            this.customername = new SimpleStringProperty(customername);
            this.totalcost = new SimpleStringProperty(totalcost);
            this.quantitypurchased = new SimpleStringProperty(quantitypurchased);
            this.status = new SimpleStringProperty(status);
            this.id = new SimpleStringProperty(id);
        }

        public String getProductname() {
            return productname.get();
        }

        public String getCustomername() {
            return customername.get();
        }

        public String getId() {
            return id.get();
        }

        public String getQuantitypurchased() {
            return quantitypurchased.get();
        }

        public String getStatus() {
            return status.get();
        }

        public String getTotalcost() {
            return totalcost.get();
        }
    }
}
