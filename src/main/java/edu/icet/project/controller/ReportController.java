package edu.icet.project.controller;

import edu.icet.project.bo.BoFactory;
import edu.icet.project.bo.custom.ReportBo;
import edu.icet.project.dto.User;
import edu.icet.project.util.BoType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReportController implements Initializable {

    @FXML
    private Circle userImage;
    @FXML
    private Label lblUserName;
    @FXML
    private Label lblUserType;

    @FXML
    private Label todayIncome;
    @FXML
    private Label monthIncome;
    @FXML
    private Label annualIncome;

    @FXML
    private Label todayOrder;
    @FXML
    private Label monthOrder;
    @FXML
    private Label annualOrder;

    @FXML
    private Label todaySoldProduct;
    @FXML
    private Label monthSoldProduct;
    @FXML
    private Label annualSoldProduct;

    @FXML
    private Label todaySoldProductMen;
    @FXML
    private Label monthSoldProductMen;
    @FXML
    private Label annualSoldProductMen;

    @FXML
    private Label todaySoldProductWomen;
    @FXML
    private Label monthSoldProductWomen;
    @FXML
    private Label annualSoldProductWomen;

    @FXML
    private Label todaySoldProductKid;
    @FXML
    private Label monthSoldProductKid;
    @FXML
    private Label annualSoldProductKid;

    @FXML
    private Label returnOrderToday;
    @FXML
    private Label returnOrderMonth;
    @FXML
    private Label returnOrderAnnual;

    @FXML
    private Label changedOrderToday;
    @FXML
    private Label changedOrderMonth;
    @FXML
    private Label changedOrderAnnual;

    @FXML
    private Label returnMenToday;
    @FXML
    private Label returnMenMonth;
    @FXML
    private Label returnMenAnnual;

    @FXML
    private Label returnWomenToday;
    @FXML
    private Label returnWomenMonth;
    @FXML
    private Label returnWomenAnnual;

    @FXML
    private Label returnKidToday;
    @FXML
    private Label returnKidMonth;
    @FXML
    private Label returnKidAnnual;

    @FXML
    private Label purchasedProductToday;
    @FXML
    private Label purchasedProductMonth;
    @FXML
    private Label purchasedProductAnnual;


    private final ReportBo reportBo = BoFactory.getInstance().getBo(BoType.REPORT);
    private User loggedUser;

    public void setUser(User user){
        loggedUser = user;

        userImage.setFill(new ImagePattern(new Image(user.getImageUrl())));
        lblUserName.setText(user.getName());
        lblUserType.setText(user.getUserType());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDetails();
    }

    private void setDetails(){
        todayIncome.setText("Rs. " + reportBo.todayIncome());
        monthIncome.setText("Rs. " + reportBo.monthIncome());
        annualIncome.setText("Rs. " + reportBo.annualIncome());

        todayOrder.setText(reportBo.todayOrdersTotal() + " Orders");
        monthOrder.setText(reportBo.monthOrdersTotal() + " Orders");
        annualOrder.setText(reportBo.annualOrdersTotal() + " Orders");

        todaySoldProduct.setText(reportBo.todaySoldProduct() + " items");
        monthSoldProduct.setText(reportBo.monthSoldProduct() + " items");
        annualSoldProduct.setText(reportBo.annualSoldProduct() + " items");

        todaySoldProductMen.setText(reportBo.todaySoldMen() + " items");
        monthSoldProductMen.setText(reportBo.monthSoldMen() + " items");
        annualSoldProductMen.setText(reportBo.annualSoldMen() + " items");

        todaySoldProductWomen.setText(reportBo.todaySoldWomen() + " items");
        monthSoldProductWomen.setText(reportBo.monthSoldWomen() + " items");
        annualSoldProductWomen.setText(reportBo.annualSoldWomen() + " items");

        todaySoldProductKid.setText(reportBo.todaySoldKid() + " items");
        monthSoldProductKid.setText(reportBo.monthSoldKid() + " items");
        annualSoldProductKid.setText(reportBo.annualSoldKid() + " items");

        returnOrderToday.setText(reportBo.todayOrdersReturn() + " Orders");
        returnOrderMonth.setText(reportBo.monthOrdersReturn() + " Orders");
        returnOrderAnnual.setText(reportBo.annualOrdersReturn() + " Orders");

        changedOrderToday.setText(reportBo.todayOrdersChanged() + " Orders");
        changedOrderMonth.setText(reportBo.monthOrdersChanged() + " Orders");
        changedOrderAnnual.setText(reportBo.annualOrdersChanged() + " Orders");

        returnMenToday.setText(reportBo.todayReturnMen() + " items");
        returnMenMonth.setText(reportBo.monthReturnMen() + " items");
        returnMenAnnual.setText(reportBo.annualReturnMen() + " items");

        returnWomenToday.setText(reportBo.todayReturnWomen() + " items");
        returnWomenMonth.setText(reportBo.monthReturnWomen() + " items");
        returnWomenAnnual.setText(reportBo.annualReturnWomen() + " items");

        returnKidToday.setText(reportBo.todayReturnKid() + " items");
        returnKidMonth.setText(reportBo.monthReturnKid() + " items");
        returnKidAnnual.setText(reportBo.annualReturnKid() + " items");

        purchasedProductToday.setText(reportBo.todayPurchasedProduct() + " Product");
        purchasedProductMonth.setText(reportBo.monthPurchasedProduct() + " Product");
        purchasedProductAnnual.setText(reportBo.annualPurchasedProduct() + " Product");
    }


    // Refresh Button Action Event
    @FXML
    void refreshOnAction() {
        setDetails();
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
        Stage stage = (Stage) lblUserType.getScene().getWindow();
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
