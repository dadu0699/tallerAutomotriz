package org.didierdominguez.view.customer;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.didierdominguez.bean.*;
import org.didierdominguez.controller.ControllerAttentionQueue;
import org.didierdominguez.controller.ControllerPriorityQueue;
import org.didierdominguez.controller.ControllerServiceFinished;
import org.didierdominguez.list.Queue.QueueNode;
import org.didierdominguez.util.ScreenSize;

import java.util.ArrayList;

public class ViewAutomobileProgress extends Stage {
    private static ViewAutomobileProgress instance;
    private TableView tableViewPriorityQueue;
    private TableView tableViewServiceQueue;
    private TableView tableViewServiceFinished;
    private ObservableList observableListPriorityQueue;
    private ObservableList observableListServiceQueue;
    private ObservableList observableListServiceFinished;
    private Customer customer;

    private ViewAutomobileProgress() {
    }

    public static ViewAutomobileProgress getInstance() {
        if (instance == null) {
            instance = new ViewAutomobileProgress();
        }
        return instance;
    }

    private void updateObservableListPriorityQueue() {
        ArrayList<Order> arrayListPriorityQueue = new ArrayList<>();
        QueueNode auxiliaryNode = ControllerPriorityQueue.getInstance().getPriorityQueue().getfirstNode();
        while (auxiliaryNode != null) {
            if (((Order) auxiliaryNode.getObject()).getCustomer() == customer) {
                arrayListPriorityQueue.add((Order) auxiliaryNode.getObject());
            }
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (observableListPriorityQueue != null) {
            observableListPriorityQueue.clear();
        }
        observableListPriorityQueue = FXCollections.observableArrayList(arrayListPriorityQueue);
    }

    private void updateObservableListServiceQueue() {
        ArrayList<Order> arrayListServiceQueue = new ArrayList<>();
        QueueNode auxiliaryNode = ControllerAttentionQueue.getInstance().getAttentionQueue().getfirstNode();
        while (auxiliaryNode != null) {
            if (((Order) auxiliaryNode.getObject()).getCustomer() == customer) {
                arrayListServiceQueue.add((Order) auxiliaryNode.getObject());
            }
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (observableListServiceQueue != null) {
            observableListServiceQueue.clear();
        }
        observableListServiceQueue = FXCollections.observableArrayList(arrayListServiceQueue);
    }

    private void updateObservableListServiceFinished() {
        ArrayList<Order> arrayListServiceFinished = new ArrayList<>();
        QueueNode auxiliaryNode = ControllerServiceFinished.getInstance().getServiceFinishedQueue().getfirstNode();
        while (auxiliaryNode != null) {
            if (((Order) auxiliaryNode.getObject()).getCustomer() == customer) {
                arrayListServiceFinished.add((Order) auxiliaryNode.getObject());
            }
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (observableListServiceFinished != null) {
            observableListServiceFinished.clear();
        }
        observableListServiceFinished = FXCollections.observableArrayList(arrayListServiceFinished);
    }

    public void updateTableViewItemsPriorityQueue() {
        updateObservableListPriorityQueue();
        tableViewPriorityQueue.setItems(observableListPriorityQueue);
    }

    public void updateTableViewItemsServiceQueue() {
        updateObservableListServiceQueue();
        tableViewServiceQueue.setItems(observableListServiceQueue);
    }

    public void updateTableViewServiceFinished() {
        updateObservableListServiceFinished();
        tableViewServiceFinished.setItems(observableListServiceFinished);
    }

    public VBox getView(Customer customer) {
        VBox vBox = new VBox();
        GridPane gridPaneTitle = new GridPane();
        this.customer = customer;

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPaneTitle.setVgap(10);
        gridPaneTitle.setPadding(new Insets(20));
        // gridPaneTitle.setGridLinesVisible(true);
        gridPaneTitle.setMinWidth(x / 2);
        gridPaneTitle.setPrefSize(x, y / 8);
        vBox.setPrefSize(x, y);

        Text textTitle = new Text("PROGRESO AUTOMOVILES");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPaneTitle.add(textTitle, 0, 0);

        HBox hBoxPanels = new HBox();
        hBoxPanels.setPrefSize(x, 7 * y / 8);
        hBoxPanels.getChildren().addAll(getViewPriorityQueue(), getViewService());

        vBox.getChildren().addAll(gridPaneTitle, hBoxPanels);
        return vBox;
    }

    private GridPane getViewPriorityQueue() {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(0, 10, 20, 20));
        gridPane.setPrefSize(x, y);

        Text textTitle = new Text("COLA DE ESPERA");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(20));
        gridPane.add(textTitle, 0, 0);

        TableColumn<Order, Integer> columnID = new TableColumn<>("ID");
        columnID.setPrefWidth(50);
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Order, Car> columnCar = new TableColumn<>("CARRO");
        columnCar.setPrefWidth(x/2);
        columnCar.setCellValueFactory(new PropertyValueFactory<>("car"));

        updateObservableListPriorityQueue();
        tableViewPriorityQueue = new TableView<>(observableListPriorityQueue);
        tableViewPriorityQueue.getColumns().addAll(columnID, columnCar);
        tableViewPriorityQueue.setPrefSize(x, 7 * y / 8);

        gridPane.add(tableViewPriorityQueue, 0, 1);
        return gridPane;
    }

    private GridPane getViewService() {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(5, 20, 20, 10));
        gridPane.setPrefSize(x, y);

        Text textTitle = new Text("AUTOMOVILES EN SERVICIO");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(20));
        gridPane.add(textTitle, 0, 0);

        TableColumn<Order, Integer> columnID = new TableColumn<>("ID");
        columnID.setPrefWidth(50);
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Order, Car> columnCar = new TableColumn<>("CARRO");
        columnCar.setPrefWidth(x/6);
        columnCar.setCellValueFactory(new PropertyValueFactory<>("car"));
        TableColumn<Order, Employee> columnEmployee = new TableColumn<>("MECÁNICO");
        columnEmployee.setPrefWidth(x/6);
        columnEmployee.setCellValueFactory(new PropertyValueFactory<>("employee"));

        updateObservableListServiceQueue();
        tableViewServiceQueue = new TableView<>(observableListServiceQueue);
        tableViewServiceQueue.getColumns().addAll(columnID, columnCar, columnEmployee);
        tableViewServiceQueue.setPrefSize(x, 7 * y / 8);
        gridPane.add(tableViewServiceQueue, 0, 1);

        Text textTitleC = new Text("AUTOMOVILES FINALIZADOS");
        textTitleC.getStyleClass().add("textTitle");
        textTitleC.setFont(new Font(20));
        gridPane.add(textTitleC, 0, 2);

        TableColumn<Order, Integer> columnIDF = new TableColumn<>("ID");
        columnIDF.setPrefWidth(50);
        columnIDF.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Order, Car> columnCarF = new TableColumn<>("CARRO");
        columnCarF.setPrefWidth(x/8);
        columnCarF.setCellValueFactory(new PropertyValueFactory<>("car"));
        TableColumn<Order, Employee> columnEmployeeF = new TableColumn<>("MECÁNICO");
        columnEmployeeF.setPrefWidth(x/8);
        columnEmployeeF.setCellValueFactory(new PropertyValueFactory<>("employee"));
        TableColumn<Order, Service> columnService = new TableColumn<>("SERVICIO");
        columnService.setPrefWidth(x/8);
        columnService.setCellValueFactory(new PropertyValueFactory<>("service"));

        updateObservableListServiceFinished();
        tableViewServiceFinished = new TableView<>(observableListServiceFinished);
        tableViewServiceFinished.getColumns().addAll(columnIDF, columnCarF, columnEmployeeF, columnService);
        tableViewServiceFinished.setPrefSize(x, 7 * y / 8);
        gridPane.add(tableViewServiceFinished, 0, 3);

        return gridPane;
    }
}
