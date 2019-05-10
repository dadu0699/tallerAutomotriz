package org.didierdominguez.view.administrator;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.didierdominguez.bean.Service;
import org.didierdominguez.bean.SparePart;
import org.didierdominguez.controller.ControllerService;
import org.didierdominguez.controller.ControllerSparePart;
import org.didierdominguez.list.SimpleList.SimpleNode;
import org.didierdominguez.list.Stack.StackNode;
import org.didierdominguez.util.Alert;
import org.didierdominguez.util.FileControl;
import org.didierdominguez.util.ScreenSize;
import org.didierdominguez.util.Verifications;

import java.util.ArrayList;

public class ViewService extends Stage {
    private static ViewService instance;
    private HBox hBox;
    private GridPane gridPane;
    private TableView tableView;
    private ObservableList observableList;

    private ViewService() {
    }

    public static ViewService getInstance() {
        if (instance == null) {
            instance = new ViewService();
        }
        return instance;
    }

    public void restartHBox() {
        hBox.getChildren().clear();
        hBox.getChildren().add(gridPane);
    }

    private void updateObservableList() {
        ArrayList<Service> arrayListService = new ArrayList<>();
        SimpleNode auxiliaryNode = ControllerService.getInstance().getServiceList().getFirstNode();
        while (auxiliaryNode != null) {
            arrayListService.add((Service) auxiliaryNode.getObject());
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (observableList != null) {
            observableList.clear();
        }
        observableList = FXCollections.observableArrayList(arrayListService);
    }

    public void updateTableViewItems() {
        updateObservableList();
        tableView.setItems(observableList);
    }

    public HBox getViewService() {
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

        Text textTitle = new Text("SERVICIOS");
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
            FileControl.getInstance().uploadFile("ServiceFile", "*.tms");
            ArrayList<String> arrayList = FileControl.getInstance().readFile();
            if (arrayList != null) {
                for (String command: arrayList) {
                    String[] params = command.split("-");
                    String[] spares = params[3].split(";");
                    if (ControllerService.getInstance().searchService(params[0], params[1], params[2]) == null) {
                        ControllerService.getInstance().createService(params[0], params[1], params[2], Double.parseDouble(params[4]));
                        Service service = ControllerService.getInstance().searchService(params[0], params[1], params[2]);
                        for (String sparePartCommand: spares) {
                            SparePart sparePart = ControllerSparePart.getInstance().searchSparePart(Integer.parseInt(sparePartCommand));
                            if (sparePart != null) {
                                if (sparePart.getModel().equalsIgnoreCase(params[2]) && sparePart.getBrand().equalsIgnoreCase(params[1])) {
                                    ControllerService.getInstance().createSparesStack(service.getId(), sparePart);
                                }
                            }
                        }
                    }
                }
                updateTableViewItems();
                Alert.getInstance().showNotification("SERVICIOS", "ARCHIVO LEIDO EXITOSAMENTE");
            }
        });

        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y);
        buttonAdd.setOnAction(event -> {
            hBox.getChildren().clear();
            hBox.getChildren().addAll(gridPane, CreateService.getInstance().getGridPane());
        });

        JFXButton buttonUpdate = new JFXButton("MODIFICAR");
        buttonUpdate.getStyleClass().addAll("customButton", "warningButton");
        buttonUpdate.setButtonType(JFXButton.ButtonType.FLAT);
        buttonUpdate.setPrefSize(x, y);
        buttonUpdate.setOnAction(event -> {
            hBox.getChildren().clear();
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                hBox.getChildren().addAll(gridPane, UpdateService.getInstance()
                        .getGridPane((Service) tableView.getSelectionModel().getSelectedItem()));
            } else {
                hBox.getChildren().add(gridPane);
            }
        });

        JFXButton buttonDelete = new JFXButton("ELIMINAR");
        buttonDelete.getStyleClass().addAll("customButton", "dangerButton");
        buttonDelete.setButtonType(JFXButton.ButtonType.FLAT);
        buttonDelete.setPrefSize(x, y);
        buttonDelete.setOnAction(event -> {
            Service service = (Service) tableView.getSelectionModel().getSelectedItem();
            if (service != null) {
                restartHBox();
                ControllerService.getInstance().deleteService(service.getId());
                updateTableViewItems();
                Alert.getInstance().showNotification("SERVICIOS", "SERVICIO ELIMINADO EXITOSAMENTE");
            }
        });

        hBoxButtons.getChildren().addAll(textFieldSearch, buttonFile, buttonAdd, buttonUpdate, buttonDelete);
        hBoxButtons.setPrefSize(x, y / 8);
        hBoxButtons.setMargin(textFieldSearch, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonFile, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonAdd, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonUpdate, new Insets(0, 5, 0, 0));
        gridPane.add(hBoxButtons, 0, 1);

        TableColumn<Service, Integer> columnID = new TableColumn<>("ID");
        columnID.setPrefWidth(50);
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Service, String> columnName = new TableColumn<>("NOMBRE");
        columnName.setPrefWidth((3 * x / 4) / 6);
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Service, String> columnBrand = new TableColumn<>("MARCA");
        columnBrand.setPrefWidth((3 * x / 4) / 6);
        columnBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        TableColumn<Service, String> columnModel = new TableColumn<>("MODELO");
        columnModel.setPrefWidth((3 * x / 4) / 6);
        columnModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        TableColumn<Service, Integer> columnLaborPrice = new TableColumn<>("PRECIO MANO DE OBRA");
        columnLaborPrice.setPrefWidth((3 * x / 4) / 6);
        columnLaborPrice.setCellValueFactory(new PropertyValueFactory<>("laborPrice"));
        TableColumn<Service, String> columnTotal = new TableColumn<>("PRECIO");
        columnTotal.setPrefWidth((3 * x / 4) / 6);
        columnTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        updateObservableList();
        tableView = new TableView<>(observableList);
        tableView.getColumns().addAll(columnID, columnName, columnBrand, columnModel, columnLaborPrice, columnTotal);
        tableView.setPrefSize(x, 7 * y / 8);
        tableView.setOnMouseClicked(event -> {
            hBox.getChildren().clear();
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                hBox.getChildren().addAll(gridPane, ShowService.getInstance().getViewSparePart((Service)
                        tableView.getSelectionModel().getSelectedItem()));
            } else {
                hBox.getChildren().add(gridPane);
            }
        });
        gridPane.add(tableView, 0, 2);
        hBox.getChildren().add(gridPane);

        return hBox;
    }
}

class CreateService {
    private static CreateService instance;

    private CreateService() {
    }

    static CreateService getInstance() {
        if (instance == null) {
            instance = new CreateService();
        }
        return instance;
    }

    GridPane getGridPane() {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(20));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("AGREGAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 5, 2, 1);

        JFXTextField fieldName = new JFXTextField();
        fieldName.setPromptText("NOMBRE");
        fieldName.setLabelFloat(true);
        fieldName.setPrefWidth(x);
        gridPane.add(fieldName, 0, 6, 2, 1);

        JFXTextField fieldBrand = new JFXTextField();
        fieldBrand.setPromptText("MARCA");
        fieldBrand.setLabelFloat(true);
        fieldBrand.setPrefWidth(x);
        gridPane.add(fieldBrand, 0, 7, 2, 1);

        JFXTextField fieldModel = new JFXTextField();
        fieldModel.setPromptText("MODELO");
        fieldModel.setLabelFloat(true);
        fieldModel.setPrefWidth(x);
        gridPane.add(fieldModel, 0, 8, 2, 1);

        Label labelLaborPrice = new Label("PRECIO MANO DE OBRA:");
        gridPane.add(labelLaborPrice, 0, 9);
        Spinner<Double> spinnerLaborPrice = new Spinner<>(0.00, 100000.00, 0, 1);
        spinnerLaborPrice.setEditable(true);
        spinnerLaborPrice.setPrefWidth(x);
        gridPane.add(spinnerLaborPrice, 1, 9);

        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y / 20);
        buttonAdd.setOnAction(event -> {
            if (fieldName.getText().length() == 0
                    || fieldBrand.getText().length() == 0
                    || fieldModel.getText().length() == 0
                    || !Verifications.getInstance().isNumericDouble(spinnerLaborPrice.getEditor().getText())
                    || Double.parseDouble(spinnerLaborPrice.getEditor().getText()) <= 0) {
                Alert.getInstance().showAlert(gridPane, "ERROR", "UNO O MÁS DATOS SON INCORRECTOS");
            } else {
                Service service = ControllerService.getInstance().searchService(fieldName.getText().trim(),
                        fieldBrand.getText().trim(),
                        fieldModel.getText().trim());
                if (service != null) {
                    Alert.getInstance().showAlert(gridPane, "ERROR", "EL SERVICIO YA ESTÁ REGISTRADO");
                } else {
                    ControllerService.getInstance().createService(fieldName.getText().trim().toUpperCase(),
                            fieldBrand.getText().trim().toUpperCase(), fieldModel.getText().trim().toUpperCase(),
                            Double.parseDouble(spinnerLaborPrice.getEditor().getText().trim()));
                    ViewService.getInstance().updateTableViewItems();
                    Alert.getInstance().showNotification("SERVICIOS", "SERVICIO AGREGADO EXITOSAMENTE");
                }
            }
        });
        gridPane.add(buttonAdd, 0, 10);

        JFXButton buttonCancel = new JFXButton("CANCELAR");
        buttonCancel.getStyleClass().addAll("customButton", "dangerButton");
        buttonCancel.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCancel.setPrefSize(x, y / 20);
        buttonCancel.setOnAction(event -> ViewService.getInstance().restartHBox());
        gridPane.add(buttonCancel, 1, 10);

        return gridPane;
    }
}

class UpdateService {
    private static UpdateService instance;

    private UpdateService() {
    }

    static UpdateService getInstance() {
        if (instance == null) {
            instance = new UpdateService();
        }
        return instance;
    }

    GridPane getGridPane(Service service) {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(20));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("MODIFICAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 5, 2, 1);

        JFXTextField fieldName = new JFXTextField(service.getName());
        fieldName.setPromptText("NOMBRE");
        fieldName.setLabelFloat(true);
        fieldName.setPrefWidth(x);
        gridPane.add(fieldName, 0, 6, 2, 1);

        JFXTextField fieldBrand = new JFXTextField(service.getBrand());
        fieldBrand.setPromptText("MARCA");
        fieldBrand.setLabelFloat(true);
        fieldBrand.setPrefWidth(x);
        gridPane.add(fieldBrand, 0, 7, 2, 1);

        JFXTextField fieldModel = new JFXTextField(service.getModel());
        fieldModel.setPromptText("MODELO");
        fieldModel.setLabelFloat(true);
        fieldModel.setPrefWidth(x);
        gridPane.add(fieldModel, 0, 8, 2, 1);

        Label labelLaborPrice = new Label("PRECIO MANO DE OBRA:");
        gridPane.add(labelLaborPrice, 0, 9);
        Spinner<Double> spinnerLaborPrice = new Spinner<>(0.00, 100000.00, service.getLaborPrice(), 1);
        spinnerLaborPrice.setEditable(true);
        spinnerLaborPrice.setPrefWidth(x);
        gridPane.add(spinnerLaborPrice, 1, 9);

        JFXButton buttonUpdate = new JFXButton("MODIFICAR");
        buttonUpdate.getStyleClass().addAll("customButton", "primaryButton");
        buttonUpdate.setButtonType(JFXButton.ButtonType.FLAT);
        buttonUpdate.setPrefSize(x, y / 20);
        buttonUpdate.setOnAction(event -> {
            if (fieldName.getText().length() == 0
                    || fieldBrand.getText().length() == 0
                    || fieldModel.getText().length() == 0
                    || !Verifications.getInstance().isNumericDouble(spinnerLaborPrice.getEditor().getText())
                    || Double.parseDouble(spinnerLaborPrice.getEditor().getText()) <= 0) {
                Alert.getInstance().showAlert(gridPane, "ERROR", "UNO O MÁS DATOS SON INCORRECTOS");
            } else {
                ControllerService.getInstance().updateService(service.getId(),
                        fieldName.getText().trim().toUpperCase(),
                        fieldBrand.getText().trim().toUpperCase(), fieldModel.getText().trim().toUpperCase(),
                        Double.parseDouble(spinnerLaborPrice.getEditor().getText().trim()));
                if (ControllerService.getInstance().updateService()) {
                    Alert.getInstance().showNotification("SERVICIOS", "SERVICIO ACTUALIZADO EXITOSAMENTE");
                } else {
                    Alert.getInstance().showAlert(gridPane, "ERROR", "ERROR AL MODIFICAR REPUESTO");
                }
                ViewService.getInstance().updateTableViewItems();
            }
        });
        gridPane.add(buttonUpdate, 0, 10);

        JFXButton buttonCancel = new JFXButton("CANCELAR");
        buttonCancel.getStyleClass().addAll("customButton", "dangerButton");
        buttonCancel.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCancel.setPrefSize(x, y / 20);
        buttonCancel.setOnAction(event -> ViewService.getInstance().restartHBox());
        gridPane.add(buttonCancel, 1, 10);

        return gridPane;
    }
}

class ShowService {
    private static ShowService instance;
    private GridPane gridPane;
    private TableView tableView;
    private ObservableList observableList;
    private ObservableList observableListOptions;

    private ShowService() {
    }

    static ShowService getInstance() {
        if (instance == null) {
            instance = new ShowService();
        }
        return instance;
    }

    private void updateObservableList(Service service) {
        ArrayList<SparePart> arrayListSparePart = new ArrayList<>();
        StackNode auxiliaryNode = (StackNode) service.getSpares().getTop();
        while (auxiliaryNode != null) {
            arrayListSparePart.add((SparePart) auxiliaryNode.getObject());
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (observableList != null) {
            observableList.clear();
        }
        observableList = FXCollections.observableArrayList(arrayListSparePart);
    }

    private void updateObservableListOptions(Service service) {
        ArrayList<SparePart> arrayListSparePart = new ArrayList<>();
        SimpleNode auxiliaryNode = ControllerSparePart.getInstance().getSparePartList().getFirstNode();
        while (auxiliaryNode != null) {
            SparePart sparePart = (SparePart) auxiliaryNode.getObject();
            if (sparePart.getBrand().equalsIgnoreCase(service.getBrand())
                    && sparePart.getModel().equalsIgnoreCase(service.getModel())) {
                arrayListSparePart.add((SparePart) auxiliaryNode.getObject());
            }
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (observableListOptions != null) {
            observableListOptions.clear();
        }
        observableListOptions = FXCollections.observableArrayList(arrayListSparePart);
    }

    public void updateTableViewItems(Service service) {
        updateObservableList(service);
        tableView.setItems(observableList);
    }

    public GridPane getViewSparePart(Service service) {
        gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20));
        // gridPane.setGridLinesVisible(true);
        gridPane.setPrefSize(x, y);

        Text textTitle = new Text("REPUESTOS");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 3);

        HBox hBoxButtons = new HBox();

        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y);

        JFXButton buttonDelete = new JFXButton("ELIMINAR");
        buttonDelete.getStyleClass().addAll("customButton", "dangerButton");
        buttonDelete.setButtonType(JFXButton.ButtonType.FLAT);
        buttonDelete.setPrefSize(x, y);
        buttonDelete.setOnAction(event -> {
            if (service.getSpares().getTop() != null) {
                service.getSpares().pop();
                updateTableViewItems(service);
                ControllerService.getInstance().updateTotalService(service.getId());
                ViewService.getInstance().updateTableViewItems();
                Alert.getInstance().showNotification("REPUESTOS", "REPUESTO ELIMINADO EXITOSAMENTE");
            }
        });

        hBoxButtons.getChildren().addAll(buttonAdd, buttonDelete);
        hBoxButtons.setPrefSize(x, y / 7);
        hBoxButtons.setMargin(buttonAdd, new Insets(0, 5, 0, 0));
        gridPane.add(hBoxButtons, 0, 4);

        updateObservableListOptions(service);
        JFXComboBox<SparePart> comboBoxSparePart = new JFXComboBox<>(observableListOptions);
        comboBoxSparePart.setPromptText("REPUESTO");
        comboBoxSparePart.setLabelFloat(true);
        comboBoxSparePart.setPrefSize(x, y / 7);
        buttonAdd.setOnAction(event -> {
            if (comboBoxSparePart.getSelectionModel().getSelectedItem() == null) {
                Alert.getInstance().showAlert(gridPane, "ERROR", "NO SE HA SELEECIONADO UN REPUESTO");
            } else {
                ControllerService.getInstance().createSparesStack(service.getId(), comboBoxSparePart.getSelectionModel().getSelectedItem());
                updateTableViewItems(service);
                ControllerService.getInstance().updateTotalService(service.getId());
                ViewService.getInstance().updateTableViewItems();
                Alert.getInstance().showNotification("REPUESTOS", "REPUESTO AGREGADO EXITOSAMENTE");
            }
        });
        gridPane.add(comboBoxSparePart, 0, 5);

        TableColumn<SparePart, Integer> columnID = new TableColumn<>("ID");
        columnID.setPrefWidth(50);
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<SparePart, String> columnName = new TableColumn<>("NOMBRE");
        columnName.setPrefWidth((3 * x / 4) / 5);
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<SparePart, Double> columnPrice = new TableColumn<>("PRECIO");
        columnPrice.setPrefWidth((3 * x / 4) / 5);
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        updateObservableList(service);
        tableView = new TableView<>(observableList);
        tableView.getColumns().addAll(columnID, columnName, columnPrice);
        tableView.setPrefSize(x, 6 * y / 7);
        gridPane.add(tableView, 0, 6);

        return gridPane;
    }
}
