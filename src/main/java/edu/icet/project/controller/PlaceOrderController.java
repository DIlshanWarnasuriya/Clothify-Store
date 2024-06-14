package edu.icet.project.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.icet.project.bo.BoFactory;
import edu.icet.project.bo.custom.CustomerBo;
import edu.icet.project.bo.custom.ProductBo;
import edu.icet.project.dto.Product;
import edu.icet.project.dto.table.CartTable;
import edu.icet.project.util.AlertMessage;
import edu.icet.project.util.AlertType;
import edu.icet.project.util.BoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PlaceOrderController implements Initializable {

    @FXML
    private JFXTextField txtSearch;
    @FXML
    private ImageView imageBox;

    @FXML
    private Label lblName;
    @FXML
    private Label lblPrice;
    @FXML
    private Label lblSize;
    @FXML
    private Label lblAvailable;

    @FXML
    private JFXTextField txtCustomerId;
    @FXML
    private JFXTextField txtQty;

    @FXML
    private Label lblTotal;
    @FXML
    private JFXComboBox<String> cmbPaymentMethod;

    @FXML
    private TableView<CartTable> cartTable;
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
    private TableColumn<?, ?> colTotal;

    private final ProductBo productBo = BoFactory.getInstance().getBo(BoType.PRODUCT);
    private final CustomerBo customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);

    private ObservableList<CartTable> cartList = FXCollections.observableArrayList();
    private CartTable selectedProduct = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colImage.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        setPaymentMethodToComboBox();
        selectProductInCart();
    }

    private void setPaymentMethodToComboBox() {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("Cash Payment");
        list.add("Card Payment");
        cmbPaymentMethod.setItems(list);
    }

    private void loadTable() {
        cartTable.setItems(cartList);
        double netTotal = 0;

        if (cartList != null) {
            for (CartTable product : cartList) {
                netTotal += product.getTotal();
            }
            lblTotal.setText(Double.toString(netTotal));
        }
    }

    private void selectProductInCart() {
        cartTable.getSelectionModel().selectedItemProperty().addListener((observableValue, cartTable1, select) -> {
            if (select != null) {
                selectedProduct = select;
            }
        });
    }

    @FXML
    void searchOnAction(MouseEvent event) {
        try {
            int id = Integer.parseInt(txtSearch.getText());
            if (id <= 0) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter valid Product id");
            } else {
                Product product = productBo.searchById(id);
                if (product == null) {
                    AlertMessage.getInstance().informerAlert(AlertType.ERROR, "Product not found. Please check Product id");
                } else {
                    AlertMessage.getInstance().informerAlert(AlertType.SUCCESS, "Product Found");
                    lblName.setText(product.getName());
                    lblPrice.setText(product.getPrice().toString());
                    lblSize.setText(product.getSize());
                    imageBox.setImage(new Image(product.getImageUrl()));
                    txtQty.setText("");

                    CartTable cartProduct = getProductFromCart(id);
                    if (cartProduct == null) {
                        lblAvailable.setText(product.getQty().toString());
                    } else {
                        int cartQty = cartProduct.getQty();
                        int available = product.getQty();
                        lblAvailable.setText(Integer.toString(available - cartQty));
                    }

                }
            }
        } catch (RuntimeException e) {
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter valid Product id");
        }
    }

    private CartTable getProductFromCart(int id) {
        if (cartList != null) {
            for (CartTable product : cartList) {
                if (product.getId() == id) {
                    return product;
                }
            }
        }
        return null;
    }

    @FXML
    void addToCartOnAction(ActionEvent event) {
        try {
            int productId = Integer.parseInt(txtSearch.getText());
            int qty = Integer.parseInt(txtQty.getText());

            if (productId <= 0 || qty <= 0) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter valid Product id and qty");
            } else if (productBo.searchById(productId) == null) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter valid Product id");
            } else if (Integer.parseInt(lblAvailable.getText()) < qty) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "You have exceeded the available items. check available items");
            } else {
                double total = Double.parseDouble(lblPrice.getText()) * qty;
                ImageView imageView = new ImageView(new Image(imageBox.getImage().getUrl()));
                imageView.setFitHeight(50);
                imageView.setFitWidth(50);

                CartTable item = new CartTable(imageView, productId, lblName.getText(), lblSize.getText(), qty, total);
                cartList.add(item);
                loadTable();
                refreshOnAction();
            }
        } catch (RuntimeException e) {
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter valid Product id and qty");
        }
    }

    @FXML
    void deleteFromCartOnAction(ActionEvent event) {
        if (selectedProduct == null) {
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please select product from cart");
        } else {
            cartList.remove(selectedProduct);
            loadTable();
        }
    }

    @FXML
    void placeOrderOnAction(ActionEvent event) {

    }

    @FXML
    void refreshOnAction() {
        txtSearch.setText("");
        txtQty.setText("");
        lblName.setText("");
        lblSize.setText("");
        lblAvailable.setText("");
        lblPrice.setText("");
        imageBox.setImage(null);
        cmbPaymentMethod.setValue(null);
        cmbPaymentMethod.setPromptText("Select pay method");
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
        Stage stage = (Stage) txtQty.getScene().getWindow();
        stage.setIconified(true);
    }


}
