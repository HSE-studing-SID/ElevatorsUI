package sample;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage ps) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Elevators.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Elevators");
        stage.show();
        stage.setOnCloseRequest(windowEvent -> System.exit(0));
    }

    public static void main(String[] args) {
//        Controller controller = Controller.shared;
//        Thread thread = new Thread(controller);
//        thread.start();
        launch(args);
    }
}
