package org.didierdominguez.view.administrator;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.didierdominguez.bean.Employee;
import org.didierdominguez.bean.User;
import org.didierdominguez.controller.ControllerEmployee;
import org.didierdominguez.controller.ControllerUser;
import org.didierdominguez.list.DoubleList.DoubleNode;
import org.didierdominguez.util.Alert;
import org.didierdominguez.util.FileControl;
import org.didierdominguez.util.ScreenSize;

import java.util.ArrayList;

public class ViewEmployee extends Stage {
    private static ViewEmployee instance;
    private HBox hBox;
    private GridPane gridPane;
    private TableView tableView;
    private ObservableList observableList;

    private ViewEmployee() {
    }

    public static ViewEmployee getInstance() {
        if (instance == null) {
            instance = new ViewEmployee();
        }
        return instance;
    }

    public void restartHBox() {
        hBox.getChildren().clear();
        hBox.getChildren().add(gridPane);
    }

    private void updateObservableList() {
        ArrayList<Employee> arrayListEmployee = new ArrayList<>();
        DoubleNode auxiliaryNode = ControllerEmployee.getInstance().getEmployeeList().getFirstNode();
        while (auxiliaryNode != null) {
            arrayListEmployee.add((Employee) auxiliaryNode.getObject());
            auxiliaryNode = auxiliaryNode.getNextNode();
        }

        if (observableList != null) {
            observableList.clear();
        }
        observableList = FXCollections.observableArrayList(arrayListEmployee);
    }

    public void updateTableViewItems() {
        updateObservableList();
        tableView.setItems(observableList);
    }

    public HBox getViewEmployee() {
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

        Text textTitle = new Text("EMPLEADOS");
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
            FileControl.getInstance().uploadFile("EmployeeFile", "*.tme");
            ArrayList<String> arrayList = FileControl.getInstance().readFile();
            for (String command: arrayList) {
                String[] params = command.split("-");
                boolean userRole = params[1].equals("ADMINISTRADOR");
                ControllerEmployee.getInstance().createEmployee(params[0], params[1], params[2], params[3], userRole);
            }
            updateTableViewItems();
            Alert.getInstance().showNotification("EMPLEADO", "ARCHIVO LEIDO EXITOSAMENTE");
        });

        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y);
        buttonAdd.setOnAction(event -> {
            hBox.getChildren().clear();
            hBox.getChildren().addAll(gridPane, CreateEmployee.getInstance().getGridPane());
        });

        JFXButton buttonUpdate = new JFXButton("MODIFICAR");
        buttonUpdate.getStyleClass().addAll("customButton", "warningButton");
        buttonUpdate.setButtonType(JFXButton.ButtonType.FLAT);
        buttonUpdate.setOnAction(event -> {
            hBox.getChildren().clear();
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                hBox.getChildren().addAll(gridPane, UpdateEmployee.getInstance()
                        .getGridPane((Employee) tableView.getSelectionModel().getSelectedItem()));
            } else {
                hBox.getChildren().add(gridPane);
            }
        });
        buttonUpdate.setPrefSize(x, y);

        JFXButton buttonDelete = new JFXButton("ELIMINAR");
        buttonDelete.getStyleClass().addAll("customButton", "dangerButton");
        buttonDelete.setButtonType(JFXButton.ButtonType.FLAT);
        buttonDelete.setPrefSize(x, y);
        buttonDelete.setOnAction(event -> {
            Employee employee = (Employee) tableView.getSelectionModel().getSelectedItem();
            if (employee != null) {
                restartHBox();
                ControllerEmployee.getInstance().deleteEmployee(employee.getId());
                updateTableViewItems();
                Alert.getInstance().showNotification("EMPLEADO", "EMPLEADO ELIMINADO EXITOSAMENTE");
            }
        });

        hBoxButtons.getChildren().addAll(textFieldSearch, buttonFile, buttonAdd, buttonUpdate, buttonDelete);
        hBoxButtons.setPrefSize(x, y / 8);
        hBoxButtons.setMargin(textFieldSearch, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonFile, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonAdd, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonUpdate, new Insets(0, 5, 0, 0));
        gridPane.add(hBoxButtons, 0, 1);

        TableColumn<Employee, Integer> columnID = new TableColumn<>("ID");
        columnID.setPrefWidth(50);
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Employee, String> columnName = new TableColumn<>("NOMBRE");
        columnName.setPrefWidth((3 * x / 4) / 4);
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Employee, User> columnAddress = new TableColumn<>("USUARIO");
        columnAddress.setPrefWidth((3 * x / 4) / 4);
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("user"));
        TableColumn<Employee, String> columnPhoneNumber = new TableColumn<>("ROLE");
        columnPhoneNumber.setPrefWidth((3 * x / 4) / 4);
        columnPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("role"));

        updateObservableList();
        tableView = new TableView<>(observableList);
        tableView.getColumns().addAll(columnID, columnName, columnAddress, columnPhoneNumber);
        tableView.setPrefSize(x, 7 * y / 8);
        tableView.setOnMouseClicked(event -> {
            hBox.getChildren().clear();
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                hBox.getChildren().addAll(gridPane, ShowEmployee.getInstance()
                        .getGridPane((Employee) tableView.getSelectionModel().getSelectedItem()));
            } else {
                hBox.getChildren().add(gridPane);
            }
        });
        gridPane.add(tableView, 0, 2);
        hBox.getChildren().add(gridPane);

        return hBox;
    }
}

class CreateEmployee {
    private static CreateEmployee instance;

    private CreateEmployee() {
    }

    static CreateEmployee getInstance() {
        if (instance == null) {
            instance = new CreateEmployee();
        }
        return instance;
    }

    GridPane getGridPane() {
        GridPane gridPane = new GridPane();
        RequiredFieldValidator validator = new RequiredFieldValidator();

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
        fieldName.getValidators().add(validator);
        gridPane.add(fieldName, 0, 6, 2, 1);

        String[] options = { "MECÁNICO", "ADMINISTRADOR", "RECEPTOR/PAGADOR" };
        ObservableList observableList = FXCollections.observableArrayList(options);
        ObservableList<String> data = observableList;
        JFXComboBox<String> comboBoxRol = new JFXComboBox<>(data);
        comboBoxRol.setPromptText("ROL");
        comboBoxRol.setLabelFloat(true);
        comboBoxRol.setPrefWidth(x);
        gridPane.add(comboBoxRol, 0, 7, 2, 1);

        JFXTextField fieldUser = new JFXTextField();
        fieldUser.setPromptText("USUARIO");
        fieldUser.setLabelFloat(true);
        fieldUser.setPrefWidth(x);
        fieldUser.getValidators().add(validator);
        gridPane.add(fieldUser, 0, 8, 2, 1);

        JFXPasswordField fieldPassword = new JFXPasswordField();
        fieldPassword.setPromptText("CONTRASEÑA");
        fieldPassword.setLabelFloat(true);
        fieldPassword.setPrefWidth(x);
        gridPane.add(fieldPassword, 0, 9, 2, 1);

        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y / 20);
        buttonAdd.setOnAction(event -> {
            if (fieldName.getText().length() == 0 || comboBoxRol.getSelectionModel().getSelectedItem() == null
                    || fieldUser.getText().length() == 0 || fieldPassword.getText().length() == 0) {
                Alert.getInstance().showAlert(gridPane, "ERROR", "UNO O MÁS DATOS SON INCORRECTOS");
            } else {
                Employee employee = ControllerEmployee.getInstance().searchEmployeeName(fieldName.getText());
                User user = ControllerUser.getInstance().searchUserName(fieldUser.getText());
                if (user != null) {
                    Alert.getInstance().showAlert(gridPane, "ERROR", "EL USUARIO YA ESTÁ REGISTRADO");
                } else if (employee != null) {
                    Alert.getInstance().showAlert(gridPane, "ERROR", "EL EMPLEADO YA ESTÁ REGISTRADO");
                } else {
                    boolean userRole = comboBoxRol.getSelectionModel().getSelectedItem().equals("ADMINISTRADOR");
                    ControllerEmployee.getInstance().createEmployee(fieldName.getText().trim().toUpperCase(),
                            comboBoxRol.getSelectionModel().getSelectedItem(), fieldUser.getText().trim().toUpperCase(),
                            fieldPassword.getText().trim().toUpperCase(), userRole);
                    ViewEmployee.getInstance().updateTableViewItems();
                    Alert.getInstance().showNotification("EMPLEADO", "EMPLEADO AGREGADO EXITOSAMENTE");
                }
            }
        });
        gridPane.add(buttonAdd, 0, 10);

        JFXButton buttonCancel = new JFXButton("CANCELAR");
        buttonCancel.getStyleClass().addAll("customButton", "dangerButton");
        buttonCancel.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCancel.setPrefSize(x, y / 20);
        buttonCancel.setOnAction(event -> ViewEmployee.getInstance().restartHBox());
        gridPane.add(buttonCancel, 1, 10);

        return gridPane;
    }
}

class UpdateEmployee {
    private static UpdateEmployee instance;

    private UpdateEmployee() {
    }

    static UpdateEmployee getInstance() {
        if (instance == null) {
            instance = new UpdateEmployee();
        }
        return instance;
    }

    GridPane getGridPane(Employee employee) {
        GridPane gridPane = new GridPane();
        RequiredFieldValidator validator = new RequiredFieldValidator();

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

        JFXTextField fieldName = new JFXTextField(employee.getName());
        fieldName.setPromptText("NOMBRE");
        fieldName.setLabelFloat(true);
        fieldName.setPrefWidth(x);
        fieldName.getValidators().add(validator);
        gridPane.add(fieldName, 0, 6, 2, 1);

        String[] options = { "MECÁNICO", "ADMINISTRADOR", "RECEPTOR/PAGADOR" };
        ObservableList observableList = FXCollections.observableArrayList(options);
        ObservableList<String> data = observableList;
        JFXComboBox<String> comboBoxRol = new JFXComboBox<>(data);
        comboBoxRol.setPromptText("ROL");
        comboBoxRol.getSelectionModel().select(employee.getRole());
        comboBoxRol.setLabelFloat(true);
        comboBoxRol.setPrefWidth(x);
        gridPane.add(comboBoxRol, 0, 7, 2, 1);

        JFXTextField fieldUser = new JFXTextField(employee.getUser().getUserName());
        fieldUser.setPromptText("USUARIO");
        fieldUser.setLabelFloat(true);
        fieldUser.setPrefWidth(x);
        fieldUser.getValidators().add(validator);
        gridPane.add(fieldUser, 0, 8, 2, 1);

        JFXPasswordField fieldPassword = new JFXPasswordField();
        fieldPassword.setPromptText("CONTRASEÑA");
        fieldPassword.setAccessibleText(employee.getUser().getPassword());
        fieldPassword.setLabelFloat(true);
        fieldPassword.setPrefWidth(x);
        gridPane.add(fieldPassword, 0, 9, 2, 1);

        JFXButton buttonUpdate = new JFXButton("MODIFICAR");
        buttonUpdate.getStyleClass().addAll("customButton", "primaryButton");
        buttonUpdate.setButtonType(JFXButton.ButtonType.FLAT);
        buttonUpdate.setPrefSize(x, y / 20);
        buttonUpdate.setOnAction(event -> {
            if (fieldName.getText().length() == 0 || comboBoxRol.getSelectionModel().getSelectedItem() == null
                    || fieldUser.getText().length() == 0 || fieldPassword.getText().length() == 0) {
                Alert.getInstance().showAlert(gridPane, "ERROR", "UNO O MÁS DATOS SON INCORRECTOS");
            } else {
                boolean userRole = comboBoxRol.getSelectionModel().getSelectedItem().equals("ADMINISTRADOR");
                ControllerEmployee.getInstance().updateEmployee(employee.getId(),
                        fieldName.getText().trim().toUpperCase(), comboBoxRol.getSelectionModel().getSelectedItem(),
                        fieldUser.getText().trim().toUpperCase(), fieldPassword.getText().trim().toUpperCase(),
                        userRole);
                if (ControllerEmployee.getInstance().updateEmployee()) {
                    Alert.getInstance().showNotification("EMPLEADO", "EMPLEADO ACTUALIZADO EXITOSAMENTE");
                } else {
                    Alert.getInstance().showAlert(gridPane, "ERROR", "ERROR AL MODIFICAR EMPLEADO");
                }
                ViewEmployee.getInstance().updateTableViewItems();
            }
        });
        gridPane.add(buttonUpdate, 0, 10);

        JFXButton buttonCancel = new JFXButton("CANCELAR");
        buttonCancel.getStyleClass().addAll("customButton", "dangerButton");
        buttonCancel.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCancel.setPrefSize(x, y / 20);
        buttonCancel.setOnAction(event -> ViewEmployee.getInstance().restartHBox());
        gridPane.add(buttonCancel, 1, 10);

        return gridPane;
    }
}

class ShowEmployee {
    private static ShowEmployee instance;

    private ShowEmployee() {
    }

    static ShowEmployee getInstance() {
        if (instance == null) {
            instance = new ShowEmployee();
        }
        return instance;
    }

    GridPane getGridPane(Employee employee) {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(20));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("MOSTRAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 5, 2, 1);

        JFXTextField fieldName = new JFXTextField(employee.getName());
        fieldName.setPromptText("NOMBRE");
        fieldName.setLabelFloat(true);
        fieldName.setPrefWidth(x);
        fieldName.setEditable(false);
        gridPane.add(fieldName, 0, 6, 2, 1);

        JFXTextField fieldRol = new JFXTextField(employee.getRole());
        fieldRol.setPromptText("ROL");
        fieldRol.setLabelFloat(true);
        fieldRol.setPrefWidth(x);
        fieldRol.setEditable(false);
        gridPane.add(fieldRol, 0, 7, 2, 1);

        JFXTextField fieldUser = new JFXTextField(employee.getUser().getUserName());
        fieldUser.setPromptText("USUARIO");
        fieldUser.setLabelFloat(true);
        fieldUser.setPrefWidth(x);
        fieldUser.setEditable(false);
        gridPane.add(fieldUser, 0, 8, 2, 1);

        JFXButton buttonCopy = new JFXButton("COPIAR");
        buttonCopy.getStyleClass().addAll("customButton", "primaryButton");
        buttonCopy.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCopy.setPrefSize(x, y / 20);
        buttonCopy.setOnAction(event -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString("NOMBRE:        " + employee.getName() + "\nROL:         " + employee.getRole()
                    + "\nUSUARIO:     " + employee.getUser().getUserName());
            clipboard.setContent(content);
        });
        gridPane.add(buttonCopy, 0, 9);

        JFXButton buttonCancel = new JFXButton("CANCELAR");
        buttonCancel.getStyleClass().addAll("customButton", "dangerButton");
        buttonCancel.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCancel.setPrefSize(x, y / 20);
        buttonCancel.setOnAction(event -> ViewEmployee.getInstance().restartHBox());
        gridPane.add(buttonCancel, 1, 9);

        return gridPane;
    }
}
