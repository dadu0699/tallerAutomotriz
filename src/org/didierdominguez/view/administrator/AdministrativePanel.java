package org.didierdominguez.view.administrator;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.didierdominguez.Main;
import org.didierdominguez.util.ScreenSize;
import org.didierdominguez.view.login.Login;

import static javafx.geometry.Pos.TOP_RIGHT;

public class AdministrativePanel {
    private static AdministrativePanel instance;
    private VBox vBoxButtons;

    private AdministrativePanel() {
    }

    public static AdministrativePanel getInstance() {
        if (instance == null) {
            instance = new AdministrativePanel();
        }
        return instance;
    }

    private HBox getPane() {
        HBox hBox = new HBox();
        vBoxButtons = new VBox();
        VBox vBoxPanels = new VBox();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        hBox.setId("adminPanel");
        hBox.setPrefSize(x, y);
        vBoxButtons.setPrefSize((x / 4), y);
        vBoxPanels.setPrefSize((3 * x / 4), y);
        vBoxPanels.getChildren().add(ViewEmployee.getInstance().getViewEmployee());

        JFXButton buttonHeader = new JFXButton("     TALLER\nAUTOMOTRIZ");
        buttonHeader.setDisable(true);
        buttonHeader.getStyleClass().addAll("headerButton", "panelButton");
        buttonHeader.setPrefSize(x, 2 * y / 8);
        buttonHeader.setButtonType(JFXButton.ButtonType.FLAT);

        JFXButton buttonEmployees = new JFXButton("EMPLEADOS");
        buttonEmployees.setId("buttonEmployees");
        buttonEmployees.getStyleClass().addAll("panelButton", "primaryButton");
        buttonEmployees.setPrefSize(x, y / 8);
        buttonEmployees.setButtonType(JFXButton.ButtonType.FLAT);
        selectButton(buttonEmployees);
        buttonEmployees.setOnAction(event -> {
            selectButton(buttonEmployees);
            vBoxPanels.getChildren().clear();
            vBoxPanels.getChildren().add(ViewEmployee.getInstance().getViewEmployee());
        });

        JFXButton buttonSpareParts = new JFXButton("REPUESTOS");
        buttonSpareParts.setId("buttonSpareParts");
        buttonSpareParts.getStyleClass().addAll("panelButton", "primaryButton");
        buttonSpareParts.setPrefSize(x, y / 8);
        buttonSpareParts.setButtonType(JFXButton.ButtonType.FLAT);
        buttonSpareParts.setOnAction(event -> {
            selectButton(buttonSpareParts);
            vBoxPanels.getChildren().clear();
            vBoxPanels.getChildren().add(ViewSparePart.getInstance().getViewSparePart());
        });

        JFXButton buttonServices = new JFXButton("SERVICIOS");
        buttonServices.setId("buttonServices");
        buttonServices.getStyleClass().addAll("panelButton", "primaryButton");
        buttonServices.setPrefSize(x, y / 8);
        buttonServices.setButtonType(JFXButton.ButtonType.FLAT);
        buttonServices.setOnAction(event -> {
            selectButton(buttonServices);
            vBoxPanels.getChildren().clear();
            vBoxPanels.getChildren().add(ViewService.getInstance().getViewService());
        });

        JFXButton buttonCustomersCars = new JFXButton("    CLIENTES\nAUTOMOVILES");
        buttonCustomersCars.setId("buttonCustomersCars");
        buttonCustomersCars.getStyleClass().addAll("panelButton", "primaryButton");
        buttonCustomersCars.setPrefSize(x, y / 8);
        buttonCustomersCars.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCustomersCars.setOnAction(event -> {
            selectButton(buttonCustomersCars);
            vBoxPanels.getChildren().clear();
            vBoxPanels.getChildren().add(ViewCustomer.getInstance().getViewCustomerDetail());
        });

        JFXButton buttonProgressCar = new JFXButton("   PROGRESO\nAUTOMOVILES");
        buttonProgressCar.setId("buttonProgressCar");
        buttonProgressCar.getStyleClass().addAll("panelButton", "primaryButton");
        buttonProgressCar.setPrefSize(x, y / 8);
        buttonProgressCar.setButtonType(JFXButton.ButtonType.FLAT);
        buttonProgressCar.setOnAction(event -> {
            selectButton(buttonProgressCar);
            vBoxPanels.getChildren().clear();
            vBoxPanels.getChildren().add(ViewQueue.getInstance().getView());
        });

        JFXButton buttonReport = new JFXButton("REPORTES");
        buttonReport.setId("buttonReport");
        buttonReport.getStyleClass().addAll("panelButton", "primaryButton");
        buttonReport.setPrefSize(x, y / 8);
        buttonReport.setButtonType(JFXButton.ButtonType.FLAT);
        buttonReport.setOnAction(event -> {
            selectButton(buttonReport);
            vBoxPanels.getChildren().clear();
            vBoxPanels.getChildren().add(ViewReport.getInstance().getViewReport());
        });

        JFXButton buttonLogOut = new JFXButton("CERRAR SESIÓN");
        buttonLogOut.setId("buttonLogOut");
        buttonLogOut.getStyleClass().addAll("panelButton", "primaryButton");
        buttonLogOut.setPrefSize(x, y / 8);
        buttonLogOut.setButtonType(JFXButton.ButtonType.FLAT);
        buttonLogOut.setOnAction(event -> Login.getInstance().showWindow());

        vBoxButtons.getChildren().addAll(buttonHeader, buttonEmployees, buttonSpareParts, buttonServices,
                buttonCustomersCars, buttonProgressCar, buttonReport, buttonLogOut);
        hBox.getChildren().addAll(vBoxButtons, vBoxPanels);

        return hBox;
    }

    private void selectButton(JFXButton jfxButton) {
        for (Node node : vBoxButtons.getChildren()) {
            if (node instanceof JFXButton) {
                if (node.getId() != null) {
                    node.getStyleClass().remove("selectedPanelButton");
                    node.getStyleClass().add("primaryButton");
                }
            }
        }
        jfxButton.getStyleClass().remove("primaryButton");
        jfxButton.getStyleClass().add("selectedPanelButton");
    }

    public void showWindow() {
        Stage stage = Main.getStage();
        VBox root = Main.getRoot();

        root.getChildren().clear();

        stage.hide();
        stage.setWidth(ScreenSize.getInstance().getX());
        stage.setHeight(720);
        stage.centerOnScreen();
        stage.setMaximized(true);

        root.getChildren().addAll(getPane());
        stage.show();
    }
}
