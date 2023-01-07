package controller;

import com.jfoenix.controls.JFXComboBox;
import functions.functions;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class NeworderController implements Initializable {

    ObservableList<Cart> listTable = FXCollections.observableArrayList();
    ObservableList<Checkout> list = FXCollections.observableArrayList();
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab cartTab;
    @FXML
    private Button cartCheckout;
    @FXML
    private Button abortCart;
    @FXML
    private TableView<Cart> cartTableView;
    @FXML
    private TableColumn<Cart, String> cartNameCol;
    @FXML
    private TableColumn<Cart, String> cartFarmerCol;
    @FXML
    private TableColumn<Cart, String> cartSellingPriceCol;
    @FXML
    private TableColumn<Cart, String> cartQuantityCol;
    @FXML
    private TableColumn<Cart, String> cartProductIdCol;
    @FXML
    private TextField productNameField;
    @FXML
    private TextField sellingPriceField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField productIdField;
    @FXML
    private Button addToCartbtn;
    @FXML
    private Button resetCartBtn;
    @FXML
    private Tab checkoutTab;
    @FXML
    private HBox hboxCheckout;
    @FXML
    private Button sellCheckoutbtn;
    @FXML
    private Button addMoreToCart;
    @FXML
    private Button abortSalebtn;
    @FXML
    private TableView<Checkout> checkoutTableView;
    @FXML
    private TableColumn<Checkout, String> checkoutProductNameCol;
    @FXML
    private TableColumn<Checkout, String> checkoutPriceCol;
    @FXML
    private TableColumn<Checkout, String> checkoutQuantityCol;
    @FXML
    private TableColumn<Checkout, String> checkoutTotalSalesCol;
    @FXML
    private TextField farmerNameField;
    private JFXComboBox<String> selectCollectionPoint;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initTableCart();
        loadData();
        initTableCheckout();

        cartTableView.setOnMouseClicked((MouseEvent event) -> {
            if (cartTableView.getSelectionModel().getSelectedItem() != null) {
                Cart tableRowSelected = cartTableView.getSelectionModel().getSelectedItem();
                productNameField.setText(tableRowSelected.getProductname());
                farmerNameField.setText(tableRowSelected.getFarmername());
                sellingPriceField.setText(tableRowSelected.getSellingprice());
                productIdField.setText(tableRowSelected.getProductid());
            }
        });
        checkoutTab.setDisable(true);

    }

    private void loadData() {
        listTable.clear();
        String query = "SELECT * FROM products ORDER BY productname ASC";
        try {
            ResultSet rs = database.DatabaseFunctions.execQuery(query);
            while (rs.next()) {
                listTable.add(new Cart(
                        rs.getString("productname"),
                        database.DatabaseFunctions.getFarmerNameByProductId(rs.getString("farmerid")),
                        rs.getString("price"),
                        rs.getString("quantity"),
                        rs.getString("id")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        cartTableView.getItems().setAll(listTable);
    }

    private void initTableCart() {
        cartNameCol.setCellValueFactory(new PropertyValueFactory<>("productname"));
        cartFarmerCol.setCellValueFactory(new PropertyValueFactory<>("farmername"));
        cartSellingPriceCol.setCellValueFactory(new PropertyValueFactory<>("sellingprice"));
        cartQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        cartProductIdCol.setCellValueFactory(new PropertyValueFactory<>("productid"));

    }

    private void initTableCheckout() {
        checkoutProductNameCol.setCellValueFactory(new PropertyValueFactory<>("productname"));
        checkoutPriceCol.setCellValueFactory(new PropertyValueFactory<>("sellingprice"));
        checkoutQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        checkoutTotalSalesCol.setCellValueFactory(new PropertyValueFactory<>("totalsales"));
    }

    @FXML
    private void goToCheckOutAction(ActionEvent event) {
        checkoutTableView.getItems().setAll(list);
        tabPane.getSelectionModel().select(1);
        checkoutTab.setDisable(false);
    }

    @FXML
    private void abortCartAction(ActionEvent event) {
        list.clear();
        productNameField.setText(null);
        farmerNameField.setText(null);
        sellingPriceField.setText(null);
        quantityField.setText("1");
    }

    @FXML
    private void addToCartAction(ActionEvent event) {
        String values[] = {
            productNameField.getText(),
            farmerNameField.getText(),
            sellingPriceField.getText(),
            quantityField.getText()
        };

        // Check for empty values. Make sure that there is no empty field
        boolean empty = false;
        for (String s : values) {
            if (s.isEmpty()) {
                empty = true;
            }
        }
        if (!empty) {
            addToCart();
            functions.dialogPopUp("Added to cart", "INFORMATION");
            productNameField.setText(null);
            farmerNameField.setText(null);
            sellingPriceField.setText(null);
            quantityField.setText("1");
        } else {
            functions.dialogPopUp("Some fields are empty\nPlease select a product to purchase.", "ERROR");
        }
    }

    private void addToCart() {
        int totalsales = Integer.parseInt(quantityField.getText()) * Integer.parseInt(sellingPriceField.getText());
        list.add(new Checkout(
                productNameField.getText(),
                sellingPriceField.getText(),
                quantityField.getText(),
                totalsales,
                Integer.parseInt(productIdField.getText())
        ));
    }

    @FXML
    private void resetCartAction(ActionEvent event) {
        productNameField.setText(null);
        farmerNameField.setText(null);
        sellingPriceField.setText(null);
        quantityField.setText("1");
    }

    @FXML
    private void sellCheckoutAction(ActionEvent event) {
        makeNewOrder();
    }

    private void makeNewOrder() {
        int total = 0;
        boolean result = false;
        String products = " ";
        String date = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd").format(java.time.LocalDateTime.now());
        total = checkoutTableView.getItems().stream().map((item) -> item.getTotalsales()).reduce(total, Integer::sum);
        // Get a list of Products as array
        for (Checkout item : checkoutTableView.getItems()) {
            products = products + "\n" + item.getProductname() + " :: " + item.getTotalsales();
        }
        ObservableList<String> collectionPoints
                = FXCollections.observableArrayList();

        try {
            ResultSet rs = database.DatabaseFunctions.execQuery("SELECT name  FROM collectionpoints ORDER BY name ASC");
            while (rs.next()) {
                collectionPoints.add(rs.getString(1));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        final JFXComboBox comboBox = new JFXComboBox(collectionPoints);

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Complete Order");

        ButtonType okButtonType = new ButtonType("Buy", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(20, 150, 20, 20));

        Label productsLabel = new Label("Products");
        Text _products = new Text(products);
        Label totalCost = new Label("Total ");
        Label _totalCost = new Label("" + total);
        Label collectionPoint = new Label("Collection Point");

        gp.add(productsLabel, 0, 0);
        gp.add(_products, 1, 0);
        gp.add(totalCost, 0, 1);
        gp.add(_totalCost, 1, 1);
        gp.add(collectionPoint, 0, 2);
        gp.add(comboBox, 1, 2);

        dialog.getDialogPane().setContent(gp);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                if (addToOrders(_products.getText(), _totalCost.getText(), comboBox.getSelectionModel().getSelectedItem().toString())) {
                    functions.dialogPopUp("", "Added Successfully");
                    loadData();
                } else {
                    functions.dialogPopUp("ERROR", "Some Error Occured. \nSorry");
                }
            }
            return null;
        });
        Optional<Pair<String, String>> result2 = dialog.showAndWait();
        list.clear();
        loadData();

    }

    public boolean addToOrders(String products, String totalSales, String collectionPoint) {
        boolean result = false;

        String date = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd").format(java.time.LocalDateTime.now());
        for (Checkout item : checkoutTableView.getItems()) {
            if (database.DatabaseFunctions.execAction("INSERT INTO orders VALUES('" + database.DatabaseFunctions.checkLoggedIn() + "','" + item.getProductid() + "','" + database.DatabaseFunctions.getCollectionIdByName(collectionPoint) + "','PROCESSING','" + Integer.parseInt(item.getQuantity()) + "','" + item.getTotalsales() + "', '" + date + "', NULL);")) {
                if (database.DatabaseFunctions.execAction("UPDATE products SET quantity=quantity-'" + item.getQuantity() + "' WHERE id='" + item.getProductid() + "'")) {
                    result = true;
                    tabPane.getSelectionModel().select(cartTab);
                    list.clear();
                    checkoutTab.setDisable(true);

                }
            } else {
                functions.dialogPopUp("Could not process\nSome  error occured. \nPlease try again later.", "WARNING");
            }
        }
        return result;
    }

    @FXML
    private void addMoreToCartAction(ActionEvent event) {
        tabPane.getSelectionModel().select(0);
    }

    @FXML
    private void abortCheckoutAction(ActionEvent event) {
        tabPane.getSelectionModel().select(0);
        checkoutTab.setDisable(true);
        list.clear();
        productNameField.setText(null);
        farmerNameField.setText(null);
        sellingPriceField.setText(null);
        quantityField.setText("1");
    }

    public static class CollectionPoints {

        private final String[] values;

        public CollectionPoints(String[] values) {
            this.values = values;
        }

        public String[] getValues() {
            return values;
        }
    }

    public static class Cart {

        private final SimpleStringProperty productname;
        private final SimpleStringProperty farmername;
        private final SimpleStringProperty sellingprice;
        private final SimpleStringProperty quantity;
        private final SimpleStringProperty productid;

        public Cart(String productname, String productcode, String sellingprice, String quantity, String productid) {
            this.productname = new SimpleStringProperty(productname);
            this.farmername = new SimpleStringProperty(productcode);
            this.sellingprice = new SimpleStringProperty(sellingprice);
            this.quantity = new SimpleStringProperty(quantity);
            this.productid = new SimpleStringProperty(productid);
        }

        public String getProductname() {
            return productname.get();
        }

        public String getFarmername() {
            return farmername.get();
        }

        public String getSellingprice() {
            return sellingprice.get();
        }

        public String getQuantity() {
            return quantity.get();
        }

        public String getProductid() {
            return productid.get();
        }

    }

    public static class Checkout {

        private final SimpleStringProperty productname;
        private final SimpleStringProperty sellingprice;
        private final SimpleStringProperty quantity;
        private final SimpleIntegerProperty totalsales;
        private final SimpleIntegerProperty productid;

        public Checkout(String productname, String sellingprice, String quantity, int totalsales, int productid) {
            this.productname = new SimpleStringProperty(productname);
            this.sellingprice = new SimpleStringProperty(sellingprice);
            this.quantity = new SimpleStringProperty(quantity);
            this.totalsales = new SimpleIntegerProperty(totalsales);
            this.productid = new SimpleIntegerProperty(productid);
        }

        public String getProductname() {
            return productname.get();
        }

        public String getSellingprice() {
            return sellingprice.get();
        }

        public String getQuantity() {
            return quantity.get();
        }

        public int getTotalsales() {
            return totalsales.get();
        }

        public int getProductid() {
            return productid.get();
        }

    }
}
