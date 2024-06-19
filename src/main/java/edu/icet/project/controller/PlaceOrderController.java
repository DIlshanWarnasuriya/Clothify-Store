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
import edu.icet.project.dto.User;
import edu.icet.project.dto.table.CartTable;
import edu.icet.project.util.AlertMessage;
import edu.icet.project.util.AlertType;
import edu.icet.project.util.BoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class PlaceOrderController implements Initializable {

    @FXML
    private Circle userImage;
    @FXML
    private Label lblUserName;
    @FXML
    private Label lblUserType;

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

    private User loggedUser;

    public void setUser(User user){
        loggedUser = user;

        userImage.setFill(new ImagePattern(new Image(user.getImageUrl())));
        lblUserName.setText(user.getName());
        lblUserType.setText(user.getUserType());
    }

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

    // set payment methods to paymentMethod Combo Box
    private void setPaymentMethodToComboBox() {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("Cash Payment");
        list.add("Card Payment");
        cmbPaymentMethod.setItems(list);
    }

    // Load Product to cart
    private void loadTable() {
        cartTable.setItems(cartList);
        double netTotal = 0;

        for (CartTable product : cartList) {
            netTotal += product.getTotal();
        }
        lblTotal.setText(Double.toString(netTotal));
    }

    // select product form order table
    private void selectProductInCart() {
        cartTable.getSelectionModel().selectedItemProperty().addListener((observableValue, cartTable1, select) -> {
            if (select != null) {
                selectedProduct = select;
            }
        });
    }

    // Search Button Action Event
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

                    CartTable cartProduct = getProductFromCart(id); // search product form cart
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

    // search product from cart
    private CartTable getProductFromCart(int id) {
        for (CartTable product : cartList) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    // Add Product to cart Button Action Event
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

    // Remove Product from cart Button Action Event
    @FXML
    void removeFromCartOnAction() {
        if (selectedProduct == null) {
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "Please select product from cart");
        } else {
            cartList.remove(selectedProduct);
            refreshOnAction();
        }
    }

    // Place Order Button Action Event
    @FXML
    void placeOrderOnAction() {

        try{
            Integer orderId = ordersBo.getAllOrders().size() + 1;
            Integer customerId = Integer.parseInt(txtCustomerId.getText());
            String paymentMethod = cmbPaymentMethod.getValue();
            Date date = new Date();

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

    // Refresh Button Action Event
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

    // Close Button Action Event
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

    // Close Button Action Event
    @FXML
    void minimizeOnAction() {
        Stage stage = (Stage) txtQty.getScene().getWindow();
        stage.setIconified(true);
    }


    // ------------------------------- navigation buttons ---------------------------------------

    @FXML
    void homePageNavigation() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Home.fxml"));
        Parent root = loader.load();
        HomeController controller = loader.getController();
        controller.setUser(loggedUser);

        Stage stage = (Stage) lblUserName.getScene().getWindow();
        stage.getIcons().add(new Image("images/icons/Logo.png"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }

    @FXML
    void productPageNavigation() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Product.fxml"));
        Parent root = loader.load();
        ProductController controller = loader.getController();
        controller.setUser(loggedUser);

        Stage stage = (Stage) lblUserName.getScene().getWindow();
        stage.getIcons().add(new Image("images/icons/Logo.png"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }
    @FXML
    void orderPageNavigation() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Orders.fxml"));
        Parent root = loader.load();
        OrdersController controller = loader.getController();
        controller.setUser(loggedUser);

        Stage stage = (Stage) lblUserName.getScene().getWindow();
        stage.getIcons().add(new Image("images/icons/Logo.png"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }
    @FXML
    void customerPageNavigation() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Customer.fxml"));
        Parent root = loader.load();
        CustomerController controller = loader.getController();
        controller.setUser(loggedUser);

        Stage stage = (Stage) lblUserName.getScene().getWindow();
        stage.getIcons().add(new Image("images/icons/Logo.png"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }
    @FXML
    void supplierPageNavigation() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Supplier.fxml"));
        Parent root = loader.load();
        SupplierController controller = loader.getController();
        controller.setUser(loggedUser);

        Stage stage = (Stage) lblUserName.getScene().getWindow();
        stage.getIcons().add(new Image("images/icons/Logo.png"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }
    @FXML
    void profilePageNavigation() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Profile.fxml"));
        Parent root = loader.load();
        ProfileController controller = loader.getController();
        controller.setUser(loggedUser);

        Stage stage = (Stage) lblUserName.getScene().getWindow();
        stage.getIcons().add(new Image("images/icons/Logo.png"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }
}
