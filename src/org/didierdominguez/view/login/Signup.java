package org.didierdominguez.view.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.didierdominguez.Main;
import org.didierdominguez.bean.Customer;
import org.didierdominguez.bean.User;
import org.didierdominguez.controller.ControllerCustomer;
import org.didierdominguez.controller.ControllerUser;
import org.didierdominguez.util.Alert;
import org.didierdominguez.util.ScreenSize;
import org.didierdominguez.util.Verifications;
import org.didierdominguez.view.customer.CustomerPanel;

public class Signup {
    private static Signup instance;

    private Signup() {
    }

    public static Signup getInstance() {
        if (instance == null) {
            instance = new Signup();
        }
        return instance;
    }

    public GridPane getSignup() {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setId("gridPaneAccessWindow");
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("CREAR UNA CUENTA");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 0);

        JFXTextField fieldID = new JFXTextField();
        fieldID.setPromptText("DPI");
        fieldID.setPrefSize(x, y);
        gridPane.add(fieldID, 0, 1);

        JFXTextField fieldName = new JFXTextField();
        fieldName.setPromptText("NOMBRE");
        fieldName.setPrefSize(x, y);
        gridPane.add(fieldName, 0, 2);

        JFXTextField fieldUser = new JFXTextField();
        fieldUser.setPromptText("USUARIO");
        fieldUser.setId("fieldUser");
        fieldUser.setPrefSize(x, y);
        gridPane.add(fieldUser, 0, 3);

        JFXPasswordField fieldPassword = new JFXPasswordField();
        fieldPassword.setPromptText("CONTRASEÑA");
        fieldPassword.setId("fieldPassword");
        fieldPassword.setPrefSize(x, y);
        gridPane.add(fieldPassword, 0, 4);

        JFXButton buttonSignUp = new JFXButton("REGISTRARSE");
        buttonSignUp.getStyleClass().addAll("customButton", "primaryButton");
        buttonSignUp.setButtonType(JFXButton.ButtonType.FLAT);
        buttonSignUp.setPrefSize(x, y);
        buttonSignUp.setOnAction(event -> {
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
                    Alert.getInstance().showAlert(gridPane, "ERROR", "EL USUARIO YA ESTÁ EN USO");
                } else if (customer != null) {
                    Alert.getInstance().showAlert(gridPane, "ERROR", "EL CLIENTE YA ESTÁ REGISTRADO");
                } else {
                    ControllerCustomer.getInstance().createCustomer(Integer.parseInt(fieldID.getText().trim()),
                            fieldName.getText().trim().toUpperCase(), false, fieldUser.getText().trim().toUpperCase(),
                            fieldPassword.getText().trim().toUpperCase());
                    CustomerPanel.getInstance().showWindow(ControllerCustomer.getInstance().searchCustomer(Integer.parseInt(fieldID.getText().trim())));
                    Alert.getInstance().showNotification("REGISTRO", "REGISTRO REALIZADO EXITOSAMENTE");
                }
            }
        });
        GridPane.setMargin(buttonSignUp, new Insets(5, 0, 0, 0));
        gridPane.add(buttonSignUp, 0, 5);

        Text label = new Text("¿YA ERES USUARIO?");
        label.getStyleClass().add("textTitlehref");
        GridPane.setHalignment(label, HPos.RIGHT);
        GridPane.setMargin(label, new Insets(-10, 100, 0, 0));
        gridPane.add(label, 0, 6);

        Text text = new Text("INICIAR SESIÓN");
        text.getStyleClass().add("texthref");
        GridPane.setHalignment(text, HPos.RIGHT);
        GridPane.setMargin(text, new Insets(-10, 0, 0, 0));
        gridPane.add(text, 0, 6);
        text.setOnMouseClicked(event -> Login.getInstance().showWindow());

        return gridPane;
    }

    public void showWindow() {
        Stage stage = Main.getStage();
        VBox root = Main.getRoot();

        root.getChildren().clear();

        stage.hide();
        stage.setWidth(480);
        stage.setHeight(335);
        stage.setMaximized(false);

        root.getChildren().addAll(getSignup());
        stage.show();
    }
}
