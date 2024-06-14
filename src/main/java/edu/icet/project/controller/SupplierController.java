package edu.icet.project.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.icet.project.bo.BoFactory;
import edu.icet.project.bo.custom.SupplierBo;
import edu.icet.project.dto.Supplier;
import edu.icet.project.dto.table.SupplierTable;
import edu.icet.project.util.AlertMessage;
import edu.icet.project.util.AlertType;
import edu.icet.project.util.BoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

public class SupplierController implements Initializable {

    @FXML
    private JFXTextField txtSearch;
    @FXML
    private Circle imageCircle;

    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXTextField txtContactNo;
    @FXML
    private JFXTextField txtCompany;
    @FXML
    private JFXComboBox<String> cmbGender;

    @FXML
    private TableView<SupplierTable> supplierTable;
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
    private TableColumn<?, ?> colCompany;

    private final SupplierBo supplierBo = BoFactory.getInstance().getBo(BoType.SUPPLIER);
    private Supplier selectSupplier = null;
    private String url = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContactNo.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colCompany.setCellValueFactory(new PropertyValueFactory<>("company"));

        setGenderTypes();
        loadTable();
        selectRowInTable();
    }

    private void setGenderTypes() {
        ObservableList<String> type = FXCollections.observableArrayList();
        type.add("Male");
        type.add("Female");
        cmbGender.setItems(type);
    }

    private void loadTable() {
        ObservableList<SupplierTable> list = FXCollections.observableArrayList();
        for (Supplier supplier : supplierBo.getAll()) {
            if (!supplier.getStatus().equals("deleted")) {
                Circle circle = new Circle(25, 25, 25);
                circle.setFill(new ImagePattern(new Image(supplier.getImageUrl())));
                list.add(new SupplierTable(circle, supplier.getId(), supplier.getName(), supplier.getContactNo(), supplier.getEmail(), supplier.getCompany()));
            }
        }
        FXCollections.reverse(list);
        supplierTable.setItems(list);
    }

    private void selectRowInTable() {
        supplierTable.getSelectionModel().selectedItemProperty().addListener((observableValue, supplierTable1, select) -> {
            if (select != null) {
                ObservableList<Supplier> search = supplierBo.search(select.getId().toString());
                selectSupplier = search.get(0);
                url = selectSupplier.getImageUrl();

                txtName.setText(selectSupplier.getName());
                txtEmail.setText(selectSupplier.getEmail());
                txtCompany.setText(selectSupplier.getCompany());
                txtContactNo.setText(selectSupplier.getContactNo());
                cmbGender.setValue(selectSupplier.getGender());
                imageCircle.setFill(new ImagePattern(new Image(url)));
            }
        });
    }

    @FXML
    void searchOnAction() {
        String data = txtSearch.getText();
        if (data.isEmpty()) {
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter searching data");
        } else {
            ObservableList<SupplierTable> list = FXCollections.observableArrayList();
            for (Supplier supplier : supplierBo.search(data)) {
                if (!supplier.getStatus().equals("deleted")) {
                    Circle circle = new Circle(25, 25, 25);
                    circle.setFill(new ImagePattern(new Image(supplier.getImageUrl())));
                    list.add(new SupplierTable(circle, supplier.getId(), supplier.getName(), supplier.getContactNo(), supplier.getEmail(), supplier.getCompany()));
                }
            }
            if (list.isEmpty()) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Search Suppliers not found");
            } else {
                FXCollections.reverse(list);
                supplierTable.setItems(list);
            }
        }
    }

    @FXML
    void selectImageOnAction() {
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
    void addOnAction() {
        try {
            String name = txtName.getText();
            String email = txtEmail.getText();
            String company = txtCompany.getText();
            String contactNo = txtContactNo.getText();
            String gender = cmbGender.getValue();
            String imageUrl = url;

            if (name.isEmpty() || email.isEmpty() || company.isEmpty() || contactNo.isEmpty() || gender.isEmpty()) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter all data");
            } else if (contactNo.length() != 10 || contactNo.charAt(0) != '0') {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter valid contact number");
            } else if (!supplierBo.search(contactNo).isEmpty()) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "This Contact number is already use");
            } else if (!supplierBo.search(email).isEmpty()) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "This email is already use");
            } else {
                if (gender.equals("Female") && imageUrl.equals("images/profile/Male.png")) {
                    imageUrl = "images/profile/Female.png";
                } else if (gender.equals("Male") && imageUrl.equals("images/profile/Female.png")) {
                    imageUrl = "images/profile/Male.png";
                }

                Supplier supplier = new Supplier(name, email, company, contactNo, gender, imageUrl, "Active");
                boolean res = supplierBo.saveSupplier(supplier);
                if (res) {
                    AlertMessage.getInstance().informerAlert(AlertType.SUCCESS, "Supplier Add Successful");
                    refreshOnAction();
                } else AlertMessage.getInstance().informerAlert(AlertType.ERROR, "Supplier Add Fail");
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
            String company = txtCompany.getText();
            String contactNo = txtContactNo.getText();
            String gender = cmbGender.getValue();
            String imageUrl = url;

            if (selectSupplier.getImageUrl().equals(imageUrl) && selectSupplier.getName().equals(name) && selectSupplier.getEmail().equals(email) && selectSupplier.getCompany().equals(company) && selectSupplier.getContactNo().equals(contactNo) && selectSupplier.getGender().equals(gender)) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "No any changes");
            } else if (name.isEmpty() || email.isEmpty() || company.isEmpty() || contactNo.isEmpty()) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter all data");
            } else if (contactNo.length() != 10 || contactNo.charAt(0) != '0') {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter valid contact number");
            } else if (!selectSupplier.getContactNo().equals(contactNo) && !supplierBo.search(contactNo).isEmpty()) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "This Contact number is already use");
            } else if (!selectSupplier.getEmail().equals(email) && !supplierBo.search(email).isEmpty()) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "This email is already use");
            } else {
                if (gender.equals("Female") && imageUrl.equals("images/profile/Male.png")) {
                    imageUrl = "images/profile/Female.png";
                } else if (gender.equals("Male") && imageUrl.equals("images/profile/Female.png")) {
                    imageUrl = "images/profile/Male.png";
                }

                Supplier supplier = new Supplier(selectSupplier.getId(), name, email, company, contactNo, gender, imageUrl, "Active");
                boolean res = supplierBo.updateSupplier(supplier);
                if (res) {
                    AlertMessage.getInstance().informerAlert(AlertType.SUCCESS, "Supplier Update Successful");
                    refreshOnAction();
                } else AlertMessage.getInstance().informerAlert(AlertType.ERROR, "Supplier Update Fail");
            }
        } catch (RuntimeException e) {
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter all data");
        }
    }

    @FXML
    void deleteOnAction() {
        if (selectSupplier == null) {
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please Select Supplier form table");
        } else {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Confirmation Dialog");
            alert.setContentText("Are you sure to delete this Supplier?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            ImageView icon = new ImageView(new Image("images/icons/WARNING.png"));
            alert.setGraphic(icon);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    selectSupplier.setStatus("deleted");
                    boolean res = supplierBo.deleteSupplier(selectSupplier);

                    if (res) {
                        AlertMessage.getInstance().informerAlert(AlertType.SUCCESS, "Supplier Delete success");
                        refreshOnAction();
                    } else AlertMessage.getInstance().informerAlert(AlertType.ERROR, "Supplier Delete fail");
                }
            });
        }
    }

    @FXML
    void refreshOnAction() {
        txtSearch.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtCompany.setText("");
        txtContactNo.setText("");
        cmbGender.setValue(null);
        cmbGender.setPromptText("Select Gender");
        imageCircle.setFill(Color.valueOf("#d9d9d9"));
        selectSupplier = null;
        url = null;
        loadTable();
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
        Stage stage = (Stage) txtCompany.getScene().getWindow();
        stage.setIconified(true);
    }


}
