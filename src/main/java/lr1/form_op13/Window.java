package lr1.form_op13;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class Window {
    private static final FXMLLoader fxmlLoader;
    private static final Scene scene;
    static {
        fxmlLoader = new FXMLLoader(Main.class.getResource("mainForm.fxml"));
        try {
            scene = new Scene(fxmlLoader.load(),840,579);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static Stage stage;

    private Window(){}

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stg) {
        if (stage != null)
            return;
        stage = stg;
        stage.setScene(scene);
        stage.setTitle("Форма ОП-13");
        stage.setResizable(false);
    }

    public static void show(){
        if (stage == null)
            return;
        stage.show();
    }

    public static void close(){
        if (stage == null)
            return;
        stage.close();
    }
}
