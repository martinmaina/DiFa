package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyEvent;

public class ProductsController implements Initializable {

    @FXML
    private TableColumn<?, ?> productNameCol;
    @FXML
    private TableColumn<?, ?> farmerNameCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void searchProduct(KeyEvent event) {
        System.out.println("Key pressed");
    }

}
