package controller;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomeController implements Initializable {
    
    @FXML
    private Label userNameLabel;
    private FlowPane flowPane;
    @FXML
    private TableColumn<?, ?> productNameCol;
    @FXML
    private TableColumn<?, ?> farmerNameCol;
    @FXML
    private StackPane motherPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    @FXML
    private void logoutAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Want to exit?");
        alert.setTitle("Confirm Exit!!");
        System.exit(0);
        
    }
    
    @FXML
    private void goHomeAction(ActionEvent event) throws IOException {
        motherPane.getChildren().clear();
        motherPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/products.fxml")));
    }
    
    @FXML
    private void userProfileAction(ActionEvent event) throws IOException {
        motherPane.getChildren().clear();
        motherPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/customerPage.fxml")));
    }
    
    @FXML
    private void userOrdersAction(ActionEvent event) throws IOException {
        motherPane.getChildren().clear();
        motherPane.getChildren().add(FXMLLoader.load(getClass().getResource("/view/customerorders.fxml")));
    }
    
    private void searchProduct(KeyEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Its working");
        alert.showAndWait();
    }
    private double xOffset = 0;
    private double yOffset = 0;
    
    private void loadStage(String fxml, String title) {
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension size = tk.getScreenSize();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
//            stage.getIcons().add(new Image("/Files/heart_beat.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            
            stage.initStyle(StageStyle.DECORATED.UNDECORATED);
            root.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });
            stage.setScene(new Scene(root, size.getWidth() , size.getHeight()));
//            stage.setScene(new Scene(root, size.width, size.height));
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    @FXML
    private void closeSystem(ActionEvent event) {
        System.exit(0);
    }
}
