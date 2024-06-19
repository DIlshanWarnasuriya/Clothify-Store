package edu.icet.project.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.icet.project.bo.BoFactory;
import edu.icet.project.bo.custom.UserBo;
import edu.icet.project.dto.User;
import edu.icet.project.util.AlertMessage;
import edu.icet.project.util.AlertType;
import edu.icet.project.util.BoType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Base64;

public class LoginController {

    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXPasswordField txtPassword;

    private final UserBo userBo = BoFactory.getInstance().getBo(BoType.USER);

    // Login Button Action Event
    @FXML
    void loginOnAction() throws IOException {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        if (email.isEmpty() || password.isEmpty()){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Enter Email or password");
        }
        else{
            User user = searchUser(email, password); // search user using email and password
            if (user==null){
                AlertMessage.getInstance().informerAlert(AlertType.ERROR, "Email or password is wrong");
            }
            else{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Home.fxml"));
                Parent root = loader.load();
                HomeController controller = loader.getController();
                controller.setUser(user);

                Stage stage = (Stage) txtEmail.getScene().getWindow();
                stage.close();

                Stage newStage = new Stage();
                newStage.getIcons().add(new Image("images/icons/Logo.png"));
                newStage.setScene(new Scene(root));
                newStage.show();

            }
        }
    }

    // search user
    private User searchUser(String email, String password){
        for (User user : userBo.getAll()){
            String userPassword = new String(Base64.getDecoder().decode(user.getPassword()));
            if (user.getEmail().equals(email) && userPassword.equals(password) && !user.getStatus().equals("deleted")){
                return user;
            }
        }
        return null;
    }

    // Forgot password Button Action Event
    @FXML
    void forgotPasswordOnAction() {
        // Forgot password Button Action Event
    }

    // Close Button Action Event
    @FXML
    void closeOnAction() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Confirmation Dialog");
        alert.setContentText("Are you close the program");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        ImageView icon = new ImageView(new Image("images/icons/WARNING.png"));
        alert.setGraphic(icon);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                System.exit(0);
            }
        });
    }

    // Minimize Button Action Event
    @FXML
    void minimizeOnAction() {
        Stage stage = (Stage) txtEmail.getScene().getWindow();
        stage.setIconified(true);
    }
}