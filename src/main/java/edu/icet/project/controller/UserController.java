package edu.icet.project.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import edu.icet.project.bo.BoFactory;
import edu.icet.project.bo.custom.UserBo;
import edu.icet.project.dto.User;
import edu.icet.project.dto.table.UserTable;
import edu.icet.project.util.AlertMessage;
import edu.icet.project.util.AlertType;
import edu.icet.project.util.BoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        if (tableData.isEmpty()){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Search User not found");
        }
        else{
            FXCollections.reverse(tableData);
            userTable.setItems(tableData);
        }
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
                selectUser.setPassword(password);
                txtPassword.setText(selectUser.getPassword());
                url = selectUser.getImageUrl();
            }
        });
    }

    @FXML
    void searchUserOnAction(MouseEvent event) {
        String data = txtSearch.getText();

        if (data.isEmpty()){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter searching data");
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
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "No user found");
                refreshOnAction();
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
            AlertMessage.getInstance().informerAlert(AlertType.ERROR, e.getMessage());
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
            String imageUrl = url;
            String password = Base64.getEncoder().encodeToString(txtPassword.getText().getBytes());

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || contactNo.isEmpty() || gender.isEmpty() || userType.isEmpty()){
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please Enter all data");
            }
            else if (contactNo.length() != 10 || contactNo.charAt(0) != '0'){
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please Enter valid phone No");
            }
            else if(!userBo.search(contactNo).isEmpty()){
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "The Contact number is already use");
            }
            else if(!userBo.search(email).isEmpty()){
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "The Email is already use");
            }
            else{
                if (gender.equals("Female") && selectUser.getImageUrl().equals("images/profile/Male.png")){
                    imageUrl = "images/profile/Female.png";
                }
                else if (gender.equals("Male") && selectUser.getImageUrl().equals("images/profile/Female.png")){
                    imageUrl = "images/profile/Male.png";
                }

                User user = new User (name, email, password, address, contactNo, gender, userType, imageUrl, "Active");
                boolean res = userBo.saveUser(user);

                if (res) {
                    AlertMessage.getInstance().informerAlert(AlertType.SUCCESS, "User added Success");
                    refreshOnAction();
                }
                else AlertMessage.getInstance().informerAlert(AlertType.ERROR, "User added Fail");
            }
        }catch (RuntimeException ex){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please Enter all data");
        }
    }

    @FXML
    void updateUserOnAction(ActionEvent event) {
        try{
            String name = txtName.getText();
            String email = txtEmail.getText();
            String password = txtPassword.getText();
            String address = txtAddress.getText();
            String contactNo = txtContactNo.getText();
            String gender = cmbGender.getValue();
            String userType = cmbUserType.getValue();
            String imageUrl = url;


            if (selectUser.getImageUrl().equals(imageUrl) && selectUser.getName().equals(name) && selectUser.getEmail().equals(email) && selectUser.getPassword().equals(password) && selectUser.getAddress().equals(address) && selectUser.getContactNo().equals(contactNo) && selectUser.getGender().equals(gender) && selectUser.getUserType().equals(userType)){
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "No any changes");
            }
            else if (name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty() || contactNo.isEmpty() || gender.isEmpty() || userType.isEmpty()){
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please select user form table and change eny details");
            }
            else if (contactNo.length() != 10 || contactNo.charAt(0) != '0'){
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please Enter valid phone No");
            }
            else if(!selectUser.getContactNo().equals(contactNo) && !userBo.search(contactNo).isEmpty()){
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "The Contact number is already use");
            }
            else if(!selectUser.getEmail().equals(email) && !userBo.search(email).isEmpty()){
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "The Email is already use");
            }
            else{
                if (gender.equals("Female") && selectUser.getImageUrl().equals("images/profile/Male.png")){
                    imageUrl = "images/profile/Female.png";
                }
                else if (gender.equals("Male") && selectUser.getImageUrl().equals("images/profile/Female.png")){
                    imageUrl = "images/profile/Male.png";
                }
                password = Base64.getEncoder().encodeToString(password.getBytes());

                User user = new User (selectUser.getId(), name, email, password, address, contactNo, gender, userType, imageUrl, "Active");
                boolean res = userBo.updateUser(user);
                if (res) {
                    AlertMessage.getInstance().informerAlert(AlertType.SUCCESS, "User Update Success");
                    refreshOnAction();
                }
                else AlertMessage.getInstance().informerAlert(AlertType.ERROR, "User Update Fail");
            }
        }catch (RuntimeException ex){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please Enter all data");
        }
    }

    @FXML
    void deleteUserOnAction(ActionEvent event) {
        if (selectUser==null){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please select user form table");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Confirmation Dialog");
            alert.setContentText("Are you sure to delete this user?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            ImageView icon = new ImageView(new Image("images/icons/WARNING.png"));
            alert.setGraphic(icon);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    selectUser.setStatus("deleted");
                    boolean res = userBo.deleteUser(selectUser);

                    if (res){
                        AlertMessage.getInstance().informerAlert(AlertType.SUCCESS, "User Delete success");
                        refreshOnAction();
                    }
                    else AlertMessage.getInstance().informerAlert(AlertType.ERROR, "User Delete fail");
                }
            });
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
