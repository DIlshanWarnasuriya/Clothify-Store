package edu.icet.project.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.icet.project.bo.BoFactory;
import edu.icet.project.bo.custom.UserBo;
import edu.icet.project.dto.User;
import edu.icet.project.util.AlertMessage;
import edu.icet.project.util.AlertType;
import edu.icet.project.util.BoType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

public class ProfileController {

    @FXML
    private Circle imageCircle;

    @FXML
    private Label lblUserName;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtContactNo;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private Circle loggedUserImage;

    @FXML
    private Label loggedUserName;

    @FXML
    private Label loggedUserType;

    private final UserBo userBo = BoFactory.getInstance().getBo(BoType.USER);

    private User loggedUser;
    private String url;

    public void setUser(User user){
        loggedUser = user;
        loggedUserDetail();
        setUserDetails();
    }

    private void loggedUserDetail(){
        loggedUserName.setText(loggedUser.getName());
        loggedUserType.setText(loggedUser.getUserType());
        loggedUserImage.setFill(new ImagePattern(new Image(loggedUser.getImageUrl())));
    }

    private void setUserDetails(){
        imageCircle.setFill(new ImagePattern(new Image(loggedUser.getImageUrl())));
        lblUserName.setText(loggedUser.getName());
        txtEmail.setText(loggedUser.getEmail());
        txtAddress.setText(loggedUser.getAddress());
        txtContactNo.setText(loggedUser.getContactNo());
    }


    @FXML
    void selectImageOnAction(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        File file = fileChooser.showOpenDialog(imageCircle.getScene().getWindow());

        try {
            if (file != null) {
                Path path = new File("src/main/resources/images/profile/" + file.getName()).toPath();
                Files.copy(file.toPath(), path, StandardCopyOption.REPLACE_EXISTING);

                url = path.toUri().toString();
                imageCircle.setFill(new ImagePattern(new Image(path.toUri().toString())));
            }
        } catch (IOException e) {
            AlertMessage.getInstance().informerAlert(AlertType.ERROR, e.getMessage());
        }
    }

    @FXML
    void updateDetailOnAction(ActionEvent event) {
        String email = txtEmail.getText();
        String address = txtAddress.getText();
        String contactNo = txtContactNo.getText();
        String imageUrl = url==null ? loggedUser.getImageUrl() : url;
        String password = txtPassword.getText();
        String userPassword = new String(Base64.getDecoder().decode(loggedUser.getPassword()));

        if(email.isEmpty() || address.isEmpty() || contactNo.isEmpty() || password.isEmpty()){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please fill all data");
        }
        else if (loggedUser.getEmail().equals(email) && loggedUser.getAddress().equals(address) && loggedUser.getContactNo().equals(contactNo)){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "No any changes");
        }
        else if (contactNo.length() != 10 || contactNo.charAt(0) != '0') {
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter valid contact number");
        }
        else if (password.length() < 8) {
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter 8 or mor characters for password");
        }
        else if(!userPassword.equals(password)){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Password is wrong. enter correct password");
        }
        else{
            loggedUser.setEmail(email);
            loggedUser.setAddress(address);
            loggedUser.setContactNo(contactNo);
            loggedUser.setImageUrl(imageUrl);

            boolean res = userBo.updateUser(loggedUser);
            if (res) AlertMessage.getInstance().informerAlert(AlertType.SUCCESS, "user detail update successful");
            else AlertMessage.getInstance().informerAlert(AlertType.ERROR, "user detail update fail");
            refreshOnAction();
        }
    }

    @FXML
    void forgotPasswordOnAction(MouseEvent event) {

    }

    @FXML
    void refreshOnAction() {
        setUserDetails();
        loggedUserDetail();
    }
    @FXML
    void closeOnAction(MouseEvent event) {
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
    @FXML
    void minimizeOnAction(MouseEvent event) {
        Stage stage = (Stage) txtAddress.getScene().getWindow();
        stage.setIconified(true);
    }
}
