package edu.icet.project.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.icet.project.bo.BoFactory;
import edu.icet.project.bo.custom.UserBo;
import edu.icet.project.dto.User;
import edu.icet.project.dto.table.UserTable;
import edu.icet.project.util.BoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private TableView<UserTable> userTable;
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
    private User user = null;

    String url = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colNumber.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colType.setCellValueFactory(new PropertyValueFactory<>("userType"));

        setTypeOfUsers();
        setGenders();
        loadTable();
        selectRowInTable();
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

    private void loadTable(){

        ObservableList<UserTable> tableData = FXCollections.observableArrayList();
        for (User users : userBo.getAll()){
            double width = 50;
            double height = 50;
            Circle circle = new Circle(width / 2, height / 2, Math.min(width, height) / 2);
            circle.setStrokeWidth(2);
            circle.setFill(new ImagePattern(new Image(users.getImageUrl())));

            tableData.add(new UserTable(circle, users.getId(), users.getName(), users.getContactNo(), users.getEmail(), users.getAddress(), users.getUserType()));
        }
        FXCollections.reverse(tableData);
        userTable.setItems(tableData);
    }

    private void selectRowInTable(){
        userTable.getSelectionModel().selectedItemProperty().addListener((observableValue, userTable1, select) -> {
            if (select != null){
                for (User users : userBo.getAll()){
                    if (Objects.equals(users.getId(), select.getId())){
                        user = users;

                        imageCircle.setFill(new ImagePattern(new Image(users.getImageUrl())));
                        txtName.setText(users.getName());
                        txtEmail.setText(users.getEmail());
                        txtAddress.setText(users.getAddress());
                        txtContactNo.setText(users.getContactNo());
                        cmbGender.setValue(users.getGender());
                        cmbUserType.setValue(users.getUserType());
                        return;
                    }
                }
            }
        });
    }

    @FXML
    void searchUserOnAction(MouseEvent event) {
        if (txtSearch.getText().isEmpty()){
            new Alert(Alert.AlertType.WARNING, "Please enter searching data").show();
        }
        else {
            String search = txtSearch.getText();
            ObservableList<UserTable> tableData = FXCollections.observableArrayList();

            for (User users : userBo.getAll()){
                if (users.getId().toString().equals(search) || users.getName().equals(search) || users.getEmail().equals(search) || users.getAddress().equals(search) || users.getContactNo().equals(search) || users.getGender().equals(search) || users.getUserType().equals(search)){

                    double width = 50;
                    double height = 50;
                    Circle circle = new Circle(width / 2, height / 2, Math.min(width, height) / 2);
                    circle.setStrokeWidth(2);
                    circle.setFill(new ImagePattern(new Image(users.getImageUrl())));

                    tableData.add(new UserTable(circle, users.getId(), users.getName(), users.getContactNo(), users.getEmail(), users.getAddress(), users.getUserType()));
                }
            }

            if (tableData.isEmpty()){
                new Alert(Alert.AlertType.WARNING, "No user found").show();
                txtSearch.setText("");
            }
            else{
                FXCollections.reverse(tableData);
                userTable.setItems(tableData);
            }
        }
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

                if (res) {
                    new Alert(Alert.AlertType.INFORMATION, "User added success").show();
                    loadTable();
                }
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
    void refreshOnAction(MouseEvent event) {

    }

    @FXML
    void closeOnAction(MouseEvent event) {

    }

    @FXML
    void minimizeOnAction(MouseEvent event) {

    }
}
