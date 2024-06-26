package edu.icet.project.controller;

import com.jfoenix.controls.JFXTextField;
import edu.icet.project.bo.BoFactory;
import edu.icet.project.bo.custom.OrdersBo;
import edu.icet.project.dto.Orders;
import edu.icet.project.dto.OrdersDetails;
import edu.icet.project.dto.User;
import edu.icet.project.dto.table.OrderDetailTable;
import edu.icet.project.dto.table.OrderTable;
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
import java.text.SimpleDateFormat;
import java.util.*;

public class OrdersController implements Initializable {
    @FXML
    private TableView<OrderDetailTable> orderDetailTable;
    @FXML
    private TableView<OrderTable> orderTable;
    @FXML
    private TableColumn<?, ?> colCustomerId;
    @FXML
    private TableColumn<?, ?> colDate;
    @FXML
    private TableColumn<?, ?> colImage;
    @FXML
    private TableColumn<?, ?> colOrderDetailStatus;
    @FXML
    private TableColumn<?, ?> colOrderId;
    @FXML
    private TableColumn<?, ?> colOrderStatus;
    @FXML
    private TableColumn<?, ?> colPaymentMethod;
    @FXML
    private TableColumn<?, ?> colProductId;
    @FXML
    private TableColumn<?, ?> colQty;
    @FXML
    private TableColumn<?, ?> colTotal;
    @FXML
    private TableColumn<?, ?> colUserId;
    @FXML
    private JFXTextField txtSearch;
    @FXML
    private Circle userImage;
    @FXML
    private Label lblUserName;
    @FXML
    private Label lblUserType;

    private final OrdersBo ordersBo = BoFactory.getInstance().getBo(BoType.ORDERS);
    private Orders selectOrder = null;
    private OrdersDetails selectOrderProduct = null;

    private User loggedUser;

    public void setUser(User user){
        loggedUser = user;

        userImage.setFill(new ImagePattern(new Image(user.getImageUrl())));
        lblUserName.setText(user.getName());
        lblUserType.setText(user.getUserType());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPaymentMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        colOrderStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colImage.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colOrderDetailStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadOrderTable();
        selectOrderOnAction();
        selectOrderProductOnAction();
    }

    // Load all orders to table
    private void loadOrderTable(){
        ObservableList<OrderTable> list = FXCollections.observableArrayList();

        for (Orders orders : ordersBo.getAllOrders()) {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
            list.add(new OrderTable(orders.getId(), orders.getCustomerId(), orders.getUserId(), date, orders.getPaymentMethod(), orders.getStatus()));
        }
        FXCollections.reverse(list);
        orderTable.setItems(list);
    }


    // select order form order table
    private void selectOrderOnAction(){
        orderTable.getSelectionModel().selectedItemProperty().addListener((observableValue, orderTable1, select) -> {
            if (select!=null){
                selectOrder = ordersBo.searchOrderById(select.getId());

                ObservableList<OrderDetailTable> list = FXCollections.observableArrayList();
                for (OrdersDetails items : ordersBo.searchAllOrderProductByOrderId(select.getId())){
                    ImageView imageView = new ImageView(new Image(items.getImageUrl()));
                    imageView.setFitHeight(50);
                    imageView.setFitWidth(50);

                    list.add(new OrderDetailTable(items.getId(), imageView, items.getProductId(), items.getQty(), items.getTotal(), items.getStatus()));
                }
                orderDetailTable.setItems(list);
            }
        });
    }

    // select order product form order product table
    private void selectOrderProductOnAction(){
        orderDetailTable.getSelectionModel().selectedItemProperty().addListener((observableValue, orderDetailTable1, select) -> {
            if (select!=null){
                selectOrderProduct = ordersBo.searchOrderProductById(select.getId());
            }
        });
    }

    // Search Button Action Event
    @FXML
    void searchOnAction() {
        String data = txtSearch.getText();
        if (data.isEmpty()){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "please enter data to search");
        }
        else {
            ObservableList<OrderTable> list = FXCollections.observableArrayList();

            for (Orders orders : ordersBo.searchOrder(data)) {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(orders.getDate());
                list.add(new OrderTable(orders.getId(), orders.getCustomerId(), orders.getUserId(), date, orders.getPaymentMethod(), orders.getStatus()));
            }
            FXCollections.reverse(list);
            orderTable.setItems(list);
        }
    }

    // Delete order Button Action Event
    @FXML
    void deleteOrderOnAction() {
        if(selectOrder == null){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "please select Order");
        }
        else if(selectOrder.getStatus().equals("return")){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "this order is already deleted");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Confirmation Dialog");
            alert.setContentText("Are you delete this Order");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            ImageView icon = new ImageView(new Image("images/icons/WARNING.png"));
            alert.setGraphic(icon);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    // selected order
                    Orders order = ordersBo.searchOrderById(selectOrder.getId());
                    order.setStatus("return");

                    // order products list by order id
                    List<OrdersDetails> ordersDetailsList = new ArrayList<>();
                    for (OrdersDetails item : ordersBo.searchAllOrderProductByOrderId(selectOrder.getId())){
                        item.setStatus("return");
                        ordersDetailsList.add(item);
                    }

                    boolean res = ordersBo.deleteOrder(order, ordersDetailsList);
                    if (res){
                        AlertMessage.getInstance().informerAlert(AlertType.SUCCESS, "Order delete Success");
                        refreshOnAction();
                    }
                    else AlertMessage.getInstance().informerAlert(AlertType.ERROR, "Order delete fail");
                }
            });
        }
    }

    // Delete ordered product Button Action Event
    @FXML
    void deleteProductOnAction() {
        if(selectOrderProduct == null){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "please select Order product");
        }
        else if(selectOrderProduct.getStatus().equals("return")){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "this order product is already deleted");
        }
        else if(ordersBo.searchAllOrderProductByOrderId(selectOrder.getId()).size() == 1){
            AlertMessage.getInstance().informerAlert(AlertType.WARNING, "you cant delete this product. please delete this orders");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Confirmation Dialog");
            alert.setContentText("Are you delete this ordered product");
            alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            ImageView icon = new ImageView(new Image("images/icons/WARNING.png"));
            alert.setGraphic(icon);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    // selected order
                    Orders order = ordersBo.searchOrderById(selectOrder.getId());
                    order.setStatus("changed");

                    // selected order products
                    OrdersDetails orderProduct = ordersBo.searchOrderProductById(selectOrderProduct.getId());
                    orderProduct.setStatus("return");
                    List<OrdersDetails> list = Collections.singletonList(orderProduct);

                    boolean res = ordersBo.deleteOrder(order, list);
                    if (res){
                        AlertMessage.getInstance().informerAlert(AlertType.SUCCESS, "Ordered product delete Success");
                        refreshOnAction();
                    }
                    else AlertMessage.getInstance().informerAlert(AlertType.ERROR, "Ordered product delete fail");
                }
            });
        }
    }

    // Refresh Button Action Event
    @FXML
    void refreshOnAction() {
        orderDetailTable.setItems(null);
        selectOrder = null;
        selectOrderProduct = null;
        loadOrderTable();
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

    // Minimize Button Action Event
    @FXML
    void minimizeOnAction() {
        Stage stage = (Stage) txtSearch.getScene().getWindow();
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
    void placeOrderPageNavigation() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PlaceOrder.fxml"));
        Parent root = loader.load();
        PlaceOrderController controller = loader.getController();
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
