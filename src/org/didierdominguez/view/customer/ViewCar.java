package org.didierdominguez.view.customer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.didierdominguez.bean.Car;
import org.didierdominguez.bean.Customer;
import org.didierdominguez.controller.ControllerCar;
import org.didierdominguez.list.CircularSimpleList.CircularSimpleNode;
import org.didierdominguez.util.Alert;
import org.didierdominguez.util.FileControl;
import org.didierdominguez.util.ScreenSize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ViewCar extends Stage {
    private static ViewCar instance;
    private HBox hBox;
    private GridPane gridPane;
    private TableView tableView;
    private ObservableList observableList;
    private Customer customer;

    private ViewCar() {
    }

    public static ViewCar getInstance() {
        if (instance == null) {
            instance = new ViewCar();
        }
        return instance;
    }

    public void restartHBox() {
        hBox.getChildren().clear();
        hBox.getChildren().addAll(gridPane, CreateCar.getInstance().getGridPane(customer));
    }

    private void updateObservableList() {
        ArrayList<Car> arrayListCar = new ArrayList<>();
        CircularSimpleNode auxiliaryNode = ControllerCar.getInstance().getCarList().getNode();
        if (auxiliaryNode != null) {
            do {
                if (((Car) auxiliaryNode.getObject()).getCustomer() == customer) {
                    arrayListCar.add((Car) auxiliaryNode.getObject());
                }
                auxiliaryNode = auxiliaryNode.getNextNode();
            } while (auxiliaryNode != ControllerCar.getInstance().getCarList().getNode());
        }
        if (observableList != null) {
            observableList.clear();
        }
        observableList = FXCollections.observableArrayList(arrayListCar);
    }

    public void updateTableViewItems() {
        updateObservableList();
        tableView.setItems(observableList);
    }

    public HBox getViewCar(Customer customer) {
        hBox = new HBox();
        gridPane = new GridPane();
        this.customer = customer;

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));
        gridPane.setMaxWidth(x / 2);
        gridPane.setPrefSize(x, y);
        // gridPane.setGridLinesVisible(true);
        hBox.setPrefSize(x, y);

        Text textTitle = new Text("AUTOMOVILES");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 0);

        JFXTextField textFieldSearch = new JFXTextField();
        textFieldSearch.setPromptText("BUSCAR");
        textFieldSearch.setPrefSize(x, y / 8);
        gridPane.add(textFieldSearch, 0, 1);

        HBox hBoxButtons = new HBox();
        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y);
        buttonAdd.setOnAction(event -> restartHBox());

        JFXButton buttonUpdate = new JFXButton("MODIFICAR");
        buttonUpdate.getStyleClass().addAll("customButton", "warningButton");
        buttonUpdate.setButtonType(JFXButton.ButtonType.FLAT);
        buttonUpdate.setPrefSize(x, y);
        buttonUpdate.setOnAction(event -> {
            hBox.getChildren().clear();
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                hBox.getChildren().addAll(gridPane, UpdateCar.getInstance()
                        .getGridPane((Car) tableView.getSelectionModel().getSelectedItem()));
            } else {
                restartHBox();
            }
        });

        JFXButton buttonDelete = new JFXButton("ELIMINAR");
        buttonDelete.getStyleClass().addAll("customButton", "dangerButton");
        buttonDelete.setButtonType(JFXButton.ButtonType.FLAT);
        buttonDelete.setPrefSize(x, y);
        buttonDelete.setOnAction(event -> {
            Car car = (Car) tableView.getSelectionModel().getSelectedItem();
            if (car != null) {
                restartHBox();
                ControllerCar.getInstance().deleteCar(car.getId());
                updateTableViewItems();
                Alert.getInstance().showNotification(customer.getName(), "AUTOMOVIL ELIMINADO EXITOSAMENTE");
            }
        });

        hBoxButtons.getChildren().addAll(buttonAdd, buttonUpdate, buttonDelete);
        hBoxButtons.setPrefSize(x, y / 8);
        hBoxButtons.setMargin(buttonAdd, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonUpdate, new Insets(0, 5, 0, 0));
        gridPane.add(hBoxButtons, 0, 2);

        TableColumn<Car, Integer> columnIDC = new TableColumn<>("PLACA");
        columnIDC.setPrefWidth(x / 7);
        columnIDC.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Car, String> columnBrand = new TableColumn<>("MARCA");
        columnBrand.setPrefWidth(x / 7);
        columnBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        TableColumn<Car, String> columnModel = new TableColumn<>("MODELO");
        columnModel.setPrefWidth(x / 6);
        columnModel.setCellValueFactory(new PropertyValueFactory<>("Model"));

        updateObservableList();
        tableView = new TableView<>(observableList);
        tableView.getColumns().addAll(columnIDC, columnBrand, columnModel);
        tableView.setPrefSize(x, 7 * y / 8);
        tableView.setOnMouseClicked(event -> {
            hBox.getChildren().clear();
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                hBox.getChildren().addAll(gridPane, ShowCar.getInstance()
                        .getGridPane((Car) tableView.getSelectionModel().getSelectedItem()));
            } else {
                restartHBox();
            }
        });
        gridPane.add(tableView, 0, 3);
        hBox.getChildren().addAll(gridPane, CreateCar.getInstance().getGridPane(customer));

        return hBox;
    }
}

class CreateCar {
    private static CreateCar instance;
    private String directory;
    private ImageView imageView;

    private CreateCar() {
        directory = System.getProperty("user.dir") + "/src/org/didierdominguez/assets/images/";
    }

    static CreateCar getInstance() {
        if (instance == null) {
            instance = new CreateCar();
        }
        return instance;
    }

    GridPane getGridPane(Customer customer) {
        GridPane gridPane = new GridPane();
        FileControl.getInstance().setFileControlImage(null);

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setMaxWidth(x / 2);
        gridPane.setPadding(new Insets(20));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("AGREGAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(22));
        gridPane.add(textTitle, 0, 0, 2, 1);

        try {
            Image image = new Image(new FileInputStream(directory + "upload-pic.jpg"));
            imageView = new ImageView(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setFitWidth(x/2);
        imageView.setPreserveRatio(true);
        imageView.setOnMouseClicked(event -> {
            FileControl.getInstance().uploadImage("carImage", "*.jpg");
            if (FileControl.getInstance().getFileControlImage() != null) {
                try {
                    Image image = new Image(new FileInputStream(directory
                            + FileControl.getInstance().getFileControlImage().getName()));
                    imageView.setImage(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println(FileControl.getInstance().getFileControlImage().getName());
            }
        });
        imageView.getStyleClass().addAll("uploadImageView");
        gridPane.add(imageView, 0, 1, 2, 1);
        gridPane.setMargin(imageView, new Insets(-10, -5, -25, 0));

        JFXTextField fieldID = new JFXTextField();
        fieldID.setPromptText("PLACA");
        fieldID.setLabelFloat(true);
        fieldID.setPrefWidth(x);
        gridPane.add(fieldID, 0, 3, 2 ,1);

        JFXTextField fieldBrand = new JFXTextField();
        fieldBrand.setPromptText("MARCA");
        fieldBrand.setLabelFloat(true);
        fieldBrand.setPrefWidth(x);
        gridPane.add(fieldBrand, 0, 4, 2 ,1);

        JFXTextField fieldModel = new JFXTextField();
        fieldModel.setPromptText("MODELO");
        fieldModel.setLabelFloat(true);
        fieldModel.setPrefWidth(x);
        gridPane.add(fieldModel, 0, 5, 2 ,1);

        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y / 20);
        buttonAdd.setOnAction(event -> {
            if (fieldID.getText().length() == 0
                    || fieldBrand.getText().length() == 0
                    || fieldModel.getText().length() == 0
                    || FileControl.getInstance().getFileControlImage() == null) {
                Alert.getInstance().showAlert(gridPane, "ERROR", "UNO O MÁS DATOS SON INCORRECTOS");
            } else {
                Car car = ControllerCar.getInstance().searchCar(fieldID.getText().trim());
                if (car != null) {
                    Alert.getInstance().showAlert(gridPane, "ERROR", "EL AUTOMOVIL YA ESTÁ REGISTRADO");
                } else {
                    ControllerCar.getInstance().createCar(fieldID.getText().trim(), fieldBrand.getText().trim(),
                            fieldModel.getText().trim(), FileControl.getInstance().getFileControlImage().getName(),
                            customer);
                    ViewCar.getInstance().updateTableViewItems();
                    ViewCar.getInstance().restartHBox();
                    Alert.getInstance().showNotification(customer.getName(), "AUTOMOVIL AGREGADO EXITOSAMENTE");
                }
            }
        });
        gridPane.add(buttonAdd, 0, 6);

        JFXButton buttonCancel = new JFXButton("CANCELAR");
        buttonCancel.getStyleClass().addAll("customButton", "dangerButton");
        buttonCancel.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCancel.setPrefSize(x, y / 20);
        buttonCancel.setOnAction(event -> ViewCar.getInstance().restartHBox());
        gridPane.add(buttonCancel, 1, 6);

        return gridPane;
    }
}

class UpdateCar {
    private static UpdateCar instance;
    private String directory;
    private ImageView imageView;

    private UpdateCar() {}

    static UpdateCar getInstance() {
        if (instance == null) {
            instance = new UpdateCar();
        }
        return instance;
    }

    GridPane getGridPane(Car car) {
        GridPane gridPane = new GridPane();
        this.directory = System.getProperty("user.dir") + "/src/org/didierdominguez/assets/images/";
        FileControl.getInstance().setFileControlImage(null);

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setMaxWidth(x / 2);
        gridPane.setPadding(new Insets(20));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("MODIFICAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(22));
        gridPane.add(textTitle, 0, 0, 2, 1);

        File directory = new File(this.directory + car.getPicture());
        if (directory.exists()) {
            this.directory += car.getPicture();
        } else {
            this.directory += "image-not-found.jpg";
        }
        try {
            Image image = new Image(new FileInputStream(this.directory));
            imageView = new ImageView(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setFitWidth(x/2);
        imageView.setPreserveRatio(true);
        imageView.setOnMouseClicked(event -> {
            FileControl.getInstance().uploadImage("carImage", "*.jpg");
            if (FileControl.getInstance().getFileControlImage() != null) {
                try {
                    this.directory = System.getProperty("user.dir") + "/src/org/didierdominguez/assets/images/";
                    Image image = new Image(new FileInputStream(this.directory
                            + FileControl.getInstance().getFileControlImage().getName()));
                    imageView.setImage(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println(FileControl.getInstance().getFileControlImage().getName());
            }
        });
        imageView.getStyleClass().addAll("uploadImageView");
        gridPane.add(imageView, 0, 1, 2, 1);
        gridPane.setMargin(imageView, new Insets(-10, -5, -25, 0));

        JFXTextField fieldID = new JFXTextField(String.valueOf(car.getId()));
        fieldID.setPromptText("PLACA");
        fieldID.setLabelFloat(true);
        fieldID.setPrefWidth(x);
        gridPane.add(fieldID, 0, 3, 2 ,1);

        JFXTextField fieldBrand = new JFXTextField(car.getBrand());
        fieldBrand.setPromptText("MARCA");
        fieldBrand.setLabelFloat(true);
        fieldBrand.setPrefWidth(x);
        gridPane.add(fieldBrand, 0, 4, 2 ,1);

        JFXTextField fieldModel = new JFXTextField(car.getModel());
        fieldModel.setPromptText("MODELO");
        fieldModel.setLabelFloat(true);
        fieldModel.setPrefWidth(x);
        gridPane.add(fieldModel, 0, 5, 2 ,1);

        JFXButton buttonUpdate = new JFXButton("MODIFICAR");
        buttonUpdate.getStyleClass().addAll("customButton", "primaryButton");
        buttonUpdate.setButtonType(JFXButton.ButtonType.FLAT);
        buttonUpdate.setPrefSize(x, y / 20);
        buttonUpdate.setOnAction(event -> {
            String imageRoute;
            if (fieldID.getText().length() == 0
                    || fieldBrand.getText().length() == 0
                    || fieldModel.getText().length() == 0) {
                Alert.getInstance().showAlert(gridPane, "ERROR", "UNO O MÁS DATOS SON INCORRECTOS");
            } else {
                if (FileControl.getInstance().getFileControlImage() != null
                        && !car.getPicture().equals(FileControl.getInstance().getFileControlImage().getName())) {
                    imageRoute = FileControl.getInstance().getFileControlImage().getName();
                } else {
                    imageRoute = car.getPicture();
                }
                ControllerCar.getInstance().updateCar(fieldID.getText().trim(), fieldBrand.getText().trim(),
                        fieldModel.getText().trim(), imageRoute);
                if (!ControllerCar.getInstance().updateCar()){
                    Alert.getInstance().showAlert(gridPane, "ERROR", "ERROR AL MODIFICAR AUTOMOVIL");
                } else {
                    ViewCar.getInstance().updateTableViewItems();
                    Alert.getInstance().showNotification(car.getCustomer().getName(), "AUTOMOVIL ACTUALIZADO EXITOSAMENTE");
                }
            }
        });
        gridPane.add(buttonUpdate, 0, 6);

        JFXButton buttonCancel = new JFXButton("CANCELAR");
        buttonCancel.getStyleClass().addAll("customButton", "dangerButton");
        buttonCancel.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCancel.setPrefSize(x, y / 20);
        buttonCancel.setOnAction(event -> ViewCar.getInstance().restartHBox());
        gridPane.add(buttonCancel, 1, 6);

        return gridPane;
    }
}

class ShowCar {
    private static ShowCar instance;
    private String directory;
    private ImageView imageView;

    private ShowCar() {
    }

    static ShowCar getInstance() {
        if (instance == null) {
            instance = new ShowCar();
        }
        return instance;
    }

    GridPane getGridPane(Car car) {
        GridPane gridPane = new GridPane();
        this.directory = System.getProperty("user.dir") + "/src/org/didierdominguez/assets/images/";
        FileControl.getInstance().setFileControlImage(null);

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setMaxWidth(x / 2);
        gridPane.setPadding(new Insets(22));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("MOSTRAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(20));
        gridPane.add(textTitle, 0, 0, 2, 1);

        File directory = new File(this.directory + car.getPicture());
        if (directory.exists()) {
            this.directory += car.getPicture();
        } else {
            this.directory += "image-not-found.jpg";
        }
        try {
            Image image = new Image(new FileInputStream(this.directory));
            imageView = new ImageView(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setFitWidth(x/2);
        imageView.setPreserveRatio(true);
        gridPane.add(imageView, 0, 1, 2, 1);
        gridPane.setMargin(imageView, new Insets(-10, -5, -25, 0));

        JFXTextField fieldID = new JFXTextField(String.valueOf(car.getId()));
        fieldID.setPromptText("PLACA");
        fieldID.setLabelFloat(true);
        fieldID.setPrefWidth(x);
        fieldID.setEditable(false);
        gridPane.add(fieldID, 0, 3, 2 ,1);

        JFXTextField fieldBrand = new JFXTextField(car.getBrand());
        fieldBrand.setPromptText("MARCA");
        fieldBrand.setLabelFloat(true);
        fieldBrand.setPrefWidth(x);
        fieldBrand.setEditable(false);
        gridPane.add(fieldBrand, 0, 4, 2 ,1);

        JFXTextField fieldModel = new JFXTextField(car.getModel());
        fieldModel.setPromptText("MODELO");
        fieldModel.setLabelFloat(true);
        fieldModel.setPrefWidth(x);
        fieldModel.setEditable(false);
        gridPane.add(fieldModel, 0, 5, 2 ,1);

        JFXButton buttonCopy = new JFXButton("COPIAR");
        buttonCopy.getStyleClass().addAll("customButton", "primaryButton");
        buttonCopy.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCopy.setPrefSize(x, y / 20);
        buttonCopy.setOnAction(event -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString("PLACA:         " + car.getId()
                    + "\nMARCA:         " + car.getBrand()
                    + "\nMODELO:        " + car.getModel());
            clipboard.setContent(content);
        });
        gridPane.add(buttonCopy, 0, 6);

        JFXButton buttonService = new JFXButton("SERVICIO");
        buttonService.getStyleClass().addAll("customButton", "warningButton");
        buttonService.setButtonType(JFXButton.ButtonType.FLAT);
        buttonService.setPrefSize(x, y / 20);
        gridPane.add(buttonService, 1, 6);

        return gridPane;
    }
}

