package edu.icet.project.controller;

import edu.icet.project.bo.BoFactory;
import edu.icet.project.bo.custom.HomeBo;
import edu.icet.project.bo.custom.ProductBo;
import edu.icet.project.dto.OrdersDetails;
import edu.icet.project.dto.User;
import edu.icet.project.util.BoType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Pane adminPane;
    @FXML
    private ImageView bestSellingProductImage;
    @FXML
    private Label lblAdminDate;
    @FXML
    private Label lblAdminTime;
    @FXML
    private Label lblBestSellingProductQty;
    @FXML
    private Label lblCustomerCount;
    @FXML
    private Label lblIncome;
    @FXML
    private Label lblProductCountAdmin;
    @FXML
    private Label lblUserCount;
    @FXML
    private Label lblUserName;
    @FXML
    private Label lblUserType;
    @FXML
    private PieChart pieChart;
    @FXML
    private Label soldItemAdmin;
    @FXML
    private Circle userImage;
    @FXML
    private Pane userPane;
    @FXML
    private BarChart<String, Double> barChart;
    @FXML
    private Label productStokeUser;
    @FXML
    private Label customerCountUser;
    @FXML
    private Label supplierCount;
    @FXML
    private Label soldItemUser;
    @FXML
    private Label lblUserDate;
    @FXML
    private Label lblUserTime;
    @FXML
    private ImageView kidImage;
    @FXML
    private Label kidProductName;
    @FXML
    private Label kidQty;
    @FXML
    private ImageView menImage;
    @FXML
    private Label menProductName;
    @FXML
    private Label menQty;
    @FXML
    private ImageView womenImage;
    @FXML
    private Label womenProductNAme;
    @FXML
    private Label womenQty;


    private final HomeBo homeBo = BoFactory.getInstance().getBo(BoType.HOME);
    private final ProductBo productBo = BoFactory.getInstance().getBo(BoType.PRODUCT);
    private User loggedUser;

    public void setUser(User user){
        loggedUser = user;

        userImage.setFill(new ImagePattern(new Image(user.getImageUrl())));
        lblUserName.setText(user.getName());
        lblUserType.setText(user.getUserType());

        if (user.getUserType().equals("Admin")){
            userPane.setVisible(false);
            adminPane.setVisible(true);
        }
        else{
            userPane.setVisible(true);
            adminPane.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDateAndTime();

        // admin view
        lblProductCountAdmin.setText(homeBo.getProductCount() + " Product in stock");
        lblCustomerCount.setText(homeBo.getCustomersCount() + " in list");
        lblUserCount.setText(homeBo.getUserCount() + " users in list");
        soldItemAdmin.setText(homeBo.getTodayOrderProductCount() + " item sold today");
        lblIncome.setText("Rs. " + homeBo.getIncome() + "/=");
        bestSellingProduct();
        setBarChart();
        setPieChart();

        //User View
        soldItemUser.setText(homeBo.getTodayOrderProductCount() + " item sold today");
        productStokeUser.setText(homeBo.getProductCount() + " Product in stock");
        customerCountUser.setText(homeBo.getCustomersCount() + " in customers list");
        supplierCount.setText(homeBo.getSupplierCount() + " suppliers in list");
        setBestSellingMen();
        setBestSellingWomen();
        setBestSellingKis();

    }

    //----------------------------------------------- Admin View--------------------------------------------------------

    // Bar chart manage
    private void setBarChart(){
        int[] qty = homeBo.getMonthSale();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName(null);

        for (int i=0; i<12; i++){
            series.getData().add(new XYChart.Data(months[i], qty[i]));
        }
        barChart.getData().add(series);
    }

    // pie chart manage
    private void setPieChart(){
        int[] qty = homeBo.getYearSale();
        String[] years = new String[3];

        int nowYer = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
        for (int i=2; i>=0; i--) {
            years[i] = (nowYer-i) + "";
        }

        ObservableList<PieChart.Data> chart = FXCollections.observableArrayList();
        for (int i=0; i<3; i++){
            chart.add(new PieChart.Data(years[i], qty[i]));
        }
        pieChart.setData(chart);
        pieChart.setTitle("Years");
    }

    // show best Selling product
    private void bestSellingProduct(){
        List<OrdersDetails> ordersDetails = homeBo.bestSellingProduct();
        if (!ordersDetails.isEmpty()){
            OrdersDetails bestProduct = ordersDetails.get(ordersDetails.size()-1);
            bestSellingProductImage.setImage(new Image(bestProduct.getImageUrl()));
            lblBestSellingProductQty.setText(bestProduct.getQty() + " items");
        }
    }


    //----------------------------------------------- User View--------------------------------------------------------

    // show best Selling product Men
    private void setBestSellingMen(){
        List<OrdersDetails> ordersDetails = homeBo.bestSellingMenProduct();

        if (!ordersDetails.isEmpty()){
            OrdersDetails bestProduct = ordersDetails.get(ordersDetails.size()-1);
            menImage.setImage(new Image(bestProduct.getImageUrl()));
            menProductName.setText(productBo.searchById(bestProduct.getProductId()).getName());
            menQty.setText(bestProduct.getQty() + " items");
        }
    }

    // show best Selling product Women
    private void setBestSellingWomen(){
        List<OrdersDetails> ordersDetails = homeBo.bestSellingWomenProduct();

        if (!ordersDetails.isEmpty()){
            OrdersDetails bestProduct = ordersDetails.get(ordersDetails.size()-1);
            womenImage.setImage(new Image(bestProduct.getImageUrl()));
            womenProductNAme.setText(productBo.searchById(bestProduct.getProductId()).getName());
            womenQty.setText(bestProduct.getQty() + " items");
        }
    }

    // show best Selling product Kid
    private void setBestSellingKis(){
        List<OrdersDetails> ordersDetails = homeBo.bestSellingKidProduct();

        if (!ordersDetails.isEmpty()){
            OrdersDetails bestProduct = ordersDetails.get(ordersDetails.size()-1);
            kidImage.setImage(new Image(bestProduct.getImageUrl()));
            kidProductName.setText(productBo.searchById(bestProduct.getProductId()).getName());
            kidQty.setText(bestProduct.getQty() + " items");
        }
    }


    //------------------------------------------------------------------------------------------------------------------

    // set Date and Time
    private void setDateAndTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        lblAdminDate.setText(format.format(date));
        lblUserDate.setText(format.format(date));

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
            LocalTime time = LocalTime.now();
            if (time.getHour() < 10) {
                lblAdminTime.setText("0" + time.getHour() + ":" + time.getMinute() );
                lblUserTime.setText("0" + time.getHour() + ":" + time.getMinute() );
            }
            else {
                lblAdminTime.setText(time.getHour() + ":" + time.getMinute() );
                lblUserTime.setText(time.getHour() + ":" + time.getMinute() );
            }

        }), new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    // Refresh Button Action Event
    @FXML
    void refreshOnAction() {
        // admin view
        lblProductCountAdmin.setText(homeBo.getProductCount() + " Product in stock");
        lblCustomerCount.setText(homeBo.getCustomersCount() + " in list");
        lblUserCount.setText(homeBo.getUserCount() + " users in list");
        soldItemAdmin.setText(homeBo.getTodayOrderProductCount() + " item sold today");
        lblIncome.setText("Rs. " + homeBo.getIncome() + "/=");
        bestSellingProduct();
        setBarChart();
        setPieChart();

        //User View
        soldItemUser.setText(homeBo.getTodayOrderProductCount() + " item sold today");
        productStokeUser.setText(homeBo.getProductCount() + " Product in stock");
        customerCountUser.setText(homeBo.getCustomersCount() + " in customers list");
        supplierCount.setText(homeBo.getSupplierCount() + " suppliers in list");
        setBestSellingMen();
        setBestSellingWomen();
        setBestSellingKis();
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
        Stage stage = (Stage) lblUserName.getScene().getWindow();
        stage.setIconified(true);
    }


    // ------------------------------- navigation buttons ---------------------------------------

    @FXML
    void reportOnAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Reports.fxml"));
        Parent root = loader.load();
        ReportController controller = loader.getController();
        controller.setUser(loggedUser);

        Stage stage = (Stage) lblUserName.getScene().getWindow();
        stage.getIcons().add(new Image("images/icons/Logo.png"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.show();
    }

    @FXML
    void userOnAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/User.fxml"));
        Parent root = loader.load();
        UserController controller = loader.getController();
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
