package edu.icet.project.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.icet.project.bo.BoFactory;
import edu.icet.project.bo.custom.CustomerBo;
import edu.icet.project.bo.custom.OrdersBo;
import edu.icet.project.bo.custom.ProductBo;
import edu.icet.project.dto.Orders;
import edu.icet.project.dto.OrdersDetails;
import edu.icet.project.dto.Product;
import edu.icet.project.dto.table.CartTable;
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
import javafx.stage.Stage;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private final OrdersBo ordersBo = BoFactory.getInstance().getBo(BoType.ORDERS);

    private final ObservableList<CartTable> cartList = FXCollections.observableArrayList();
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

        for (CartTable product : cartList) {
            netTotal += product.getTotal();
        }
        lblTotal.setText(Double.toString(netTotal));
    }

    private void selectProductInCart() {
        cartTable.getSelectionModel().selectedItemProperty().addListener((observableValue, cartTable1, select) -> {
            if (select != null) {
                selectedProduct = select;
            }
        });
    }

    @FXML
    void searchOnAction() {
        try {
            int id = Integer.parseInt(txtSearch.getText());
            if (id <= 0) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter valid Product id");
            } else {
                Product product = productBo.searchById(id);
                if (product == null) {
                    AlertMessage.getInstance().informerAlert(AlertType.ERROR, "Product not found. Please check Product id");
                } else {
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
        for (CartTable product : cartList) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    @FXML
    void addToCartOnAction() {
        try {
            int productId = Integer.parseInt(txtSearch.getText());
            int qty = Integer.parseInt(txtQty.getText());

            if (productId <= 0 || qty <= 0) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter valid Product id and qty");
            } else if (productBo.searchById(productId) == null) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter valid Product id");
            } else if (Integer.parseInt(lblAvailable.getText()) < qty) {
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "You have exceeded the available items. check available products");
            } else {
                double total = Double.parseDouble(lblPrice.getText()) * qty;

                CartTable product = getProductFromCart(productId);
                if (product == null){
                    ImageView imageView = new ImageView(new Image(imageBox.getImage().getUrl()));
                    imageView.setFitHeight(50);
                    imageView.setFitWidth(50);

                    CartTable item = new CartTable(imageView, productId, lblName.getText(), lblSize.getText(), qty, total);
                    cartList.add(item);
                    refreshOnAction();
                }
                else{
                    qty += product.getQty();
                    total += product.getTotal();
                    cartList.remove(product);

                    product.setQty(qty);
                    product.setTotal(total);
                    cartList.add(product);
                    refreshOnAction();
                }
            }
        } catch (RuntimeException e) {
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter valid Product id and qty");
        }
    }

    @FXML
    void removeFromCartOnAction() {
        if (selectedProduct == null) {
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please select product from cart");
        } else {
            cartList.remove(selectedProduct);
            refreshOnAction();
        }
    }

    @FXML
    void placeOrderOnAction() {

        try{
            Integer orderId = ordersBo.getAllOrders().size() + 1;
            Integer customerId = Integer.parseInt(txtCustomerId.getText());
            String paymentMethod = cmbPaymentMethod.getValue();
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            if (cartList.isEmpty()){
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please Add product to cart");
            }
            else if(customerBo.searchById(customerId) == null){
                AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Customer Id is wrong. Please enter valid customer Id");
            }
            else{
                Orders orders = new Orders(customerId,2, date, paymentMethod, "placed");
                ArrayList<OrdersDetails> list = new ArrayList<>();
                for (CartTable item : cartList){
                    list.add(new OrdersDetails(orderId, item.getId(), item.getQty(), item.getTotal(), item.getImageUrl().getImage().getUrl(), "placed"));
                }

                boolean res = ordersBo.saveOrder(orders, list);
                if (res){
                    AlertMessage.getInstance().informerAlert(AlertType.SUCCESS, "Placed order");
                    cartList.clear();
                    refreshOnAction();
                    txtCustomerId.setText("");
                }
                else AlertMessage.getInstance().informerAlert(AlertType.ERROR, "Fail");
            }
        }catch (RuntimeException e){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please enter valid customer id and select payment method");
        }
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
        Stage stage = (Stage) txtQty.getScene().getWindow();
        stage.setIconified(true);
    }


}
