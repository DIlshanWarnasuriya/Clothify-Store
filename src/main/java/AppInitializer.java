import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Objects;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws Exception {

        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("view/Orders.fxml"))));
        stage.getIcons().add(new Image("images/icons/Logo.png"));
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        //stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }
}
