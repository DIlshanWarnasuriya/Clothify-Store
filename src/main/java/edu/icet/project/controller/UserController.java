package edu.icet.project.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.icet.project.bo.BoFactory;
import edu.icet.project.bo.custom.UserBo;
import edu.icet.project.dto.User;
import edu.icet.project.util.BoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    private JFXTextField txtSearch;
    @FXML
    private Circle imageCircle;

    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXPasswordField txtPassword;
    @FXML
    private JFXTextField txtAddress;
    @FXML
    private JFXTextField txtContactNo;
    @FXML
    private JFXComboBox<String> cmbGender;
    @FXML
    private JFXComboBox<String> cmbUserType;

    @FXML
    private TableView<?> userTable;
    @FXML
    private TableColumn<?, ?> colAddress;
    @FXML
    private TableColumn<?, ?> colEmail;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colImage;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colNumber;
    @FXML
    private TableColumn<?, ?> colType;

    private UserBo userBo = BoFactory.getInstance().getBo(BoType.USER);

    String url = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTypeOfUsers();
        setGenders();
    }

    private void setTypeOfUsers() {
        ObservableList<String> type = FXCollections.observableArrayList();
        type.add("Admin");
        type.add("User");
        cmbUserType.setItems(type);
    }

    private void setGenders() {
        ObservableList<String> type = FXCollections.observableArrayList();
        type.add("Male");
        type.add("Female");
        cmbGender.setItems(type);
    }

    @FXML
    void searchUserOnAction(MouseEvent event) {

    }

    @FXML
    void selectImageOnAction(MouseEvent event) {

    }

    @FXML
    void addUserOnAction(ActionEvent event) {

        try{
            String name = txtName.getText();
            String email = txtEmail.getText();
            String password = txtPassword.getText();
            String address = txtAddress.getText();
            String contactNo = txtContactNo.getText();
            String gender = cmbGender.getValue();
            String userType = cmbUserType.getValue();
            String imageUrl = url == null ? (Objects.equals(gender, "Male") ? "images/profile/Male.png" : "images/profile/Female.png") : url;

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || contactNo.isEmpty() || gender.isEmpty() || userType.isEmpty()){
                new Alert(Alert.AlertType.WARNING, "Please Enter all data").show();
            }
            else if (contactNo.length() != 10 || contactNo.charAt(0) != '0'){
                new Alert(Alert.AlertType.WARNING, "Please Enter valid phone No").show();
            }
            else{
                User user = new User (name, email, password, address, contactNo, gender, userType, imageUrl, "Active");
                boolean res = userBo.saveUser(user);

                if (res) new Alert(Alert.AlertType.INFORMATION, "User added success").show();
                else new Alert(Alert.AlertType.ERROR, "User added Fail").show();
            }
        }catch (RuntimeException ex){
            new Alert(Alert.AlertType.WARNING, "Please Enter all data").show();
        }
    }

    @FXML
    void updateUserOnAction(ActionEvent event) {

    }

    @FXML
    void deleteUserOnAction(ActionEvent event) {

    }

    @FXML
    void closeOnAction(MouseEvent event) {

    }

    @FXML
    void minimizeOnAction(MouseEvent event) {

    }
}
