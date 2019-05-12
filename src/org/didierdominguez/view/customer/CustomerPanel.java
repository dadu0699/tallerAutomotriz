package org.didierdominguez.view.customer;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.didierdominguez.Main;
import org.didierdominguez.bean.Customer;
import org.didierdominguez.util.ScreenSize;
import org.didierdominguez.view.login.Login;

public class CustomerPanel {
    private static CustomerPanel instance;
    private HBox hBoxButtons;
    private Customer customer;

    private CustomerPanel() {
    }

    public static CustomerPanel getInstance() {
        if (instance == null) {
            instance = new CustomerPanel();
        }
        return instance;
    }

    private VBox getPane() {
        VBox vBox = new VBox();
        hBoxButtons = new HBox();
        VBox vBoxPanels = new VBox();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        vBox.setPrefSize(x, y);
        hBoxButtons.setPrefSize(x, (y/ 12));
        vBoxPanels.setPrefSize(x, (11 * y / 12));
        vBoxPanels.getChildren().addAll(ViewCar.getInstance().getViewCar(customer));

        JFXButton buttonHeader = new JFXButton("TALLER AUTOMOTRIZ");
        buttonHeader.setDisable(true);
        buttonHeader.getStyleClass().addAll("headerNavButton", "panelButton");
        buttonHeader.setPrefSize(2 * x / 6, y);
        buttonHeader.setButtonType(JFXButton.ButtonType.FLAT);

        JFXButton buttonCars = new JFXButton("AUTOMOVILES");
        buttonCars.setId("buttonCars");
        buttonCars.getStyleClass().addAll("panelButton", "primaryButton");
        buttonCars.setPrefSize(x / 6, y);
        buttonCars.setButtonType(JFXButton.ButtonType.FLAT);
        selectButton(buttonCars);
        buttonCars.setOnAction(event -> {
            selectButton(buttonCars);
            vBoxPanels.getChildren().clear();
            vBoxPanels.getChildren().add(ViewCar.getInstance().getViewCar(customer));
        });

        JFXButton buttonProgressCar = new JFXButton("PROGRESO AUTOMOVILES");
        buttonProgressCar.setId("buttonProgressCar");
        buttonProgressCar.getStyleClass().addAll("panelButton", "primaryButton");
        buttonProgressCar.setPrefSize(x / 6, y);
        buttonProgressCar.setButtonType(JFXButton.ButtonType.FLAT);
        buttonProgressCar.setOnAction(event -> {
            selectButton(buttonProgressCar);
            vBoxPanels.getChildren().clear();
        });

        JFXButton buttonInvoice = new JFXButton("FACTURAS");
        buttonInvoice.setId("buttonServices");
        buttonInvoice.getStyleClass().addAll("panelButton", "primaryButton");
        buttonInvoice.setPrefSize(x / 6, y);
        buttonInvoice.setButtonType(JFXButton.ButtonType.FLAT);
        buttonInvoice.setOnAction(event -> {
            selectButton(buttonInvoice);
            vBoxPanels.getChildren().clear();
        });

        JFXButton buttonLogOut = new JFXButton("CERRAR SESIÓN");
        buttonLogOut.setId("buttonReport");
        buttonLogOut.getStyleClass().addAll("panelButton", "primaryButton");
        buttonLogOut.setPrefSize(x / 6, y);
        buttonLogOut.setButtonType(JFXButton.ButtonType.FLAT);
        buttonLogOut.setOnAction(event -> Login.getInstance().showWindow());

        hBoxButtons.getChildren().addAll(buttonHeader, buttonCars, buttonProgressCar, buttonInvoice, buttonLogOut);
        vBox.getChildren().addAll(hBoxButtons, vBoxPanels);

        return vBox;
    }

    private void selectButton(JFXButton jfxButton) {
        for (Node node : hBoxButtons.getChildren()) {
            if (node instanceof JFXButton) {
                if (node.getId() != null) {
                    node.getStyleClass().remove("selectedPanelNavButton");
                    node.getStyleClass().add("primaryButton");
                }
            }
        }
        jfxButton.getStyleClass().remove("primaryButton");
        jfxButton.getStyleClass().add("selectedPanelNavButton");
    }

    public void showWindow(Customer customer) {
        Stage stage = Main.getStage();
        VBox root = Main.getRoot();
        this.customer = customer;

        root.getChildren().clear();

        stage.hide();
        stage.setWidth(848);
        stage.setHeight(480);
        stage.setMaximized(true);

        root.getChildren().addAll(getPane());
        stage.show();
    }
}
