package org.didierdominguez.view.administrator;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
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
import org.didierdominguez.bean.SparePart;
import org.didierdominguez.controller.ControllerSparePart;
import org.didierdominguez.list.SimpleList.SimpleNode;
import org.didierdominguez.util.Alert;
import org.didierdominguez.util.FileControl;
import org.didierdominguez.util.ScreenSize;
import org.didierdominguez.util.Verifications;

import java.util.ArrayList;

public class ViewSparePart extends Stage {
    private static ViewSparePart instance;
    private HBox hBox;
    private GridPane gridPane;
    private TableView tableView;
    private ObservableList observableList;

    private ViewSparePart() {
    }

    public static ViewSparePart getInstance() {
        if (instance == null) {
            instance = new ViewSparePart();
        }
        return instance;
    }

    public void restartHBox() {
        hBox.getChildren().clear();
        hBox.getChildren().add(gridPane);
    }

    private void updateObservableList() {
        ArrayList<SparePart> arrayListSparePart = new ArrayList<>();
        SimpleNode auxiliaryNode = ControllerSparePart.getInstance().getSparePartList().getFirstNode();
        while (auxiliaryNode != null) {
            arrayListSparePart.add((SparePart) auxiliaryNode.getObject());
            auxiliaryNode = auxiliaryNode.getNextNode();
        }
        if (observableList != null) {
            observableList.clear();
        }
        observableList = FXCollections.observableArrayList(arrayListSparePart);
    }

    public void updateTableViewItems() {
        updateObservableList();
        tableView.setItems(observableList);
    }

    public HBox getViewSparePart() {
        hBox = new HBox();
        gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 10, 20, 20));
        // gridPane.setGridLinesVisible(true);
        gridPane.setMinWidth(x / 2);
        gridPane.setPrefSize(x, y);
        hBox.setPrefSize(x, y);

        Text textTitle = new Text("REPUESTOS");
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
            FileControl.getInstance().uploadFile("SparePartFile", "*.tmr");
            ArrayList<String> arrayList = FileControl.getInstance().readFile();
            if (arrayList != null) {
                for (String command: arrayList) {
                    String[] params = command.split("-");
                    ControllerSparePart.getInstance().createSparePart(params[0], params[1], params[2],
                            Integer.valueOf(params[3]), Double.parseDouble(params[4]));
                }
                updateTableViewItems();
                Alert.getInstance().showNotification("REPUESTOS", "ARCHIVO LEIDO EXITOSAMENTE");
            }
        });

        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y);
        buttonAdd.setOnAction(event -> {
            hBox.getChildren().clear();
            hBox.getChildren().addAll(gridPane, CreateSparePart.getInstance().getGridPane());
        });

        JFXButton buttonUpdate = new JFXButton("MODIFICAR");
        buttonUpdate.getStyleClass().addAll("customButton", "warningButton");
        buttonUpdate.setButtonType(JFXButton.ButtonType.FLAT);
        buttonUpdate.setPrefSize(x, y);
        buttonUpdate.setOnAction(event -> {
            hBox.getChildren().clear();
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                hBox.getChildren().addAll(gridPane, UpdateSparePart.getInstance()
                        .getGridPane((SparePart) tableView.getSelectionModel().getSelectedItem()));
            } else {
                hBox.getChildren().add(gridPane);
            }
        });

        JFXButton buttonDelete = new JFXButton("ELIMINAR");
        buttonDelete.getStyleClass().addAll("customButton", "dangerButton");
        buttonDelete.setButtonType(JFXButton.ButtonType.FLAT);
        buttonDelete.setPrefSize(x, y);
        buttonDelete.setOnAction(event -> {
           SparePart sparePart = (SparePart) tableView.getSelectionModel().getSelectedItem();
            if (sparePart != null) {
                restartHBox();
                ControllerSparePart.getInstance().deleteSparePart(sparePart.getId());
                updateTableViewItems();
                Alert.getInstance().showNotification("REPUESTOS", "REPUESTO ELIMINADO EXITOSAMENTE");
            }
        });

        hBoxButtons.getChildren().addAll(textFieldSearch, buttonFile, buttonAdd, buttonUpdate, buttonDelete);
        hBoxButtons.setPrefSize(x, y / 8);
        hBoxButtons.setMargin(textFieldSearch, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonFile, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonAdd, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonUpdate, new Insets(0, 5, 0, 0));
        gridPane.add(hBoxButtons, 0, 1);

        TableColumn<SparePart, Integer> columnID = new TableColumn<>("ID");
        columnID.setPrefWidth(50);
        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<SparePart, String> columnName = new TableColumn<>("NOMBRE");
        columnName.setPrefWidth((3 * x / 4) / 6);
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<SparePart, String> columnBrand = new TableColumn<>("MARCA");
        columnBrand.setPrefWidth((3 * x / 4) / 6);
        columnBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        TableColumn<SparePart, String> columnModel = new TableColumn<>("MODELO");
        columnModel.setPrefWidth((3 * x / 4) / 6);
        columnModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        TableColumn<SparePart, Integer> columnStock = new TableColumn<>("STOCK");
        columnStock.setPrefWidth((3 * x / 4) / 6);
        columnStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<SparePart, Double> columnPrice = new TableColumn<>("PRECIO");
        columnPrice.setPrefWidth((3 * x / 4) / 6);
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        updateObservableList();
        tableView = new TableView<>(observableList);
        tableView.getColumns().addAll(columnID, columnName, columnBrand, columnModel, columnStock, columnPrice);
        tableView.setPrefSize(x, 7 * y / 8);
        tableView.setOnMouseClicked(event -> {
            hBox.getChildren().clear();
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                hBox.getChildren().addAll(gridPane, ShowSparePart.getInstance().getGridPane((SparePart)
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

class CreateSparePart {
    private static CreateSparePart instance;

    private CreateSparePart() {
    }

    static CreateSparePart getInstance() {
        if (instance == null) {
            instance = new CreateSparePart();
        }
        return instance;
    }

    GridPane getGridPane() {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(20, 20, 20, 10));
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

        Label labelStock = new Label("MONTO:");
        gridPane.add(labelStock, 0, 9);
        Spinner<Integer> spinnerStock = new Spinner<>(0.00, 100000.00, 0, 1);
        spinnerStock.setEditable(true);
        spinnerStock.setPrefWidth(x);
        gridPane.add(spinnerStock, 1, 9);

        Label labelPrice = new Label("PRECIO:");
        gridPane.add(labelPrice, 0, 10);
        Spinner<Double> spinnerPrice = new Spinner<>(0.00, 100000.00, 0, 1);
        spinnerPrice.setEditable(true);
        spinnerPrice.setPrefWidth(x);
        gridPane.add(spinnerPrice, 1, 10);

        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y / 20);
        buttonAdd.setOnAction(event -> {
            if (fieldName.getText().length() == 0
                    || fieldBrand.getText().length() == 0
                    || fieldModel.getText().length() == 0
                    || !Verifications.getInstance().isNumericInteger(spinnerStock.getEditor().getText())
                    || Integer.parseInt(spinnerStock.getEditor().getText()) <= 0
                    || !Verifications.getInstance().isNumericDouble(spinnerPrice.getEditor().getText())
                    || Double.parseDouble(spinnerPrice.getEditor().getText()) <= 0) {
                Alert.getInstance().showAlert(gridPane, "ERROR", "UNO O MÁS DATOS SON INCORRECTOS");
            } else {
                SparePart sparePart = ControllerSparePart.getInstance().searchSparePart(fieldName.getText().trim(),
                        fieldBrand.getText().trim(),
                        fieldModel.getText().trim());
                if (sparePart != null) {
                    Alert.getInstance().showAlert(gridPane, "ERROR", "EL REPUESTO YA ESTÁ REGISTRADO");
                } else {
                    ControllerSparePart.getInstance().createSparePart(fieldName.getText().trim().toUpperCase(),
                            fieldBrand.getText().trim().toUpperCase(), fieldModel.getText().trim().toUpperCase(),
                            Integer.parseInt(spinnerStock.getEditor().getText().trim()),
                            Double.parseDouble(spinnerPrice.getEditor().getText().trim()));
                    ViewSparePart.getInstance().updateTableViewItems();
                    Alert.getInstance().showNotification("REPUESTOS", "REPUESTO AGREGADO EXITOSAMENTE");
                }
            }
        });
        gridPane.add(buttonAdd, 0, 11);

        JFXButton buttonCancel = new JFXButton("CANCELAR");
        buttonCancel.getStyleClass().addAll("customButton", "dangerButton");
        buttonCancel.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCancel.setPrefSize(x, y / 20);
        buttonCancel.setOnAction(event -> ViewSparePart.getInstance().restartHBox());
        gridPane.add(buttonCancel, 1, 11);

        return gridPane;
    }
}

class UpdateSparePart {
    private static UpdateSparePart instance;

    private UpdateSparePart() {
    }

    static UpdateSparePart getInstance() {
        if (instance == null) {
            instance = new UpdateSparePart();
        }
        return instance;
    }

    GridPane getGridPane(SparePart sparePart) {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(20, 20, 20, 10));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("MODIFICAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 5, 2, 1);

        JFXTextField fieldName = new JFXTextField(sparePart.getName());
        fieldName.setPromptText("NOMBRE");
        fieldName.setLabelFloat(true);
        fieldName.setPrefWidth(x);
        gridPane.add(fieldName, 0, 6, 2, 1);

        JFXTextField fieldBrand = new JFXTextField(sparePart.getBrand());
        fieldBrand.setPromptText("MARCA");
        fieldBrand.setLabelFloat(true);
        fieldBrand.setPrefWidth(x);
        gridPane.add(fieldBrand, 0, 7, 2, 1);

        JFXTextField fieldModel = new JFXTextField(sparePart.getModel());
        fieldModel.setPromptText("MODELO");
        fieldModel.setLabelFloat(true);
        fieldModel.setPrefWidth(x);
        gridPane.add(fieldModel, 0, 8, 2, 1);

        Label labelStock = new Label("MONTO:");
        gridPane.add(labelStock, 0, 9);
        Spinner<Integer> spinnerStock = new Spinner<>(0.00, 100000.00, sparePart.getStock(), 1);
        spinnerStock.setEditable(true);
        spinnerStock.setPrefWidth(x);
        gridPane.add(spinnerStock, 1, 9);

        Label labelPrice = new Label("PRECIO:");
        gridPane.add(labelPrice, 0, 10);
        Spinner<Double> spinnerPrice = new Spinner<>(0.00, 100000.00, sparePart.getPrice(), 1);
        spinnerPrice.setEditable(true);
        spinnerPrice.setPrefWidth(x);
        gridPane.add(spinnerPrice, 1, 10);

        JFXButton buttonUpdate = new JFXButton("MODIFICAR");
        buttonUpdate.getStyleClass().addAll("customButton", "primaryButton");
        buttonUpdate.setButtonType(JFXButton.ButtonType.FLAT);
        buttonUpdate.setPrefSize(x, y / 20);
        buttonUpdate.setOnAction(event -> {
            if (fieldName.getText().length() == 0
                    || fieldBrand.getText().length() == 0
                    || fieldModel.getText().length() == 0
                    || !Verifications.getInstance().isNumericInteger(spinnerStock.getEditor().getText())
                    || Integer.parseInt(spinnerStock.getEditor().getText()) <= 0
                    || !Verifications.getInstance().isNumericDouble(spinnerPrice.getEditor().getText())
                    || Double.parseDouble(spinnerPrice.getEditor().getText()) <= 0) {
                Alert.getInstance().showAlert(gridPane, "ERROR", "UNO O MÁS DATOS SON INCORRECTOS");
            } else {
                ControllerSparePart.getInstance().updateSparePart(sparePart.getId(),
                        fieldName.getText().trim().toUpperCase(),
                        fieldBrand.getText().trim().toUpperCase(), fieldModel.getText().trim().toUpperCase(),
                        Integer.parseInt(spinnerStock.getEditor().getText().trim()),
                        Double.parseDouble(spinnerPrice.getEditor().getText().trim()));
                if (ControllerSparePart.getInstance().updateSparePart()) {
                    Alert.getInstance().showNotification("REPUESTOS", "REPUESTO ACTUALIZADO EXITOSAMENTE");
                } else {
                    Alert.getInstance().showAlert(gridPane, "ERROR", "ERROR AL MODIFICAR REPUESTO");
                }
                ViewSparePart.getInstance().updateTableViewItems();
            }
        });
        gridPane.add(buttonUpdate, 0, 11);

        JFXButton buttonCancel = new JFXButton("CANCELAR");
        buttonCancel.getStyleClass().addAll("customButton", "dangerButton");
        buttonCancel.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCancel.setPrefSize(x, y / 20);
        buttonCancel.setOnAction(event -> ViewSparePart.getInstance().restartHBox());
        gridPane.add(buttonCancel, 1, 11);

        return gridPane;
    }
}

class ShowSparePart {
    private static ShowSparePart instance;

    private ShowSparePart() {
    }

    static ShowSparePart getInstance() {
        if (instance == null) {
            instance = new ShowSparePart();
        }
        return instance;
    }

    GridPane getGridPane(SparePart sparePart) {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(20, 20, 20, 10));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("MOSTRAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 5, 2, 1);

        JFXTextField fieldName = new JFXTextField(sparePart.getName());
        fieldName.setPromptText("NOMBRE");
        fieldName.setLabelFloat(true);
        fieldName.setPrefWidth(x);
        fieldName.setEditable(false);
        gridPane.add(fieldName, 0, 6, 2, 1);

        JFXTextField fieldBrand = new JFXTextField(sparePart.getBrand());
        fieldBrand.setPromptText("MARCA");
        fieldBrand.setLabelFloat(true);
        fieldBrand.setPrefWidth(x);
        fieldBrand.setEditable(false);
        gridPane.add(fieldBrand, 0, 7, 2, 1);

        JFXTextField fieldModel = new JFXTextField(sparePart.getModel());
        fieldModel.setPromptText("MODELO");
        fieldModel.setLabelFloat(true);
        fieldModel.setPrefWidth(x);
        fieldModel.setEditable(false);
        gridPane.add(fieldModel, 0, 8, 2, 1);

        JFXTextField fieldStock = new JFXTextField(String.valueOf(sparePart.getStock()));
        fieldStock.setPromptText("MONTO");
        fieldStock.setLabelFloat(true);
        fieldStock.setPrefWidth(x);
        fieldStock.setEditable(false);
        gridPane.add(fieldStock, 0, 9, 2, 1);

        JFXTextField fieldPrice = new JFXTextField(String.valueOf(sparePart.getPrice()));
        fieldPrice.setPromptText("MONTO");
        fieldPrice.setLabelFloat(true);
        fieldPrice.setPrefWidth(x);
        fieldPrice.setEditable(false);
        gridPane.add(fieldPrice, 0, 10, 2, 1);

        JFXButton buttonCopy = new JFXButton("COPIAR");
        buttonCopy.getStyleClass().addAll("customButton", "primaryButton");
        buttonCopy.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCopy.setPrefSize(x, y / 20);
        buttonCopy.setOnAction(event -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString("NOMBRE:       " + sparePart.getName()
                    + "\nMARCA:      " + sparePart.getBrand()
                    + "\nMODELO:     " + sparePart.getModel()
                    + "\nMONTO:      " + sparePart.getStock()
                    + "\nPRECIO:     " + sparePart.getPrice());
            clipboard.setContent(content);
        });
        gridPane.add(buttonCopy, 0, 11);

        JFXButton buttonCancel = new JFXButton("CANCELAR");
        buttonCancel.getStyleClass().addAll("customButton", "dangerButton");
        buttonCancel.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCancel.setPrefSize(x, y / 20);
        buttonCancel.setOnAction(event -> ViewSparePart.getInstance().restartHBox());
        gridPane.add(buttonCancel, 1, 11);

        return gridPane;
    }
}
