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
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
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

    private final UserBo userBo = BoFactory.getInstance().getBo(BoType.USER);

    private User selectUser = null;
    private String url = null;

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
        for (User user : userBo.getAll()){
            if (!user.getStatus().equals("deleted")){
                Circle circle = new Circle(25, 25, 25);
                circle.setFill(new ImagePattern(new Image(user.getImageUrl())));

                tableData.add(new UserTable(circle, user.getId(), user.getName(), user.getContactNo(), user.getEmail(), user.getAddress(), user.getUserType()));
            }
        }
        FXCollections.reverse(tableData);
        userTable.setItems(tableData);
    }

    private void selectRowInTable(){
        userTable.getSelectionModel().selectedItemProperty().addListener((observableValue, userTable1, select) -> {
            if (select != null){
                ObservableList<User> search = userBo.search(select.getId().toString());
                selectUser = search.get(0);

                imageCircle.setFill(new ImagePattern(new Image(selectUser.getImageUrl())));
                txtName.setText(selectUser.getName());
                txtEmail.setText(selectUser.getEmail());
                txtAddress.setText(selectUser.getAddress());
                txtContactNo.setText(selectUser.getContactNo());
                cmbGender.setValue(selectUser.getGender());
                cmbUserType.setValue(selectUser.getUserType());

                //decode password
                byte[] decodeByte = Base64.getDecoder().decode(selectUser.getPassword());
                String password = new String(decodeByte);
                txtPassword.setText(password);
            }
        });
    }

    @FXML
    void searchUserOnAction(MouseEvent event) {
        String data = txtSearch.getText();

        if (data.isEmpty()){
            new Alert(Alert.AlertType.WARNING, "Please enter searching data").show();
        }
        else {
            ObservableList<UserTable> tableData = FXCollections.observableArrayList();
            for (User user : userBo.search(data)){
                if (!user.getStatus().equals("deleted")){
                    Circle circle = new Circle(25, 25, 25);
                    circle.setFill(new ImagePattern(new Image(user.getImageUrl())));

                    tableData.add(new UserTable(circle, user.getId(), user.getName(), user.getContactNo(), user.getEmail(), user.getAddress(), user.getUserType()));
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        File file = fileChooser.showOpenDialog(imageCircle.getScene().getWindow());

        try {
            if (file != null){
                Path path = new File("src/main/resources/images/profile/" + file.getName()).toPath();
                Files.copy(file.toPath(), path, StandardCopyOption.REPLACE_EXISTING);

                url = path.toUri().toString();

                imageCircle.setFill(new ImagePattern(new Image(path.toUri().toString())));
            }

        } catch (IOException e) {
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }


    }

    @FXML
    void addUserOnAction(ActionEvent event) {
        try{
            String name = txtName.getText();
            String email = txtEmail.getText();
            String address = txtAddress.getText();
            String contactNo = txtContactNo.getText();
            String gender = cmbGender.getValue();
            String userType = cmbUserType.getValue();
            String imageUrl = url == null ? (Objects.equals(gender, "Male") ? "images/profile/Male.png" : "images/profile/Female.png") : url;
            String password = Base64.getEncoder().encodeToString(txtPassword.getText().getBytes());

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || contactNo.isEmpty() || gender.isEmpty() || userType.isEmpty()){
                new Alert(Alert.AlertType.WARNING, "Please Enter all data").show();
            }
            else if (contactNo.length() != 10 || contactNo.charAt(0) != '0'){
                new Alert(Alert.AlertType.WARNING, "Please Enter valid phone No").show();
            }
            else if(userBo.search(contactNo) == null){
                new Alert(Alert.AlertType.WARNING, "The Contact number is already use").show();
            }
            else if(userBo.search(email) == null){
                new Alert(Alert.AlertType.WARNING, "The Email is already use").show();
            }
            else{
                User user = new User (name, email, password, address, contactNo, gender, userType, imageUrl, "Active");
                boolean res = userBo.saveUser(user);

                if (res) {
                    new Alert(Alert.AlertType.INFORMATION, "User added success").show();
                    refreshOnAction();
                }
                else new Alert(Alert.AlertType.ERROR, "User added Fail").show();
            }
        }catch (RuntimeException ex){
            new Alert(Alert.AlertType.WARNING, "Please Enter all data").show();
        }
    }

    @FXML
    void updateUserOnAction(ActionEvent event) {
        try{
            String name = txtName.getText();
            String email = txtEmail.getText();
            String address = txtAddress.getText();
            String contactNo = txtContactNo.getText();
            String gender = cmbGender.getValue();
            String userType = cmbUserType.getValue();
            String imageUrl = url == null ? selectUser.getImageUrl() : url;
            String password = Base64.getEncoder().encodeToString(txtPassword.getText().getBytes());

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || contactNo.isEmpty() || gender.isEmpty() || userType.isEmpty()){
                new Alert(Alert.AlertType.WARNING, "Please select user form table and change eny details").show();
            }
            else if (contactNo.length() != 10 || contactNo.charAt(0) != '0'){
                new Alert(Alert.AlertType.WARNING, "Please Enter valid phone No").show();
            }
            else if(!selectUser.getContactNo().equals(contactNo) && userBo.search(contactNo) != null){
                new Alert(Alert.AlertType.WARNING, "The Contact number is already use").show();
            }
            else if(!selectUser.getEmail().equals(email) && userBo.search(email) != null){
                new Alert(Alert.AlertType.WARNING, "The Email is already use").show();
            }
            else{
                User user = new User (selectUser.getId(), name, email, password, address, contactNo, gender, userType, imageUrl, "Active");
                boolean res = userBo.updateUser(user);
                if (res) {
                    new Alert(Alert.AlertType.INFORMATION, "User Update success").show();
                    refreshOnAction();
                    selectUser=null;
                }
                else new Alert(Alert.AlertType.ERROR, "User Update Fail").show();
            }
        }catch (RuntimeException ex){
            new Alert(Alert.AlertType.WARNING, "Please Enter all data").show();
        }
    }

    @FXML
    void deleteUserOnAction(ActionEvent event) {
        if (selectUser==null){
            new Alert(Alert.AlertType.WARNING, "Please select user form table").show();
        }
        else{
            selectUser.setStatus("deleted");
            boolean res = userBo.deleteUser(selectUser);
            
            if (res){
                new Alert(Alert.AlertType.INFORMATION, "User Delete success").show();
                refreshOnAction();
                selectUser=null;
            }
            else new Alert(Alert.AlertType.ERROR, "User Delete Fail").show();
        }
    }

    @FXML
    void refreshOnAction() {
        txtSearch.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtContactNo.setText("");
        txtPassword.setText("");
        cmbUserType.setValue(null);
        cmbUserType.setPromptText("Select User Type");
        cmbGender.setValue(null);
        cmbGender.setPromptText("Select Gender");
        imageCircle.setFill(Color.valueOf("#d9d9d9"));
        selectUser=null;
        loadTable();
    }

    @FXML
    void closeOnAction(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    void minimizeOnAction(MouseEvent event) {
        Stage stage = (Stage) txtAddress.getScene().getWindow();
        stage.setIconified(true);
    }
}
