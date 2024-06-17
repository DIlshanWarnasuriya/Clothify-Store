package edu.icet.project.controller;

import edu.icet.project.dto.User;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class HomeController {

    @FXML
    private Pane AdminPane;

    @FXML
    private Pane UserPane;

    @FXML
    private BarChart<?, ?> chart;

    @FXML
    private Label lblUserName;

    @FXML
    private Label lblUserType;

    @FXML
    private PieChart pychart;

    @FXML
    private Circle userImage;

    public void setUser(User user){
        userImage.setFill(new ImagePattern(new Image(user.getImageUrl())));
        lblUserName.setText(user.getName());
        lblUserType.setText(user.getUserType());

        if (user.getUserType().equals("Admin")){
            UserPane.setVisible(false);
            AdminPane.setVisible(true);
        }
        else{
            UserPane.setVisible(true);
            AdminPane.setVisible(false);
        }
    }


}
