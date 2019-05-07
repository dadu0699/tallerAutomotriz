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
import org.didierdominguez.bean.User;
import org.didierdominguez.controller.ControllerUser;
import org.didierdominguez.util.ScreenSize;
import org.didierdominguez.view.administrator.AdministrativePanel;

import java.io.IOException;

public class Login {
    private static Login instance;

    private Login() {
    }

    public static Login getInstance() {
        if (instance == null) {
            instance = new Login();
        }
        return instance;
    }

    public GridPane getLogin() {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setId("gridPaneAccessWindow");
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(20));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("INICIAR SESIÓN");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 0);

        JFXTextField fieldUser = new JFXTextField();
        fieldUser.setPromptText("Usuario");
        fieldUser.setId("fieldUser");
        fieldUser.setPrefSize(x, y);
        gridPane.add(fieldUser, 0, 1);

        JFXPasswordField fieldPassword = new JFXPasswordField();
        fieldPassword.setPromptText("Contraseña");
        fieldPassword.setId("fieldPassword");
        fieldPassword.setPrefSize(x, y);
        gridPane.add(fieldPassword, 0, 2);

        JFXButton buttonSignIn = new JFXButton("Iniciar sesión");
        buttonSignIn.getStyleClass().addAll("customButton", "primaryButton");
        buttonSignIn.setButtonType(JFXButton.ButtonType.FLAT);
        buttonSignIn.setPrefSize(x, y);
        buttonSignIn.setOnAction(event -> {
            User user = ControllerUser.getInstance().searchUserName(fieldUser.getText().trim().toUpperCase());
            if (user != null) {
                if (user.getRole() && user.getPassword().equals(fieldPassword.getText())) {
                    AdministrativePanel.getInstance().showWindow();
                }
            }
        });
        GridPane.setMargin(buttonSignIn, new Insets(5, 0, 0, 0));
        gridPane.add(buttonSignIn, 0, 3);

        Text label = new Text("¿Nuevo en el sitio?");
        label.getStyleClass().add("textTitlehref");
        GridPane.setHalignment(label, HPos.RIGHT);
        GridPane.setMargin(label, new Insets(-5, 105, 0, 0));
        gridPane.add(label, 0, 4);

        Text text = new Text("Crear una cuenta");
        text.getStyleClass().add("texthref");
        GridPane.setHalignment(text, HPos.RIGHT);
        GridPane.setMargin(text, new Insets(-5, 0, 0, 0));
        gridPane.add(text, 0, 4);
        text.setOnMouseClicked(event -> Signup.getInstance().showWindow());

        return gridPane;
    }

    public void showWindow() {
        Stage stage = Main.getStage();
        VBox root = Main.getRoot();

        root.getChildren().clear();

        stage.hide();
        stage.setWidth(480);
        stage.setHeight(270);
        stage.setMaximized(false);

        root.getChildren().addAll(getLogin());
        stage.show();
    }
}
