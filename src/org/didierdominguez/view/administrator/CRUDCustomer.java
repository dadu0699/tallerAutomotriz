package org.didierdominguez.view.administrator;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.didierdominguez.bean.Customer;
import org.didierdominguez.bean.User;
import org.didierdominguez.controller.ControllerCustomer;
import org.didierdominguez.controller.ControllerUser;
import org.didierdominguez.util.Alert;
import org.didierdominguez.util.ScreenSize;
import org.didierdominguez.util.Verifications;

public class CRUDCustomer {
}

class CreateCustomer {
    private static CreateCustomer instance;

    private CreateCustomer() {}

    static CreateCustomer getInstance() {
        if (instance == null) {
            instance = new CreateCustomer();
        }
        return instance;
    }

    public GridPane getGridPane() {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setPadding(new Insets(20));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("AGREGAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(20));
        gridPane.add(textTitle, 0, 0);

        JFXTextField fieldID = new JFXTextField();
        fieldID.setPromptText("ID / DPI");
        fieldID.setLabelFloat(true);
        fieldID.setPrefWidth(x);
        gridPane.add(fieldID, 0, 1);

        JFXTextField fieldName = new JFXTextField();
        fieldName.setPromptText("NOMBRE");
        fieldName.setLabelFloat(true);
        fieldName.setPrefWidth(x);
        gridPane.add(fieldName, 0, 2);

        JFXTextField fieldUser = new JFXTextField();
        fieldUser.setPromptText("USUARIO");
        fieldUser.setLabelFloat(true);
        fieldUser.setPrefWidth(x);
        gridPane.add(fieldUser, 0, 3);

        JFXPasswordField fieldPassword = new JFXPasswordField();
        fieldPassword.setPromptText("CONTRASEÑA");
        fieldPassword.setLabelFloat(true);
        fieldPassword.setPrefWidth(x);
        gridPane.add(fieldPassword, 0, 4);

        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y / 2);
        buttonAdd.setOnAction(event -> {
            if (fieldID.getText().length() == 0
                    || !Verifications.getInstance().isNumericInteger(fieldID.getText().trim())
                    || fieldName.getText().length() == 0
                    || fieldUser.getText().length() == 0
                    || fieldPassword.getText().length() == 0) {
                Alert.getInstance().showAlert(gridPane, "ERROR", "UNO O MÁS DATOS SON INCORRECTOS");
            } else {
                Customer customer = ControllerCustomer.getInstance().searchCustomer(Integer.parseInt(fieldID.getText().trim()));
                User user = ControllerUser.getInstance().searchUser(fieldUser.getText());
                if (user != null) {
                    Alert.getInstance().showAlert(gridPane, "ERROR", "EL USUARIO YA ESTÁ REGISTRADO");
                } else if (customer != null) {
                    Alert.getInstance().showAlert(gridPane, "ERROR", "EL CLIENTE YA ESTÁ REGISTRADO");
                } else {
                    ControllerCustomer.getInstance().createCustomer(Integer.parseInt(fieldID.getText().trim()),
                            fieldName.getText().trim().toUpperCase(), false, fieldUser.getText().trim().toUpperCase(),
                            fieldPassword.getText().trim().toUpperCase());
                    ViewCustomer.getInstance().updateTableViewItemsCustomer();
                    Alert.getInstance().showNotification("CLIENTES", "CLIENTE AGREGADO EXITOSAMENTE");
                }
            }
        });
        gridPane.add(buttonAdd, 0, 5);

        gridPane.setPadding(new Insets(-10, 20, 10, 10));
        return gridPane;
    }
}

class UpdateCustomer{
    private static UpdateCustomer instance;

    private UpdateCustomer(){}

    static UpdateCustomer getInstance() {
        if (instance == null) {
            instance = new UpdateCustomer();
        }
        return instance;
    }

    GridPane getGridPane(Customer customer) {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(20));

        Text textTitle = new Text("MODIFICAR " + customer.getName());
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(20));
        gridPane.add(textTitle, 0, 0, 2, 1);

        JFXTextField fieldID = new JFXTextField(String.valueOf(customer.getId()));
        fieldID.setPromptText("ID / DPI");
        fieldID.setLabelFloat(true);
        fieldID.setPrefWidth(x);
        gridPane.add(fieldID, 0, 1, 2, 1);

        JFXTextField fieldName = new JFXTextField(customer.getName());
        fieldName.setPromptText("NOMBRE");
        fieldName.setLabelFloat(true);
        fieldName.setPrefWidth(x);
        gridPane.add(fieldName, 0, 2, 2, 1);

        JFXTextField fieldUser = new JFXTextField(customer.getUser().getUserName());
        fieldUser.setPromptText("USUARIO");
        fieldUser.setLabelFloat(true);
        fieldUser.setPrefWidth(x);
        gridPane.add(fieldUser, 0, 3, 2, 1);

        JFXPasswordField fieldPassword = new JFXPasswordField();
        fieldPassword.setPromptText("CONTRASEÑA");
        fieldPassword.setLabelFloat(true);
        fieldPassword.setPrefWidth(x);
        gridPane.add(fieldPassword, 0, 4, 2, 1);

        JFXButton buttonUpdate = new JFXButton("MODIFICAR");
        buttonUpdate.getStyleClass().addAll("customButton", "primaryButton");
        buttonUpdate.setButtonType(JFXButton.ButtonType.FLAT);
        buttonUpdate.setPrefSize(x, y / 2);
        /*buttonUpdate.setOnAction(event -> {
            if (fieldName.getText().length() == 0
                    || fieldPhoneNumber.getText().length() == 0
                    || !fieldPhoneNumber.getText().matches("^(?=(?:[1-9]){1})\\d{4}-\\d{4}")) {
                Alert.getInstance().showAlert(gridPane, "ERROR", "UNO O MÁS DATOS SON INCORRECTOS");
            } else {
                ControllerEmployee.getInstance().updateEmployee(customer.getId(),
                        fieldName.getText().trim().toUpperCase(),
                        fieldPhoneNumber.getText().trim(), "CALL-CENTER");
                if (!ControllerEmployee.getInstance().updateEmployee()){
                    Alert.getInstance().showAlert(gridPane, "ERROR", "ERROR AL MODIFICAR EMPLEADO");
                } else {
                    ViewEmployee.getInstance().restartHBox();
                }
            }
        });*/
        gridPane.add(buttonUpdate, 0, 5);

        JFXButton buttonCancel = new JFXButton("CANCELAR");
        buttonCancel.getStyleClass().addAll("customButton", "dangerButton");
        buttonCancel.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCancel.setPrefSize(x, y / 2);
        buttonCancel.setOnAction(event -> ViewCustomer.getInstance().restartHBox());
        gridPane.add(buttonCancel, 1, 5);

        gridPane.setPadding(new Insets(-10, 20, 10, 10));
        return gridPane;
    }
}