package sample;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.*;
import java.util.stream.StreamSupport;

public class Elevator implements Runnable{

    private boolean isWorking;
    private int id;
    private ElevatorState state;
    private int floor;

    private NavigableSet<Integer> stops;
    public Map<ElevatorState, NavigableSet<Integer>> stopsMap;
    private GridPane elevatorsPane;

    public Elevator(int id, GridPane pane){
        this.id = id;
        elevatorsPane = pane;
        setWorking(true);
    }

    public static Pane getElevatorPane() {
        Line line = new Line();
        line.setStartX(55);
        line.setEndX(55);
        line.setStartY(0);
        line.setEndY(150);

        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: GREY");

        return pane;
    }

    //Getters

    public int getId() {
        return id;
    }

    public ElevatorState getState() {
        return state;
    }

    public int getFloor() {
        return floor;
    }

    public void setState(ElevatorState state) {
        this.state = state;
    }

    public boolean isWorking(){
        return this.isWorking;
    }

    //Setters

    public void setWorking(boolean state){
        this.isWorking = state;

        if(!state){
            setState(ElevatorState.NOT_WORKING);
            this.stops.clear();
        } else {
            setState(ElevatorState.WAITING);
            this.stopsMap = new LinkedHashMap<>();
            Controller.updateElevatorLists(this);
        }

        setFloor(0);
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void move(){
        Iterator<ElevatorState> iter = stopsMap.keySet().iterator();
        int start = getFloor();

        while(iter.hasNext()){
            try {
                state = iter.next();
            } catch (ConcurrentModificationException e) {
                break;
            }

            stops = stopsMap.get(state);
            iter.remove();
            Integer currFlr;
            Integer nextFlr ;

            while (!stops.isEmpty()) {
                if (state.equals(ElevatorState.UP)) {
                    currFlr = stops.pollFirst();
                    nextFlr = stops.higher(currFlr);
                } else if (state.equals(ElevatorState.DOWN)) {
                    currFlr = stops.pollLast();
                    nextFlr = stops.lower(currFlr);
                } else {
                    return;
                }

                floor = currFlr;

                if (nextFlr != null) {
                    createFloorsRow(currFlr, nextFlr);
                } else {
                    setState(ElevatorState.WAITING);
                    Controller.updateElevatorLists(this);
                }

                System.out.println(this);
                moveOnScreen();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createFloorsRow(int initial, int target){
        if(initial == target){
            return;
        }

        if(Math.abs(initial - target) == 1){
            return;
        }

        int step = target - initial < 0 ? -1 : 1;
        while(initial != target){
            initial += step;
            stops.add(initial);
        }
    }

    // todo нужно вызывать ПОСЛЕ шага
    // должен знать текущий этаж и предыдущий шаг
    private void moveOnScreen() {
        if (getState() == ElevatorState.WAITING) {
            return;
        }
        int step = getState() == ElevatorState.UP ? 1 : -1;
        Platform.runLater(() -> {
            elevatorsPane.getChildren().remove(
                    getNodeByRowColumnIndex(29 - getFloor(), id)
            );
            elevatorsPane.add(Elevator.getElevatorPane(), id, 29 - (getFloor() + step));
        });
    }

    private Node getNodeByRowColumnIndex(int row, int column) {
        Node result = null;
        for (Node node : elevatorsPane.getChildren()) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }


    @Override
    public void run() {
        while(true){
            if(isWorking()){
                move();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Elevator ID " + this.id + " | Current floor - " + getFloor() + " | next move - " + getState();
    }
}
