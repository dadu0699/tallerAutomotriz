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
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.didierdominguez.bean.Car;
import org.didierdominguez.bean.Customer;
import org.didierdominguez.bean.User;
import org.didierdominguez.controller.ControllerCar;
import org.didierdominguez.controller.ControllerCustomer;
import org.didierdominguez.list.CircularDoubleList.CircularDoubleNode;
import org.didierdominguez.list.CircularSimpleList.CircularSimpleNode;
import org.didierdominguez.util.Alert;
import org.didierdominguez.util.FileControl;
import org.didierdominguez.util.ScreenSize;

import java.util.ArrayList;

public class ViewCustomer extends Stage {
    private static ViewCustomer instance;
    private HBox hBoxPanels;
    private VBox vBoxApplications;
    private TableView tableViewCustomer;
    private TableView tableViewCar;
    private ObservableList observableListCustomer;
    private ObservableList observableListCar;

    private ViewCustomer() {
    }

    public static ViewCustomer getInstance() {
        if (instance == null) {
            instance = new ViewCustomer();
        }
        return instance;
    }

    public void restartHBox() {
        hBoxPanels.getChildren().clear();
        vBoxApplications.getChildren().clear();
        vBoxApplications.getChildren().addAll(CreateCustomer.getInstance().getGridPane(), getViewCar());
        hBoxPanels.getChildren().addAll(getViewCustomer(), vBoxApplications);
        updateTableViewItemsCar(null);
    }

    private void updateObservableListCustomer() {
        ArrayList<Customer> arrayListCustomer = new ArrayList<>();
        CircularDoubleNode auxiliaryNode = ControllerCustomer.getInstance().getCustomerList().getNode();
        if (auxiliaryNode != null) {
            do {
                arrayListCustomer.add((Customer) auxiliaryNode.getObject());
                auxiliaryNode = auxiliaryNode.getPreviousNode();
            } while (auxiliaryNode != ControllerCustomer.getInstance().getCustomerList().getNode());
        }
        if (observableListCustomer != null) {
            observableListCustomer.clear();
        }
        observableListCustomer = FXCollections.observableArrayList(arrayListCustomer);
    }

    private void updateObservableListCar(Customer customer) {
        ArrayList<Car> arrayListCar = new ArrayList<>();
        CircularSimpleNode auxiliaryNode = ControllerCar.getInstance().getCarList().getNode();
        if (auxiliaryNode != null) {
            do {
                if (((Car) auxiliaryNode.getObject()).getCustomer() == customer) {
                    arrayListCar.add((Car) auxiliaryNode.getObject());
                }
                auxiliaryNode = auxiliaryNode.getNextNode();
            } while (auxiliaryNode != ControllerCar.getInstance().getCarList().getNode());
        }
        if (observableListCar != null) {
            observableListCar.clear();
        }
        observableListCar = FXCollections.observableArrayList(arrayListCar);
    }

    public void updateTableViewItemsCustomer() {
        updateObservableListCustomer();
        tableViewCustomer.setItems(observableListCustomer);
    }

    public void updateTableViewItemsCar(Customer customer) {
        updateObservableListCar(customer);
        tableViewCar.setItems(observableListCar);
    }

    public VBox getViewCustomerDetail() {
        VBox vBox = new VBox();
        GridPane gridPaneTitle = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPaneTitle.setVgap(10);
        gridPaneTitle.setPadding(new Insets(20));
        // gridPaneTitle.setGridLinesVisible(true);
        gridPaneTitle.setMinWidth(x / 2);
        gridPaneTitle.setPrefSize(x, y / 8);
        vBox.setPrefSize(x, y);

        Text textTitle = new Text("CLIENTES / AUTOMOVILES");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPaneTitle.add(textTitle, 0, 0);

        gridPaneTitle.setPadding(new Insets(20, 20, -10, 20));
        hBoxPanels = new HBox();
        hBoxPanels.setPrefSize(x, 7 * y / 8);

        vBoxApplications = new VBox();
        vBoxApplications.setPrefSize(x, 7 * y / 8);
        vBoxApplications.getChildren().addAll(CreateCustomer.getInstance().getGridPane(), getViewCar());

        hBoxPanels.getChildren().addAll(getViewCustomer(), vBoxApplications);
        vBox.getChildren().addAll(gridPaneTitle, hBoxPanels);
        return vBox;
    }

    private GridPane getViewCustomer() {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));
        gridPane.setPrefSize(x, y);

        Text textTitle = new Text("CLIENTES");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(20));
        gridPane.add(textTitle, 0, 0);

        JFXTextField textFieldSearch = new JFXTextField();
        textFieldSearch.setPromptText("BUSCAR");
        textFieldSearch.setPrefSize(x, y / 8);
        gridPane.add(textFieldSearch, 0, 1);

        HBox hBoxButtons = new HBox();
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
                    if (ControllerCustomer.getInstance().searchCustomer(Integer.parseInt(params[0])) == null) {
                        boolean role = params[4].equalsIgnoreCase("ORO");
                        ControllerCustomer.getInstance().createCustomer(Integer.parseInt(params[0]),
                                params[1], role, params[2], params[3]);
                        String[] cars = params[5].split(";");
                        Customer customer = ControllerCustomer.getInstance().searchCustomer(Integer.parseInt(params[0]));
                        if (customer != null) {
                            for (String car: cars) {
                                String[] carDetail = car.split(",");
                                ControllerCar.getInstance().createCar(carDetail[0], carDetail[1], carDetail[2], carDetail[3], customer);
                            }
                        }
                    }
                }
                updateTableViewItemsCustomer();
                Alert.getInstance().showNotification("CLIENTES", "ARCHIVO LEIDO EXITOSAMENTE");
            }
        });

        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y);
        buttonAdd.setOnAction(event -> restartHBox());

        JFXButton buttonUpdate = new JFXButton("MODIFICAR");
        buttonUpdate.getStyleClass().addAll("customButton", "warningButton");
        buttonUpdate.setButtonType(JFXButton.ButtonType.FLAT);
        buttonUpdate.setPrefSize(x, y);
        buttonUpdate.setOnAction(event -> {
            if (tableViewCustomer.getSelectionModel().getSelectedItem() != null) {
                vBoxApplications.getChildren().remove(0);
                vBoxApplications.getChildren().add(0, UpdateCustomer.getInstance()
                        .getGridPane((Customer) tableViewCustomer.getSelectionModel().getSelectedItem()));
            }
        });

        JFXButton buttonDelete = new JFXButton("ELIMINAR");
        buttonDelete.getStyleClass().addAll("customButton", "dangerButton");
        buttonDelete.setButtonType(JFXButton.ButtonType.FLAT);
        buttonDelete.setPrefSize(x, y);
        buttonDelete.setOnAction(event -> {
            Customer customer = (Customer) tableViewCustomer.getSelectionModel().getSelectedItem();
            if (customer != null) {
                for (Object obj : observableListCar) {
                    Car car = (Car) obj;
                    ControllerCar.getInstance().deleteCar(car.getId());
                    Alert.getInstance().showNotification("CLIENTES", "AUTOMOVIL ELIMINADO EXITOSAMENTE");
                }
                ControllerCustomer.getInstance().deleteCustomer(customer.getId());
                restartHBox();
                Alert.getInstance().showNotification("CLIENTES", "CLIENTE ELIMINADO EXITOSAMENTE");
            }
        });

        hBoxButtons.getChildren().addAll(buttonFile, buttonAdd, buttonUpdate, buttonDelete);
        hBoxButtons.setMargin(buttonFile, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonAdd, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonUpdate, new Insets(0, 5, 0, 0));
        hBoxButtons.setPrefSize(x, y / 8);
        gridPane.add(hBoxButtons, 0, 2);

        TableColumn<Customer, Integer> columnID = new TableColumn<>("ID");
        columnID.setPrefWidth(50);
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Customer, String> columnName = new TableColumn<>("NOMBRE");
        columnName.setPrefWidth(x/10);
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Customer, User> columnUser = new TableColumn<>("USUARIO");
        columnUser.setPrefWidth(x/10);
        columnUser.setCellValueFactory(new PropertyValueFactory<>("user"));
        TableColumn<Customer, Boolean> columnRole = new TableColumn<>("TIPO");
        columnRole.setPrefWidth(x/10);
        columnRole.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnRole.setCellFactory(col -> new TableCell<Customer, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty) ;
                setText(empty ? null : item ? "ORO" : "NORMAL" );
            }
        });

        updateObservableListCustomer();
        tableViewCustomer = new TableView<>(observableListCustomer);
        tableViewCustomer.getColumns().addAll(columnID, columnName, columnUser, columnRole);
        tableViewCustomer.setOnMouseClicked(event -> {
            if (tableViewCustomer.getSelectionModel().getSelectedItem() != null) {
                vBoxApplications.getChildren().remove(0);
                vBoxApplications.getChildren().add(0, ShowCustomer.getInstance().getGridPane((Customer) tableViewCustomer.getSelectionModel().getSelectedItem()));
                updateTableViewItemsCar((Customer) tableViewCustomer.getSelectionModel().getSelectedItem());
            }
        });
        tableViewCustomer.setPrefSize(x, 7 * y / 8);

        gridPane.add(tableViewCustomer, 0, 3);
        gridPane.setPadding(new Insets(-10, 10, 20, 20));

        return gridPane;
    }

    private GridPane getViewCar() {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));
        gridPane.setPrefSize(x, y);

        Text textTitleC = new Text("AUTOMOVILES");
        textTitleC.getStyleClass().add("textTitle");
        textTitleC.setFont(new Font(20));
        gridPane.add(textTitleC, 0, 0);

        HBox hBoxButtons = new HBox();

        JFXButton buttonAdd = new JFXButton("AGREGAR AUTOMOVIL");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y);
        buttonAdd.setOnAction(event -> {
            if (tableViewCustomer.getSelectionModel().getSelectedItem() != null) {
                vBoxApplications.getChildren().remove(0);
                vBoxApplications.getChildren().add(0, CreateCar.getInstance()
                        .getGridPane((Customer) tableViewCustomer.getSelectionModel().getSelectedItem()));
            }
        });

        JFXButton buttonUpdate = new JFXButton("MODIFICAR");
        buttonUpdate.getStyleClass().addAll("customButton", "warningButton");
        buttonUpdate.setButtonType(JFXButton.ButtonType.FLAT);
        buttonUpdate.setPrefSize(x, y);
        buttonUpdate.setOnAction(event -> {
            if (tableViewCar.getSelectionModel().getSelectedItem() != null) {
                vBoxApplications.getChildren().remove(0);
                vBoxApplications.getChildren().add(0, UpdateCar.getInstance()
                        .getGridPane((Car) tableViewCar.getSelectionModel().getSelectedItem()));
            }
        });

        JFXButton buttonDelete = new JFXButton("ELIMINAR");
        buttonDelete.getStyleClass().addAll("customButton", "dangerButton");
        buttonDelete.setButtonType(JFXButton.ButtonType.FLAT);
        buttonDelete.setPrefSize(x, y);
        buttonDelete.setOnAction(event -> {
            Car car = (Car) tableViewCar.getSelectionModel().getSelectedItem();
            if (car != null) {
                ControllerCar.getInstance().deleteCar(car.getId());
                vBoxApplications.getChildren().remove(0);
                vBoxApplications.getChildren().add(0, ShowCustomer.getInstance().getGridPane((Customer) tableViewCustomer.getSelectionModel().getSelectedItem()));
                updateTableViewItemsCar((Customer) tableViewCustomer.getSelectionModel().getSelectedItem());
                Alert.getInstance().showNotification("CLIENTES", "AUTOMOVIL ELIMINADO EXITOSAMENTE");
            }
        });

        hBoxButtons.getChildren().addAll(buttonAdd, buttonUpdate, buttonDelete);
        hBoxButtons.setPrefSize(x, y / 8);
        hBoxButtons.setMargin(buttonAdd, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonUpdate, new Insets(0, 5, 0, 0));
        gridPane.add(hBoxButtons, 0, 1);

        TableColumn<Car, Integer> columnIDC = new TableColumn<>("PLACA");
        columnIDC.setPrefWidth(x / 10);
        columnIDC.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Car, String> columnBrand = new TableColumn<>("MARCA");
        columnBrand.setPrefWidth(x / 10);
        columnBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        TableColumn<Car, String> columnModel = new TableColumn<>("MODELO");
        columnModel.setPrefWidth(x / 10);
        columnModel.setCellValueFactory(new PropertyValueFactory<>("Model"));

        tableViewCar = new TableView<>(observableListCar);
        tableViewCar.getColumns().addAll(columnIDC, columnBrand, columnModel);
        tableViewCar.setPrefSize(x, 7 * y / 8);
        tableViewCar.setOnMouseClicked(event -> {
            if (tableViewCar.getSelectionModel().getSelectedItem() != null) {
                vBoxApplications.getChildren().remove(0);
                vBoxApplications.getChildren().add(0, ShowCar.getInstance().getGridPane((Car) tableViewCar.getSelectionModel().getSelectedItem()));
            }
        });
        gridPane.add(tableViewCar, 0, 2);

        gridPane.setPadding(new Insets(10, 20, 20, 10));
        return gridPane;
    }
}
