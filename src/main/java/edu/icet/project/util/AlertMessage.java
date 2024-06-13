package edu.icet.project.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AlertMessage {
    private static AlertMessage instance;
    private AlertMessage(){}
    private final Alert alert = new Alert(Alert.AlertType.NONE);

    public static AlertMessage getInstance(){
        return instance==null ? instance=new AlertMessage() : instance;
    }

    public void informerAlert(AlertType type, String message){
        alert.setTitle("Informer Dialog");
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.setContentText(message);
        ImageView icon = new ImageView(new Image("images/icons/"+ type +".png"));
        alert.setGraphic(icon);
        alert.show();
    }
}
