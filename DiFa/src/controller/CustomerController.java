package controller;

import controller.FarmerPageController.Orders;
import controller.FarmerPageController.Products;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CustomerController implements Initializable {

    ObservableList<FarmerPageController.Products> listProducts = FXCollections.observableArrayList();
    ObservableList<Orders> listOrders = FXCollections.observableArrayList();
    @FXML
    private Label userNameLabel;
    private FlowPane flowPane;
    @FXML
    private TableColumn<Products, String> productNameCol;
    @FXML
    private TableColumn<Products, String> farmerNameCol;
    @FXML
    private StackPane motherPane;
    @FXML
    private TableColumn<Products, String> pricePerUnitCol;
    @FXML
    private TableColumn<Products, String> quantityCol;
    @FXML
    private TableView<Products> produtcTable;
    @FXML
    private VBox tableHBox;
    @FXML
    private TableView<Orders> ordersTable;
    @FXML
    private TableColumn<Orders, String> quantityOrderedCol;
    @FXML
    private TableColumn<Orders, String> totalCostCol;
    int customerid;
    @FXML
    private TableColumn<Orders, String> customerNameOrdersCol;
    @FXML
    private TableColumn<Orders, String> productNameOrdersCol;
    @FXML
    private TableColumn<Orders, String> orderStatusCol;
    @FXML
    private HBox searchBox;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerid = Integer.parseInt(database.DatabaseFunctions.checkLoggedIn());
        initTableProducts();
        initTableOrders();
        loadDataProducts();
        tableHBox.getChildren().removeAll(produtcTable, ordersTable);
        loadDataOrders();
        userNameLabel.setText(database.DatabaseFunctions.getCustomerNameById(database.DatabaseFunctions.checkLoggedIn()));
    }

    private void initTableProducts() {
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        pricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("available"));
        farmerNameCol.setCellValueFactory(new PropertyValueFactory<>("purchased"));
    }

    private void loadDataProducts() {
        listProducts.clear();
        String query = "SELECT * FROM products";
        ResultSet rs = database.DatabaseFunctions.execQuery(query);
        try {
            while (rs.next()) {
                listProducts.add(new Products(
                        rs.getString("productname"),
                        rs.getString("price"),
                        rs.getString("quantity"),
                        database.DatabaseFunctions.getFarmerNameByProductId(rs.getString("farmerid"))
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        produtcTable.getItems().setAll(listProducts);
    }

    private void initTableOrders() {
        customerNameOrdersCol.setCellValueFactory(new PropertyValueFactory<>("customername"));
        productNameOrdersCol.setCellValueFactory(new PropertyValueFactory<>("productname"));
        quantityOrderedCol.setCellValueFactory(new PropertyValueFactory<>("quantitypurchased"));
        totalCostCol.setCellValueFactory(new PropertyValueFactory<>("totalcost"));
        orderStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadDataOrders() {
        listOrders.clear();

        String query = "SELECT * FROM orders WHERE customerid='" + customerid + "'";
        ResultSet rs = database.DatabaseFunctions.execQuery(query);
        try {
            while (rs.next()) {
                listOrders.add(new Orders(
                        database.DatabaseFunctions.getProductNameById(rs.getString("productid")), database.DatabaseFunctions.getCustomerNameById(rs.getString("customerid")),
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
        loadStage1("/view/login.fxml");
        stage.close();

    }

    private void loadStage1(String fxml) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
//            stage.getIcons().add(new Image("/Resources/icons/home.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Digital Farmers | Login");
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.DECORATED.UNDECORATED);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void goHomeAction(ActionEvent event) throws IOException {
        loadDataProducts();
        motherPane.getChildren().clear();

        tableHBox.getChildren().removeAll(searchBox, produtcTable, ordersTable);
        tableHBox.getChildren().add(searchBox);
        tableHBox.getChildren().add(produtcTable);

        motherPane.getChildren().add(tableHBox);
    }

    @FXML
    private void userProfileAction(ActionEvent event) throws IOException {
        System.out.println("User Profile");
    }

    @FXML
    private void userOrdersAction(ActionEvent event) throws IOException {
        loadDataOrders();
        motherPane.getChildren().clear();

        tableHBox.getChildren().removeAll(searchBox, produtcTable, ordersTable);
        tableHBox.getChildren().add(searchBox);
        tableHBox.getChildren().add(ordersTable);
        motherPane.getChildren().add(tableHBox);
    }

    private void searchProduct(KeyEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Its working");
        alert.showAndWait();
    }

    @FXML
    private void closeSystem(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void makeNewOrder(ActionEvent event) {
        motherPane.getChildren().clear();
        try {
            motherPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/neworder.fxml")));
        } catch (IOException e) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
