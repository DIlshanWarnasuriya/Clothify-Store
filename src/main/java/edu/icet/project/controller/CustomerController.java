package edu.icet.project.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.icet.project.bo.BoFactory;
import edu.icet.project.bo.custom.CustomerBo;
import edu.icet.project.dto.Customer;
import edu.icet.project.dto.table.CustomerTable;
import edu.icet.project.util.AlertMessage;
import edu.icet.project.util.AlertType;
import edu.icet.project.util.BoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    @FXML
    private JFXTextField txtSearch;
    @FXML
    private Circle circleImage;

    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXTextField txtAddress;
    @FXML
    private JFXTextField txtContactNo;
    @FXML
    private JFXComboBox<String> cmbGender;

    @FXML
    private TableView<CustomerTable> customerTable;
    @FXML
    private TableColumn<?, ?> colImage;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colContactNo;
    @FXML
    private TableColumn<?, ?> colEmail;
    @FXML
    private TableColumn<?, ?> colAddress;

    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);

    private String url = null;
    private Customer selectCustomer = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colImage.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContactNo.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        setGenderTypes();
        loadTable();
        selectRowInTable();
    }

    private void setGenderTypes() {
        ObservableList<String> gender = FXCollections.observableArrayList();
        gender.add("Male");
        gender.add("Female");
        cmbGender.setItems(gender);
    }

    private void loadTable() {
        ObservableList<CustomerTable> list = FXCollections.observableArrayList();
        for (Customer customer : customerBo.getAll()) {
            if (!customer.getStatus().equals("deleted")) {
                Circle circle = new Circle(25, 25, 25);
                circle.setFill(new ImagePattern(new Image(customer.getImageUrl())));
                list.add(new CustomerTable(circle, customer.getId(), customer.getName(), customer.getContactNo(), customer.getEmail(), customer.getAddress()));
            }
        }
        FXCollections.reverse(list);
        customerTable.setItems(list);
    }

    private void selectRowInTable() {
        customerTable.getSelectionModel().selectedItemProperty().addListener((observableValue, customerTable1, select) -> {
            if (select != null) {
                selectCustomer = customerBo.searchById(select.getId());

                txtName.setText(selectCustomer.getName());
                txtEmail.setText(selectCustomer.getEmail());
                txtContactNo.setText(selectCustomer.getContactNo());
                txtAddress.setText(selectCustomer.getAddress());
                cmbGender.setValue(selectCustomer.getGender());
                circleImage.setFill(new ImagePattern(new Image(selectCustomer.getImageUrl())));
                url = selectCustomer.getImageUrl();
            }
        });
    }

    @FXML
    void searchOnAction() {
        String data = txtSearch.getText();
        if (data.isEmpty()) {
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please Enter Search data to Search box");
        } else {
            ObservableList<CustomerTable> list = FXCollections.observableArrayList();
            for (Customer customer : customerBo.search(data)) {
                if (!customer.getStatus().equals("deleted")) {
                    Circle circle = new Circle(25, 25, 25);
                    circle.setFill(new ImagePattern(new Image(customer.getImageUrl())));
                    list.add(new CustomerTable(circle, customer.getId(), customer.getName(), customer.getContactNo(), customer.getEmail(), customer.getAddress()));
                }
            }
            if (list.isEmpty()) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Search Customer not found");
            } else {
                FXCollections.reverse(list);
                customerTable.setItems(list);
            }

        }
    }

    @FXML
    void selectImageOnAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        File file = fileChooser.showOpenDialog(circleImage.getScene().getWindow());

        try {
            if (file != null) {
                Path path = new File("src/main/resources/images/profile/" + file.getName()).toPath();
                Files.copy(file.toPath(), path, StandardCopyOption.REPLACE_EXISTING);

                url = path.toUri().toString();
                circleImage.setFill(new ImagePattern(new Image(path.toUri().toString())));
            }
        } catch (IOException e) {
            AlertMessage.getInstance().informerAlert(AlertType.ERROR, e.getMessage());
        }
    }

    @FXML
    void addOnAction() {
        try {
            String name = txtName.getText();
            String email = txtEmail.getText();
            String address = txtAddress.getText();
            String contactNo = txtContactNo.getText();
            String gender = cmbGender.getValue();
            String imageUrl = url;

            if (name.isEmpty() || email.isEmpty() || address.isEmpty() || contactNo.isEmpty()) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter all data");
            } else if (contactNo.length() != 10 || contactNo.charAt(0) != '0') {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter valid contact number");
            } else if (!customerBo.search(contactNo).isEmpty()) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "This Contact number is already use. Maybe this customer is there. check now");
            } else if (!customerBo.search(email).isEmpty()) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "This email is already use. Maybe this customer is there. check now");
            } else {
                if (gender.equals("Female") && imageUrl.equals("images/profile/Male.png")) {
                    imageUrl = "images/profile/Female.png";
                } else if (gender.equals("Male") && imageUrl.equals("images/profile/Female.png")) {
                    imageUrl = "images/profile/Male.png";
                }

                Customer customer = new Customer(name, email, address, contactNo, gender, imageUrl, "Active");
                boolean res = customerBo.saveCustomer(customer);
                if (res) {
                    AlertMessage.getInstance().informerAlert(AlertType.SUCCESS, "Customer Add Successful");
                    refreshOnAction();
                } else AlertMessage.getInstance().informerAlert(AlertType.ERROR, "Customer Add Fail");
            }
        } catch (RuntimeException e) {
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter all data");
        }

    }

    @FXML
    void updateOnAction() {
        try {
            String name = txtName.getText();
            String email = txtEmail.getText();
            String address = txtAddress.getText();
            String contactNo = txtContactNo.getText();
            String gender = cmbGender.getValue();
            String imageUrl = url;

            if (selectCustomer.getImageUrl().equals(imageUrl) && selectCustomer.getName().equals(name) && selectCustomer.getEmail().equals(email) && selectCustomer.getAddress().equals(address) && selectCustomer.getContactNo().equals(contactNo) && selectCustomer.getGender().equals(gender)) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "No any changes");
            } else if (name.isEmpty() || email.isEmpty() || address.isEmpty() || contactNo.isEmpty()) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter all data");
            } else if (contactNo.length() != 10 || contactNo.charAt(0) != '0') {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter valid contact number");
            } else if (!selectCustomer.getContactNo().equals(contactNo) && !customerBo.search(contactNo).isEmpty()) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "This Contact number is already use");
            } else if (!selectCustomer.getEmail().equals(email) && !customerBo.search(email).isEmpty()) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "This email is already use");
            } else {
                if (gender.equals("Female") && imageUrl.equals("images/profile/Male.png")) {
                    imageUrl = "images/profile/Female.png";
                } else if (gender.equals("Male") && imageUrl.equals("images/profile/Female.png")) {
                    imageUrl = "images/profile/Male.png";
                }

                Customer customer = new Customer(selectCustomer.getId(), name, email, address, contactNo, gender, imageUrl, "Active");
                boolean res = customerBo.updateCustomer(customer);
                if (res) {
                    AlertMessage.getInstance().informerAlert(AlertType.SUCCESS, "Customer Update Successful");
                    refreshOnAction();
                } else AlertMessage.getInstance().informerAlert(AlertType.ERROR, "Customer Update Fail");
            }
        } catch (RuntimeException e) {
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter all data");
        }
    }

    @FXML
    void deleteOnAction() {
        if (selectCustomer == null) {
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please Select Customer form table");
        } else {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Confirmation Dialog");
            alert.setContentText("Are you sure to delete this Customer?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            ImageView icon = new ImageView(new Image("images/icons/WARNING.png"));
            alert.setGraphic(icon);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    selectCustomer.setStatus("deleted");
                    boolean res = customerBo.deleteCustomer(selectCustomer);

                    if (res) {
                        AlertMessage.getInstance().informerAlert(AlertType.SUCCESS, "Customer Delete success");
                        refreshOnAction();
                    } else AlertMessage.getInstance().informerAlert(AlertType.ERROR, "Customer Delete fail");
                }
            });
        }
    }

    @FXML
    void refreshOnAction() {
        txtSearch.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtContactNo.setText("");
        txtAddress.setText("");
        cmbGender.setValue(null);
        cmbGender.setPromptText("Select Gender");
        circleImage.setFill(Color.valueOf("#d9d9d9"));
        loadTable();
        selectCustomer = null;
    }

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

    @FXML
    void minimizeOnAction() {
        Stage stage = (Stage) txtAddress.getScene().getWindow();
        stage.setIconified(true);
    }
}
