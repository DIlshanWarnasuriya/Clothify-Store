package edu.icet.project.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.icet.project.bo.BoFactory;
import edu.icet.project.bo.custom.ProductBo;
import edu.icet.project.bo.custom.SupplierBo;
import edu.icet.project.dto.Product;
import edu.icet.project.dto.table.ProductTable;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML
    private JFXTextField txtSearch;
    @FXML
    private ImageView imageBox;

    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXTextField txtPrice;
    @FXML
    private JFXTextField txtQty;
    @FXML
    private JFXTextField txtSupplierId;
    @FXML
    private JFXComboBox<String> cmbSize;
    @FXML
    private JFXComboBox<String> cmbCategory;

    @FXML
    private TableView<ProductTable> productTable;
    @FXML
    private TableColumn<?, ?> colImage;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colName;
    @FXML
    private TableColumn<?, ?> colSize;
    @FXML
    private TableColumn<?, ?> colQty;
    @FXML
    private TableColumn<?, ?> colPrice;
    @FXML
    private TableColumn<?, ?> colCategory;
    @FXML
    private TableColumn<?, ?> colDate;
    @FXML
    private TableColumn<?, ?> colSupplierId;

    private final ProductBo productBo = BoFactory.getInstance().getBo(BoType.PRODUCT);
    private final SupplierBo supplierBo = BoFactory.getInstance().getBo(BoType.SUPPLIER);
    private Product selectProduct = null;
    private String url = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));

        setSizeType();
        setCategoryType();
        loadTable();
        selectRowInTable();
    }

    private void setSizeType(){
        ObservableList<String> type = FXCollections.observableArrayList();
        type.add("XS");
        type.add("S");
        type.add("L");
        type.add("XL");
        type.add("XXL");
        cmbSize.setItems(type);
    }

    private void setCategoryType(){
        ObservableList<String> type = FXCollections.observableArrayList();
        type.add("Men");
        type.add("Women");
        type.add("Kid");
        cmbCategory.setItems(type);
    }

    private void loadTable(){
        ObservableList<ProductTable> list = FXCollections.observableArrayList();
        for (Product product : productBo.getAllProduct()) {
            if (!product.getStatus().equals("deleted")){
                ImageView imageView = new ImageView(new Image(product.getImageUrl()));
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);
                String date = new SimpleDateFormat("yyyy-MM-dd").format(product.getDate());
                list.add(new ProductTable(imageView, product.getId(), product.getName(), product.getQty(), product.getSize(), product.getPrice(), product.getCategory(), date, product.getSupplierId()));
            }
        }
        FXCollections.reverse(list);
        productTable.setItems(list);
    }

    private void selectRowInTable(){
        productTable.getSelectionModel().selectedItemProperty().addListener((observableValue, productTable1, select) -> {
            if (select!=null){
                selectProduct = productBo.searchById(select.getId());
                url = selectProduct.getImageUrl();

                imageBox.setImage(new Image(selectProduct.getImageUrl()));
                txtName.setText(select.getName());
                txtPrice.setText(select.getPrice().toString());
                txtQty.setText(select.getQty().toString());
                txtSupplierId.setText(select.getSupplierId().toString());
                cmbSize.setValue(select.getSize());
                cmbCategory.setValue(select.getCategory());
            }
        });
    }

    @FXML
    void searchOnAction() {
        String data = txtSearch.getText();
        if (data.isEmpty()){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter search data to search box");
        }
        else {
            ObservableList<ProductTable> list = FXCollections.observableArrayList();
            for (Product product : productBo.searchProduct(data)) {
                if (!product.getStatus().equals("deleted")){
                    ImageView imageView = new ImageView(new Image(product.getImageUrl()));
                    imageView.setFitHeight(50);
                    imageView.setFitWidth(50);
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(product.getDate());
                    list.add(new ProductTable(imageView, product.getId(), product.getName(), product.getQty(), product.getSize(), product.getPrice(), product.getCategory(), date, product.getSupplierId()));
                }
            }
            if (list.isEmpty()){
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Products not found");
            }
            else{
                FXCollections.reverse(list);
                productTable.setItems(list);
            }
        }
    }

    @FXML
    void selectImageOnAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        File file = fileChooser.showOpenDialog(imageBox.getScene().getWindow());

        try{
            if (file!=null){
                Path path = new File("src/main/resources/images/product/" + file.getName()).toPath();
                Files.copy(file.toPath(), path, StandardCopyOption.REPLACE_EXISTING);

                url = path.toUri().toString();
                imageBox.setImage(new Image(path.toUri().toString()));
            }
        } catch (IOException e) {
            AlertMessage.getInstance().informerAlert(AlertType.ERROR, e.getMessage());
        }
    }

    @FXML
    void addOnAction() {
        try{
            String name = txtName.getText();
            double price = Double.parseDouble(txtPrice.getText());
            int qty = Integer.parseInt(txtQty.getText());
            int supplierId = Integer.parseInt(txtSupplierId.getText());
            String size = cmbSize.getValue();
            String category = cmbCategory.getValue();
            String imageUrl = url==null ? "" : url;
            Date date = new Date();

            if (name.isEmpty() || price<=0 || qty<=0 || supplierId<=0 || size.isEmpty() || category.isEmpty()){
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter all data");
            }
            else if(imageUrl.isEmpty()){
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please Select image");
            }
            else if(supplierBo.searchById(supplierId) == null){
                AlertMessage.getInstance().informerAlert(AlertType.ERROR, "Supplier id is Wrong. please enter correct supplier id");
            }
            else{
                Product product = new Product(name, size, price, qty, imageUrl, category, supplierId, 7, date, "Active");

                if (productBo.equalsProduct(product)){
                    AlertMessage.getInstance().informerAlert(AlertType.WARNING, "this product is already have in stock");
                }
                else{
                    boolean res = productBo.saveProduct(product);
                    if (res){
                        AlertMessage.getInstance().informerAlert(AlertType.SUCCESS, "Product Add Successful");
                        refreshOnAction();
                    } else AlertMessage.getInstance().informerAlert(AlertType.ERROR, "Product Add Fail");
                }
            }
        }catch (RuntimeException e){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter valid data for all textBoxes and select Image");
        }
    }

    @FXML
    void updateOnAction() {
        try{
            String name = txtName.getText();
            double price = Double.parseDouble(txtPrice.getText());
            int qty = Integer.parseInt(txtQty.getText());
            int supplierId = Integer.parseInt(txtSupplierId.getText());
            String size = cmbSize.getValue();
            String category = cmbCategory.getValue();
            String imageUrl = url==null ? "" : url;
            Date date = new Date();

            if (name.isEmpty() || price<=0 || qty<=0 || supplierId<=0 || size.isEmpty() || category.isEmpty()){
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter all data");
            }
            else if(imageUrl.isEmpty()){
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please Select image");
            }
            else if(supplierBo.searchById(supplierId) == null){
                AlertMessage.getInstance().informerAlert(AlertType.ERROR, "Supplier id is Wrong. please enter correct supplier id");
            }
            else{
                Product product = new Product(selectProduct.getId() ,name, size, price, qty, imageUrl, category, supplierId, 7, date, "Active");

                boolean res = productBo.updateProduct(product);
                if (res){
                    AlertMessage.getInstance().informerAlert(AlertType.SUCCESS, "Product Update Successful");
                    refreshOnAction();
                } else AlertMessage.getInstance().informerAlert(AlertType.ERROR, "Product Update Fail");
            }
        }catch (RuntimeException e){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter valid data for all textBoxes and select Image");
        }
    }

    @FXML
    void deleteOnAction() {
        if (selectProduct==null){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please Select Customer form table");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Confirmation Dialog");
            alert.setContentText("Are you sure to delete this Product?");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            ImageView icon = new ImageView(new Image("images/icons/WARNING.png"));
            alert.setGraphic(icon);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    selectProduct.setStatus("deleted");
                    boolean res = productBo.deleteProduct(selectProduct);

                    if (res){
                        AlertMessage.getInstance().informerAlert(AlertType.SUCCESS, "Product Delete success");
                        refreshOnAction();
                    }
                    else AlertMessage.getInstance().informerAlert(AlertType.ERROR, "Product Delete fail");
                }
            });
        }
    }

    @FXML
    void refreshOnAction() {
        txtSearch.setText("");
        txtPrice.setText("");
        txtName.setText("");
        txtQty.setText("");
        txtPrice.setText("");
        txtSupplierId.setText("");
        cmbCategory.setValue(null);
        cmbCategory.setPromptText("Select Category");
        cmbSize.setValue(null);
        cmbSize.setPromptText("Select Size");
        imageBox.setImage(null);
        loadTable();
        url=null;
        selectProduct=null;
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
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.setIconified(true);
    }
}
