package lr1.form_op13;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Window.setStage(stage);
        Window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}