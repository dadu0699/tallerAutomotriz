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
import org.didierdominguez.util.ScreenSize;

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
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("CREAR UNA CUENTA");
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

        JFXButton buttonSignUp = new JFXButton("Registrarse");
        buttonSignUp.getStyleClass().addAll("customButton", "primaryButton");
        buttonSignUp.setButtonType(JFXButton.ButtonType.FLAT);
        buttonSignUp.setPrefSize(x, y);
        GridPane.setMargin(buttonSignUp, new Insets(5, 0, 0, 0));
        gridPane.add(buttonSignUp, 0, 3);

        Text label = new Text("¿Ya eres usuario?");
        label.getStyleClass().add("textTitlehref");
        GridPane.setHalignment(label, HPos.RIGHT);
        GridPane.setMargin(label, new Insets(-5, 80, 0, 0));
        gridPane.add(label, 0, 4);

        Text text = new Text("Iniciar sesión");
        text.getStyleClass().add("texthref");
        GridPane.setHalignment(text, HPos.RIGHT);
        GridPane.setMargin(text, new Insets(-5, 0, 0, 0));
        gridPane.add(text, 0, 4);
        text.setOnMouseClicked(event -> Login.getInstance().showWindow());

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

        root.getChildren().addAll(getSignup());
        stage.show();
    }
}
