package edu.icet.project.controller;

import edu.icet.project.bo.BoFactory;
import edu.icet.project.bo.custom.UserBo;
import edu.icet.project.util.BoType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import javafx.application.Platform;
import javafx.stage.StageStyle;
import java.util.Timer;
import java.util.TimerTask;

public class SplashController {

    @FXML
    private ImageView imageView;

    private final UserBo userBo = BoFactory.getInstance().getBo(BoType.USER);

    public void start(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    userBo.getAll(); // for hibernate start

                    Stage thisStage = (Stage) imageView.getScene().getWindow();
                    thisStage.close();

                    try {
                        Stage stage = new Stage();
                        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/Login.fxml"))));
                        stage.getIcons().add(new Image("images/icons/Logo.png"));
                        scene.setFill(Color.TRANSPARENT);
                        stage.initStyle(StageStyle.TRANSPARENT);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 1000); // 1000 milliseconds delay
    }
}
