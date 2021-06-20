package sample;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.util.Random;

public class ElevatorController {
    @FXML private GridPane elevatorsPane;

    public GridPane getElevatorsPane() {
        return elevatorsPane;
    }

    @FXML
    void initialize() throws InterruptedException {
        for (int i = 0; i < 4; i++) {
            elevatorsPane.add(Elevator.getElevatorPane(), i, 29);
        }

        Controller controller = new Controller(4, 30, elevatorsPane);
        Thread thread = new Thread(controller);
        thread.start();

        new Thread(() -> {
            int floorsCount = Controller.floorsQuantity;
            Random gen = new Random();
            int a, b;
            for (int i = 0; i < 15; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                a = gen.nextInt(floorsCount);
                b = gen.nextInt(floorsCount);
                System.out.println("From "+a+" to "+b);
                ElevatorCall call = new ElevatorCall(a, b, controller);
                call.submitCall();
            }
        }).start();
    }
}
