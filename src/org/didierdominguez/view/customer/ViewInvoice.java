package org.didierdominguez.view.customer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXScrollPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.didierdominguez.bean.Customer;
import org.didierdominguez.bean.Invoice;
import org.didierdominguez.controller.ControllerInvoice;
import org.didierdominguez.list.SimpleList.SimpleNode;
import org.didierdominguez.util.ScreenSize;

public class ViewInvoice extends Stage {
    private static ViewInvoice instance;

    private ViewInvoice() { }

    public static ViewInvoice getInstance() {
        if (instance == null) {
            instance = new ViewInvoice();
        }
        return instance;
    }

    public JFXScrollPane getScrollPane(Customer customer) {
        JFXScrollPane scrollPane = new JFXScrollPane();
        GridPane gridPaneCards = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        scrollPane.setPrefSize(x, y);
        gridPaneCards.setVgap(10);
        gridPaneCards.setHgap(10);
        gridPaneCards.setPadding(new Insets(20));
        gridPaneCards.setPrefSize(x, y);

        Text textTitle = new Text("FACTURAS");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(40));
        scrollPane.getBottomBar().getChildren().add(textTitle);
        scrollPane.getBottomBar().alignmentProperty().setValue(Pos.CENTER_LEFT);
        scrollPane.getBottomBar().setPadding(new Insets(0, 0, 10,30));
        JFXScrollPane.smoothScrolling((ScrollPane) scrollPane.getChildren().get(0));

        int row = 0;
        int col = 0;
        SimpleNode auxiliaryNode = ControllerInvoice.getInstance().getInvoiceList().getFirstNode();
        while (auxiliaryNode != null) {
            if (((Invoice) auxiliaryNode.getObject()).getOrder().getCustomer() == customer) {
                JFXDialogLayout layout = new JFXDialogLayout();
                layout.setHeading(new Label("FACTURA " + ((Invoice) auxiliaryNode.getObject()).getId()));
                layout.setBody(getGridPane((Invoice) auxiliaryNode.getObject()));
                layout.getStyleClass().addAll("customCard");
                layout.setPrefSize(x, y/10);
                gridPaneCards.add(layout, col, row);
                col++;
                if (col == 2) {
                    col = 0;
                    row++;
                }
            }
            auxiliaryNode = auxiliaryNode.getNextNode();
        }

        scrollPane.setContent(gridPaneCards);
        return scrollPane;
    }



    GridPane getGridPane(Invoice invoice) {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(10);
        gridPane.setHgap(20);
        gridPane.setPadding(new Insets(10));

        Label labelCar = new Label("AUTOMOVIL:");
        gridPane.add(labelCar, 0, 0);
        Label labelGetCar = new Label(invoice.getOrder().getCar().getBrand() + " "
                + invoice.getOrder().getCar().getModel());
        gridPane.add(labelGetCar, 1, 0);

        Label labelService = new Label("SERVICIO:");
        gridPane.add(labelService, 0, 1);
        Label labelGetService = new Label(invoice.getOrder().getService().getName());
        gridPane.add(labelGetService, 1, 1);

        Label labelTotal = new Label("TOTAL:");
        gridPane.add(labelTotal, 0, 2);
        Label labelGetTotal = new Label(String.valueOf(invoice.getOrder().getService().getTotal()));
        gridPane.add(labelGetTotal, 1, 2);

        if (!invoice.getState()) {
            JFXButton buttonSelect = new JFXButton("PAGAR");
            buttonSelect.getStyleClass().addAll("customButton", "primaryButton");
            buttonSelect.setButtonType(JFXButton.ButtonType.FLAT);
            buttonSelect.setPrefSize(x, y);
            buttonSelect.setOnAction(event -> {
                ControllerInvoice.getInstance().updateInvoice(invoice.getId());
                gridPane.getChildren().remove(buttonSelect);
            });
            gridPane.add(buttonSelect, 0, 3, 2, 1);
        }

        return gridPane;
    }
}
