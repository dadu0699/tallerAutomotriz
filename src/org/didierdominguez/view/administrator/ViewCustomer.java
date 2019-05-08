package org.didierdominguez.view.administrator;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.didierdominguez.bean.Customer;
import org.didierdominguez.bean.User;
import org.didierdominguez.controller.ControllerCustomer;
import org.didierdominguez.list.CircularDoubleList.CircularDoubleNode;
import org.didierdominguez.util.Alert;
import org.didierdominguez.util.FileControl;
import org.didierdominguez.util.ScreenSize;

import java.util.ArrayList;

public class ViewCustomer extends Stage {
    private static ViewCustomer instance;
    private HBox hBox;
    private GridPane gridPane;
    private TableView tableView;
    private ObservableList observableList;

    private ViewCustomer() {
    }

    public static ViewCustomer getInstance() {
        if (instance == null) {
            instance = new ViewCustomer();
        }
        return instance;
    }

    public void restartHBox() {
        hBox.getChildren().clear();
        hBox.getChildren().add(gridPane);
    }

    private void updateObservableList() {
        ArrayList<Customer> arrayListCustomer = new ArrayList<>();
        CircularDoubleNode auxiliaryNode = ControllerCustomer.getInstance().getCustomerList().getNode();
        if (auxiliaryNode != null) {
            do {
                arrayListCustomer.add((Customer) auxiliaryNode.getObject());
                auxiliaryNode = auxiliaryNode.getPreviousNode();
            } while (auxiliaryNode != ControllerCustomer.getInstance().getCustomerList().getNode());
        }
        if (observableList != null) {
            observableList.clear();
        }
        observableList = FXCollections.observableArrayList(arrayListCustomer);
    }

    public void updateTableViewItems() {
        updateObservableList();
        tableView.setItems(observableList);
    }

    public HBox getViewCustomer() {
        hBox = new HBox();
        gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));
        // gridPane.setGridLinesVisible(true);
        gridPane.setMinWidth(x / 2);
        gridPane.setPrefSize(x, y);
        hBox.setPrefSize(x, y);

        Text textTitle = new Text("CLIENTES");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 0);

        HBox hBoxButtons = new HBox();
        JFXTextField textFieldSearch = new JFXTextField();
        textFieldSearch.setPromptText("BUSCAR");
        textFieldSearch.setPrefSize(x, y);

        JFXButton buttonFile = new JFXButton("ARCHIVO");
        buttonFile.getStyleClass().addAll("customButton", "primaryButton");
        buttonFile.setButtonType(JFXButton.ButtonType.FLAT);
        buttonFile.setPrefSize(x, y);
        buttonFile.setOnAction(event -> {
            FileControl.getInstance().uploadFile("CustomerFile", "*.tmca");
            ArrayList<String> arrayList = FileControl.getInstance().readFile();
            if (arrayList != null) {
                for (String command: arrayList) {
                    String[] params = command.split("-");
                    boolean role = params[4].equalsIgnoreCase("ORO");
                    ControllerCustomer.getInstance().createCustomer(Integer.parseInt(params[0]),
                            params[1], role, params[2], params[3]);
                }
                updateTableViewItems();
                Alert.getInstance().showNotification("CLIENTES", "ARCHIVO LEIDO EXITOSAMENTE");
            }
        });

        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y);
        /*buttonAdd.setOnAction(event -> {
            hBox.getChildren().clear();
            hBox.getChildren().addAll(gridPane, CreateEmployee.getInstance().getGridPane());
        });*/

        JFXButton buttonUpdate = new JFXButton("MODIFICAR");
        buttonUpdate.getStyleClass().addAll("customButton", "warningButton");
        buttonUpdate.setButtonType(JFXButton.ButtonType.FLAT);
        /*buttonUpdate.setOnAction(event -> {
            hBox.getChildren().clear();
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                hBox.getChildren().addAll(gridPane, UpdateEmployee.getInstance()
                        .getGridPane((Employee) tableView.getSelectionModel().getSelectedItem()));
            } else {
                hBox.getChildren().add(gridPane);
            }
        });*/
        buttonUpdate.setPrefSize(x, y);

        JFXButton buttonDelete = new JFXButton("ELIMINAR");
        buttonDelete.getStyleClass().addAll("customButton", "dangerButton");
        buttonDelete.setButtonType(JFXButton.ButtonType.FLAT);
        buttonDelete.setPrefSize(x, y);
        buttonDelete.setOnAction(event -> {
            Customer customer = (Customer) tableView.getSelectionModel().getSelectedItem();
            if (customer != null) {
                restartHBox();
                ControllerCustomer.getInstance().deleteCustomer(customer.getId());
                updateTableViewItems();
                Alert.getInstance().showNotification("CLIENTES", "CLIENTE ELIMINADO EXITOSAMENTE");
            }
        });

        hBoxButtons.getChildren().addAll(textFieldSearch, buttonFile, buttonAdd, buttonUpdate, buttonDelete);
        hBoxButtons.setPrefSize(x, y / 8);
        hBoxButtons.setMargin(textFieldSearch, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonFile, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonAdd, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonUpdate, new Insets(0, 5, 0, 0));
        gridPane.add(hBoxButtons, 0, 1);

        TableColumn<Customer, Integer> columnID = new TableColumn<>("ID");
        columnID.setPrefWidth(50);
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Customer, String> columnName = new TableColumn<>("NOMBRE");
        columnName.setPrefWidth((3 * x / 4) / 4);
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Customer, User> columnUser = new TableColumn<>("USUARIO");
        columnUser.setPrefWidth((3 * x / 4) / 4);
        columnUser.setCellValueFactory(new PropertyValueFactory<>("user"));
        TableColumn<Customer, Boolean> columnRole = new TableColumn<>("TIPO");
        columnRole.setPrefWidth((3 * x / 4) / 4);
        columnRole.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnRole.setCellFactory(col -> new TableCell<Customer, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty) ;
                setText(empty ? null : item ? "ORO" : "NORMAL" );
            }
        });

        updateObservableList();
        tableView = new TableView<>(observableList);
        tableView.getColumns().addAll(columnID, columnName, columnUser, columnRole);
        tableView.setPrefSize(x, 7 * y / 8);
        /*tableView.setOnMouseClicked(event -> {
            hBox.getChildren().clear();
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                hBox.getChildren().addAll(gridPane, ShowEmployee.getInstance()
                        .getGridPane((Employee) tableView.getSelectionModel().getSelectedItem()));
            } else {
                hBox.getChildren().add(gridPane);
            }
        });*/
        gridPane.add(tableView, 0, 2);
        hBox.getChildren().add(gridPane);

        return hBox;
    }
}
