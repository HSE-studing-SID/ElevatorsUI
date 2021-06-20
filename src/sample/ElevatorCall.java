package sample;

public class ElevatorCall {
    private int fromFloor, toFloor;
    private Controller controller;

    public ElevatorCall(int fromFloor, int toFloor, Controller controller) {
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.controller = controller;
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public int getToFloor() {
        return toFloor;
    }

    public Elevator submitCall() {
        return controller.selectElevator(this);
    }

    public Controller getController() {
        return controller;
    }
}
