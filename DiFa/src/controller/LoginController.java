package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static database.DatabaseFunctions.CheckUserExists;
import static database.DatabaseFunctions.getId;
import static database.DatabaseFunctions.getRole;
import static database.DatabaseFunctions.loggedIn;
import static database.DatabaseFunctions.loginMain;
import functions.functions;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class LoginController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private BorderPane loginPane;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        username.setId("usernamefield");
        password.setId("passwordfield");

        username.setText("farmer one");
        password.setText("farmer");
        database.DatabaseFunctions.deleteLoggedInUsers();
    }

    @FXML
    private void loginAction(ActionEvent event) {

        tryLogin();

    }

    @FXML
    private void forgotPassword(ActionEvent event) {
        TextInputDialog tid = new TextInputDialog();
        tid.setHeaderText("Enter Username");
        tid.setTitle("Forgot Password");
        tid.showAndWait();
        String username1 = tid.getEditor().getText();

        database.DatabaseFunctions.forgetPasswordReset(username1);
        // To add functionality for forgetting password. Will Answer questions
    }

    @FXML
    private void close(ActionEvent event) {
        System.exit(0);
    }

    private void loadStage(String fxml) {

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension size = tk.getScreenSize();
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
//            stage.getIcons().add(new Image("/Files/heart_beat.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Digital Farmers | HOME");

            stage.initStyle(StageStyle.DECORATED.UNDECORATED);
            root.setOnMousePressed((MouseEvent event) -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });
            root.setOnMouseDragged((MouseEvent event) -> {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            });
            stage.setScene(new Scene(root, size.getWidth(), size.getHeight()));
//            stage.setScene(new Scene(root, size.width, size.height));
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void tryLogin() {
        if (!username.getText().isEmpty() && !password.getText().isEmpty()) {

            if (CheckUserExists(username.getText())) {

                if (loginMain(username.getText(), password.getText())) {

                    loggedIn(getRole(username.getText()), getId(username.getText()));
                    switch (getRole(getId(username.getText()))) {
                        case "ADMIN": {
                            Stage stage = (Stage) loginPane.getScene().getWindow();
                            loadStage("/view/admin.fxml");
                            stage.close();
                            break;
                        }
                        case "CUSTOMER": {
                            Stage stage = (Stage) loginPane.getScene().getWindow();
                            loadStage("/view/customer.fxml");
                            stage.close();
                            break;
                        }
                        case "FARMER": {
                            Stage stage = (Stage) loginPane.getScene().getWindow();
                            loadStage("/view/farmerPage.fxml");
                            stage.close();
                            break;
                        }
                        default:
                            break;
                    }

                } else {
//                    username.setText("");
//                    password.setText("");
                    username.setId("wrongdetails");
                    password.setId("wrongdetails");
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setTitle("Login Error");
                    alert2.setContentText("Incorrect Password");
                    alert2.showAndWait();
                }
            } else {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setTitle("Login Error");
                alert2.setContentText("User does not exist");
                alert2.showAndWait();
            }
        } else {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Login Error");
            alert2.setContentText("Provide All the Fields");
            alert2.showAndWait();
        }
    }

    @FXML
    private void signUp(ActionEvent event) {
        Dialog<Pair<String, String>> newProduct = new Dialog();
        newProduct.setTitle("Sign Up | DIFA");

        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        newProduct.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
        gp.setPadding(new Insets(20, 150, 10, 10));

        Label firstNsame = new Label("First Name");
        JFXTextField firstNameField = new JFXTextField();
        firstNameField.setPromptText("John");

        Label surnameName = new Label("Surname");
        JFXTextField surnameNameField = new JFXTextField();
        surnameNameField.setPromptText("Doe");

        Label tel = new Label("Mobile Number");
        JFXTextField telField = new JFXTextField();
        telField.setPromptText("0712 345 678");

        Label email = new Label("Email");
        JFXTextField emailField = new JFXTextField();
        emailField.setPromptText("john@doe.difa");

        Label userName = new Label("Username");
        JFXTextField userNameField = new JFXTextField();
        userNameField.setPromptText("johndoe");
        Label address = new Label("Address");
        JFXTextField addressField = new JFXTextField();
        userNameField.setPromptText("Your Address");

        Label role = new Label("Choose Account Type");
        JFXComboBox roleField = new JFXComboBox();
        roleField.getItems().addAll("CUSTOMER", "FARMER");
        roleField.setPromptText("Account Type");

        Label password1 = new Label("Password");
        JFXPasswordField password1Field = new JFXPasswordField();
        password1Field.setPromptText("Password");

        Label password2 = new Label(" Retype Password");
        JFXPasswordField password2Field = new JFXPasswordField();
        password2Field.setPromptText("Confirm Password");

        gp.add(firstNsame, 0, 0);
        gp.add(firstNameField, 1, 0);
        gp.add(surnameName, 0, 1);
        gp.add(surnameNameField, 1, 1);
        gp.add(tel, 0, 2);
        gp.add(telField, 1, 2);
        gp.add(email, 0, 3);
        gp.add(emailField, 1, 3);
        gp.add(userName, 0, 4);
        gp.add(userNameField, 1, 4);
        gp.add(role, 0, 5);
        gp.add(roleField, 1, 5);
        gp.add(address, 0, 6);
        gp.add(addressField, 1, 6);
        gp.add(password1, 0, 7);
        gp.add(password1Field, 1, 7);
        gp.add(password2, 0, 8);
        gp.add(password2Field, 1, 8);

        newProduct.getDialogPane().setContent(gp);

        newProduct.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {

                if (!password1Field.getText().equals(password2Field.getText())) {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setContentText("Password mismatch");
                    alert2.setHeaderText("Error!");
                    alert2.showAndWait();
                } else {
                    if (database.DatabaseFunctions.addNewUser(roleField.getSelectionModel().getSelectedItem().toString(), functions.generateHash(password1Field.getText()), userNameField.getText(), firstNameField.getText() + " " + surnameNameField.getText(), telField.getText(), emailField.getText(), addressField.getText())) {

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Sign Up Successfully");
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
}
